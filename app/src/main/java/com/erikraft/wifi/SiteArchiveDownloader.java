package com.erikraft.wifi;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Downloads a bounded static copy for offline browsing. It follows same-origin
 * HTML links and downloads referenced web assets, including external static
 * assets, into a local folder. It cannot reproduce server-side actions,
 * authenticated pages, DRM, or dynamically generated API content.
 */
final class SiteArchiveDownloader {
    interface Progress { void update(String message); }
    static final class Result {
        final File entry;
        final int pages;
        final int resources;
        Result(File entry, int pages, int resources) {
            this.entry = entry; this.pages = pages; this.resources = resources;
        }
    }

    private static final int MAX_PAGES = 60;
    private static final int MAX_RESOURCES = 500;
    private static final long MAX_BYTES = 80L * 1024L * 1024L;
    private static long bytesWritten;
    private static String origin;
    private static File root;
    private static Progress progress;

    static Result download(String start, File destination, Progress listener) throws Exception {
        URI startUri = new URI(start).normalize();
        if (!("http".equalsIgnoreCase(startUri.getScheme()) || "https".equalsIgnoreCase(startUri.getScheme()))
                || startUri.getHost() == null) throw new IllegalArgumentException("URL HTTP/HTTPS inválida.");
        origin = startUri.getScheme().toLowerCase(Locale.ROOT) + "://" + startUri.getAuthority().toLowerCase(Locale.ROOT);
        root = destination;
        progress = listener;
        bytesWritten = 0;
        if (!root.exists() && !root.mkdirs()) throw new java.io.IOException("Não foi possível criar a pasta offline.");
        ArrayDeque<String> pagesToVisit = new ArrayDeque<>();
        Set<String> queued = new LinkedHashSet<>();
        Set<String> visited = new LinkedHashSet<>();
        pagesToVisit.add(stripFragment(startUri.toString()));
        queued.add(stripFragment(startUri.toString()));
        File entry = null;
        int resources = 0;
        while (!pagesToVisit.isEmpty() && visited.size() < MAX_PAGES) {
            String pageUrl = pagesToVisit.removeFirst();
            if (!visited.add(pageUrl)) continue;
            report("Baixando página " + visited.size() + " de até " + MAX_PAGES + "…");
            Response response = fetch(pageUrl);
            if (response.code < 200 || response.code >= 300) continue;
            String type = response.contentType.toLowerCase(Locale.ROOT);
            if (!type.contains("text/html") && !pageUrl.matches("(?i).*\\.(html?|xhtml)(?:[?#].*)?$")) continue;
            File pageFile = fileFor(pageUrl, true);
            write(pageFile, response.data);
            if (entry == null) entry = pageFile;
            String html = new String(response.data, StandardCharsets.UTF_8);
            Matcher matcher = Pattern.compile("(?is)\\b(href|src|poster|action)\\s*=\\s*(['\\\"])(.*?)\\2").matcher(html);
            StringBuffer rewritten = new StringBuffer();
            while (matcher.find() && resources < MAX_RESOURCES) {
                String attribute = matcher.group(1);
                String original = decodeBasicEntities(matcher.group(3).trim());
                if (original.isEmpty() || original.startsWith("#") || original.startsWith("data:")
                        || original.startsWith("javascript:") || original.startsWith("mailto:")
                        || original.startsWith("tel:")) continue;
                try {
                    URI target = new URI(pageUrl).resolve(original).normalize();
                    String targetUrl = stripFragment(target.toString());
                    if (!("http".equalsIgnoreCase(target.getScheme()) || "https".equalsIgnoreCase(target.getScheme()))
                            || target.getHost() == null) continue;
                    boolean sameSite = sameOrigin(target);
                    boolean isPageLink = "href".equalsIgnoreCase(attribute)
                            && (sameSite && !hasAssetExtension(target.getPath()));
                    if (isPageLink) {
                        if (visited.size() + queued.size() < MAX_PAGES && queued.add(targetUrl)) pagesToVisit.addLast(targetUrl);
                        String local = relative(pageFile, fileFor(targetUrl, true));
                        matcher.appendReplacement(rewritten, Matcher.quoteReplacement(attribute + "=" + matcher.group(2) + local + fragment(original) + matcher.group(2)));
                    } else if (isWebResource(target.getPath())) {
                        File resourceFile = fileFor(targetUrl, false);
                        if (!resourceFile.exists()) {
                            try {
                                Response asset = fetch(targetUrl);
                                if (asset.code >= 200 && asset.code < 300) {
                                    write(resourceFile, asset.data); resources++;
                                    if (asset.contentType.toLowerCase(Locale.ROOT).contains("text/css")) {
                                        String css = new String(asset.data, StandardCharsets.UTF_8);
                                        String cssLocal = rewriteCss(css, targetUrl, resourceFile);
                                        write(resourceFile, cssLocal.getBytes(StandardCharsets.UTF_8));
                                    }
                                }
                            } catch (Exception ignored) { }
                        }
                        if (resourceFile.exists()) {
                            String local = relative(pageFile, resourceFile);
                            matcher.appendReplacement(rewritten, Matcher.quoteReplacement(attribute + "=" + matcher.group(2) + local + fragment(original) + matcher.group(2)));
                        }
                    }
                } catch (Exception ignored) { }
            }
            matcher.appendTail(rewritten);
            write(pageFile, rewritten.toString().getBytes(StandardCharsets.UTF_8));
        }
        if (entry == null || !entry.exists()) throw new java.io.IOException("O site não retornou uma página HTML salvável.");
        return new Result(entry, visited.size(), resources);
    }

    private static String rewriteCss(String css, String baseUrl, File cssFile) {
        Matcher m = Pattern.compile("(?is)url\\(\\s*(['\\\"]?)(.*?)\\1\\s*\\)").matcher(css);
        StringBuffer out = new StringBuffer();
        while (m.find()) {
            String value = m.group(2).trim();
            if (value.isEmpty() || value.startsWith("data:") || value.startsWith("#")) continue;
            try {
                URI target = new URI(baseUrl).resolve(value).normalize();
                if (!isWebResource(target.getPath())) continue;
                File targetFile = fileFor(stripFragment(target.toString()), false);
                if (!targetFile.exists()) {
                    try {
                        Response response = fetch(stripFragment(target.toString()));
                        if (response.code >= 200 && response.code < 300) write(targetFile, response.data);
                    } catch (Exception ignored) { }
                }
                if (targetFile.exists()) {
                    String local = relative(cssFile, targetFile);
                    m.appendReplacement(out, Matcher.quoteReplacement("url(" + m.group(1) + local + m.group(1) + ")"));
                }
            } catch (Exception ignored) { }
        }
        m.appendTail(out);
        return out.toString();
    }

    private static final class Response {
        final int code; final String contentType; final byte[] data;
        Response(int code, String type, byte[] data) { this.code = code; this.contentType = type == null ? "" : type; this.data = data; }
    }
    private static Response fetch(String address) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(address).openConnection();
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(15000);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("User-Agent", "ErikrafT-WiFi-Offline/1.0");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,text/css,image/*,*/*;q=0.8");
        try {
            int code = connection.getResponseCode();
            InputStream stream = code >= 400 ? connection.getErrorStream() : connection.getInputStream();
            if (stream == null) return new Response(code, connection.getContentType(), new byte[0]);
            try (InputStream in = stream; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[8192]; int count; int limit = 20 * 1024 * 1024;
                while ((count = in.read(buffer)) != -1) {
                    if (out.size() + count > limit || bytesWritten + out.size() + count > MAX_BYTES) break;
                    out.write(buffer, 0, count);
                }
                return new Response(code, connection.getContentType(), out.toByteArray());
            }
        } finally { connection.disconnect(); }
    }
    private static boolean sameOrigin(URI uri) {
        String candidate = uri.getScheme() + "://" + uri.getAuthority();
        return candidate.equalsIgnoreCase(origin);
    }
    private static boolean isWebResource(String path) {
        return path != null && path.matches("(?i).*\\.(css|js|mjs|png|jpe?g|gif|webp|svg|ico|woff2?|ttf|otf|eot|mp4|webm|ogg|mp3|wav|pdf|json|xml|txt|wasm)$");
    }
    private static boolean hasAssetExtension(String path) { return isWebResource(path); }
    private static File fileFor(String address, boolean page) throws Exception {
        URI uri = new URI(address).normalize();
        String path = uri.getPath();
        if (path == null || path.isEmpty()) path = "/";
        String host = uri.getHost() == null ? "site" : uri.getHost().replaceAll("[^A-Za-z0-9.-]", "_");
        boolean external = !sameOrigin(uri);
        String relativePath = (external ? "_external/" + host : "") + path;
        if (relativePath.endsWith("/")) relativePath += "index.html";
        else if (page && !hasAssetExtension(relativePath) && !relativePath.matches("(?i).*\\.(html?|xhtml)$")) relativePath += "/index.html";
        File file = new File(root, relativePath.replaceAll("\\\\", "/"));
        String canonicalRoot = root.getCanonicalPath() + File.separator;
        if (!file.getCanonicalPath().startsWith(canonicalRoot)) throw new java.io.IOException("Caminho inválido.");
        return file;
    }
    private static void write(File file, byte[] data) throws Exception {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) throw new java.io.IOException("Falha ao criar pasta.");
        try (FileOutputStream out = new FileOutputStream(file)) { out.write(data); }
        bytesWritten += data.length;
    }
    private static String relative(File from, File to) {
        try {
            String base = from.getParentFile().getCanonicalPath();
            String target = to.getCanonicalPath();
            String[] baseParts = base.replace('\\\\', '/').split("/");
            String[] targetParts = target.replace('\\\\', '/').split("/");
            int common = 0;
            while (common < baseParts.length && common < targetParts.length
                    && baseParts[common].equals(targetParts[common])) common++;
            StringBuilder relative = new StringBuilder();
            for (int i = common; i < baseParts.length; i++) {
                if (!baseParts[i].isEmpty()) relative.append("../");
            }
            for (int i = common; i < targetParts.length; i++) {
                if (!targetParts[i].isEmpty()) {
                    if (relative.length() > 0 && relative.charAt(relative.length() - 1) != '/') relative.append('/');
                    relative.append(targetParts[i]);
                }
            }
            return relative.length() == 0 ? new File(target).getName() : relative.toString();
        } catch (Exception e) { return Uri.encode(to.getName()); }
    }
    private static String fragment(String value) {
        int index = value.indexOf('#'); return index >= 0 ? value.substring(index) : "";
    }
    private static String stripFragment(String value) {
        int index = value.indexOf('#'); return index >= 0 ? value.substring(0, index) : value;
    }
    private static String decodeBasicEntities(String value) {
        return value.replace("&amp;", "&").replace("&#38;", "&").replace("&quot;", "\"").replace("&#39;", "'");
    }
    private static void report(String message) { if (progress != null) progress.update(message); }
    private SiteArchiveDownloader() { }
}
