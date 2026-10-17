package com.erikraft.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.ClipData;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Offline web library. MHTML archives preserve the rendered page and embedded
 * resources where WebView supports them; they cannot guarantee every dynamic
 * site, cross-origin resource, login or DRM-protected media will be archived.
 */
public final class OfflineLibraryActivity extends Activity {
    private final int bg = Color.rgb(5, 8, 13);
    private final int surface = Color.rgb(15, 23, 35);
    private final int blue = Color.rgb(66, 133, 244);
    private WebView web;
    private TextView status;
    private EditText address;
    private File archiveDir;
    private String lastArchive;
    private LanDiscovery globeDiscovery;
    private final Map<String, String[]> livePeers = new HashMap<>();

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        archiveDir = new File(getFilesDir(), "offline-sites");
        if (!archiveDir.exists()) archiveDir.mkdirs();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(bg);
        root.setPadding(dp(14), dp(12), dp(14), dp(8));

        LinearLayout heading = new LinearLayout(this);
        heading.setGravity(Gravity.CENTER_VERTICAL);
        Button back = button("← Voltar", surface);
        back.setContentDescription(AppStrings.t(this, "Navegar para cima"));
        addIcon(back, android.R.drawable.ic_media_previous);
        heading.addView(back);
        TextView title = text("ErikrafT Wi-Fi  /  Offline", 18, Color.WHITE, true);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, -2, 1);
        titleParams.leftMargin = dp(8);
        heading.addView(title, titleParams);
        Button language = button("Idioma", surface);
        addIcon(language, android.R.drawable.ic_menu_manage);
        heading.addView(language);
        Button home = button("Globo", surface);
        addIcon(home, android.R.drawable.ic_menu_mapmode);
        heading.addView(home);
        root.addView(heading);

        address = new EditText(this);
        address.setSingleLine(true);
        address.setTextColor(Color.WHITE);
        address.setHintTextColor(Color.GRAY);
        address.setHint(AppStrings.t(this, "https://wifi.erikraft.com/"));
        address.setText(AppStrings.t(this, "https://wifi.erikraft.com/"));
        address.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_URI);
        address.setSelectAllOnFocus(true);
        address.setPadding(dp(12), 0, dp(12), 0);
        address.setBackgroundColor(surface);
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, dp(48));
        ap.topMargin = dp(10);
        root.addView(address, ap);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.VERTICAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        Button openUrl = button("Abrir site", blue);
        addIcon(openUrl, android.R.drawable.ic_menu_view);
        Button save = button("Baixar site offline", blue);
        addIcon(save, android.R.drawable.ic_menu_save);
        Button library = button("Biblioteca", surface);
        addIcon(library, android.R.drawable.ic_menu_agenda);
        Button importPage = button("Importar", surface);
        addIcon(importPage, android.R.drawable.ic_menu_upload);
        LinearLayout actionRow1 = new LinearLayout(this);
        actionRow1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout actionRow2 = new LinearLayout(this);
        actionRow2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams actionButton = new LinearLayout.LayoutParams(0, dp(46), 1);
        actionButton.rightMargin = dp(5);
        actionRow1.addView(openUrl, actionButton);
        LinearLayout.LayoutParams downloadParams = new LinearLayout.LayoutParams(0, dp(46), 1);
        downloadParams.leftMargin = dp(5);
        actionRow1.addView(save, downloadParams);
        LinearLayout.LayoutParams libraryParams = new LinearLayout.LayoutParams(0, dp(46), 1);
        libraryParams.rightMargin = dp(5);
        actionRow2.addView(library, libraryParams);
        LinearLayout.LayoutParams importParams = new LinearLayout.LayoutParams(0, dp(46), 1);
        importParams.leftMargin = dp(5);
        actionRow2.addView(importPage, importParams);
        LinearLayout.LayoutParams row2Params = new LinearLayout.LayoutParams(-1, -2);
        row2Params.topMargin = dp(6);
        actions.addView(actionRow1);
        actions.addView(actionRow2, row2Params);
        root.addView(actions);

        status = text("Páginas são salvas neste dispositivo. Importe um HTML/MHTML exportado ou compartilhado pelo Chrome.", 12, Color.rgb(164,178,197), false);
        LinearLayout.LayoutParams st = new LinearLayout.LayoutParams(-1, -2);
        st.topMargin = dp(6);
        st.bottomMargin = dp(6);
        root.addView(status, st);

        web = new WebView(this);
        web.setBackgroundColor(bg);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowContentAccess(true);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")
                        || url.startsWith("file://") || url.startsWith("content://")) return false;
                return true;
            }
            @Override public void onPageFinished(WebView view, String url) {
                if (url.startsWith("file:///android_asset/mesh_globe.html")) {
                    publishPeersToGlobe();
                } else if (status != null && (url.startsWith("http") || url.startsWith("file:"))) {
                    status.setText(AppStrings.t(OfflineLibraryActivity.this, url.startsWith("file:")
                            ? "Visualizando arquivo local/offline."
                            : "Site aberto. Use “Salvar offline” para criar um arquivo MHTML."));
                }
            }
        });
        root.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);

        back.setOnClickListener(v -> { if (web != null && web.canGoBack()) web.goBack(); else showGlobe(); });
        language.setOnClickListener(v -> showLanguagePicker());
        home.setOnClickListener(v -> showGlobe());
        openUrl.setOnClickListener(v -> openAddress());
        save.setOnClickListener(v -> downloadWebsite());
        library.setOnClickListener(v -> showLibrary());
        importPage.setOnClickListener(v -> chooseImport());
        showGlobe();
        handleIncoming(getIntent());
    }

    private void showLanguagePicker() {
        String[] names = AppStrings.languageNames();
        int selected = 0;
        String code = AppStrings.selectedCode(this);
        String[] codes = AppStrings.languageCodes();
        for (int i = 0; i < codes.length; i++) if (codes[i].equalsIgnoreCase(code)) selected = i;
        new AlertDialog.Builder(this)
                .setTitle(AppStrings.t(this, "Escolher idioma"))
                .setSingleChoiceItems(names, selected, (dialog, which) -> {
                    AppStrings.select(this, codes[which]);
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton(AppStrings.t(this, "Fechar"), null)
                .show();
    }

    private void downloadWebsite() {
        String current = web == null ? null : web.getUrl();
        if (current == null || !(current.startsWith("http://") || current.startsWith("https://"))) {
            String value = address.getText().toString().trim();
            if (!value.matches("(?i)^https?://.*")) value = "https://" + value;
            current = value;
        }
        final String startUrl = current;
        try {
            Uri parsed = Uri.parse(startUrl);
            if (parsed.getHost() == null || !"https".equalsIgnoreCase(parsed.getScheme())) {
                toast("Informe um endereço HTTPS válido.");
                return;
            }
            String host = parsed.getHost().replaceAll("[^A-Za-z0-9.-]", "_");
            File siteDir = new File(archiveDir, "site-" + host + "-" + System.currentTimeMillis());
            status.setText(AppStrings.t(this, "Baixando site para uso offline…"));
            new Thread(() -> {
                try {
                    SiteArchiveDownloader.Result result = SiteArchiveDownloader.download(startUrl, siteDir,
                            message -> runOnUiThread(() -> { if (status != null) status.setText(message); }));
                    runOnUiThread(() -> {
                        lastArchive = result.entry.getAbsolutePath();
                        address.setText(startUrl);
                        web.loadUrl(Uri.fromFile(result.entry).toString());
                        status.setText(AppStrings.t(this, "Site salvo para uso offline.") + " " + result.pages
                                + " páginas, " + result.resources + " recursos.");
                        toast("Site salvo para uso offline.");
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        status.setText(AppStrings.t(this, "Não foi possível baixar o site. Verifique a conexão e tente novamente."));
                        toast("Falha ao baixar o site: " + (e.getMessage() == null ? "erro de rede" : e.getMessage()));
                    });
                }
            }, "wifi-site-downloader").start();
        } catch (Exception e) {
            toast("Informe um endereço HTTPS válido.");
        }
    }

    private void showGlobe() {
        address.setText(AppStrings.t(this, "https://wifi.erikraft.com/"));
        status.setText(AppStrings.t(this, "Globo visual de dispositivos encontrados na rede local; posições não são geográficas."));
        String globeUrl = "file:///android_asset/mesh_globe.html";
        String selectedLanguage = AppStrings.selectedCode(this);
        if (!selectedLanguage.isEmpty()) globeUrl += "?lang=" + Uri.encode(selectedLanguage);
        web.loadUrl(globeUrl);
        if (globeDiscovery != null) globeDiscovery.stop();
        synchronized (livePeers) { livePeers.clear(); }
        globeDiscovery = new LanDiscovery(this, new LanDiscovery.Listener() {
            @Override public void onStatus(String value) {
                runOnUiThread(() -> {
                    if (status != null && web != null && "file:///android_asset/mesh_globe.html".equals(web.getUrl())) {
                        status.setText(AppStrings.t(OfflineLibraryActivity.this, value));
                    }
                });
            }
            @Override public void onPeer(String name, String host, int port) {
                runOnUiThread(() -> {
                    synchronized (livePeers) {
                        livePeers.put(name + "@" + host, new String[]{name, host, String.valueOf(port)});
                    }
                    publishPeersToGlobe();
                });
            }
            @Override public void onPeerLost(String name) {
                runOnUiThread(() -> {
                    synchronized (livePeers) {
                        livePeers.entrySet().removeIf(entry -> entry.getValue()[0].equals(name));
                    }
                    publishPeersToGlobe();
                });
            }
        });
        globeDiscovery.start();
    }

    private void publishPeersToGlobe() {
        if (web == null) return;
        JSONArray peers = new JSONArray();
        synchronized (livePeers) {
            for (String[] peer : livePeers.values()) {
                JSONObject item = new JSONObject();
                try {
                    item.put("name", peer[0]);
                    item.put("address", peer[1]);
                    item.put("port", peer[2]);
                    peers.put(item);
                } catch (Exception ignored) { }
            }
        }
        web.evaluateJavascript("if(window.ErikrafTMeshUpdatePeers){window.ErikrafTMeshUpdatePeers(" + peers.toString() + ");}", null);
    }

    @Override protected void onDestroy() {
        if (globeDiscovery != null) {
            globeDiscovery.destroy();
            globeDiscovery = null;
        }
        if (web != null) {
            web.stopLoading();
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }

    private void openAddress() {
        String value = address.getText().toString().trim();
        if (value.isEmpty()) return;
        if (!value.matches("(?i)^https?://.*")) value = "https://" + value;
        try {
            Uri uri = Uri.parse(value);
            if (uri.getHost() == null) throw new IllegalArgumentException();
            web.loadUrl(uri.toString());
            status.setText(AppStrings.t(this, "Carregando site. É necessária conexão para abrir páginas que ainda não foram salvas."));
        } catch (Exception e) {
            toast("Informe um endereço HTTP ou HTTPS válido.");
        }
    }

    private void saveArchive() {
        String current = web.getUrl();
        if (current == null || !(current.startsWith("http://") || current.startsWith("https://"))) {
            toast("Abra primeiro um site online para salvar sua versão offline.");
            return;
        }
        String stamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(new Date());
        String host = Uri.parse(current).getHost();
        if (host == null) host = "site";
        host = host.replaceAll("[^A-Za-z0-9.-]", "_");
        File target = new File(archiveDir, host + "-" + stamp + ".mht");
        status.setText(AppStrings.t(this, "Salvando página e recursos disponíveis…"));
        web.saveWebArchive(target.getAbsolutePath(), true, path -> runOnUiThread(() -> {
            if (path != null && new File(path).exists() && new File(path).length() > 0) {
                lastArchive = path;
                status.setText(AppStrings.t(this, "Salvo offline: " + new File(path).getName()));
                toast("Página salva para leitura offline.");
            } else {
                status.setText(AppStrings.t(this, "O WebView não conseguiu criar o arquivo offline desta página."));
                toast("Falha ao salvar. Algumas páginas bloqueiam ou não suportam arquivamento.");
            }
        }));
    }

    private void showLibrary() {
        File[] files = archiveDir.listFiles((dir, name) -> name.endsWith(".mht") || name.endsWith(".mhtml") || name.endsWith(".html") || name.endsWith(".htm") || (new File(dir, name).isDirectory() && new File(new File(dir, name), "index.html").exists()));
        if (files == null || files.length == 0) {
            new AlertDialog.Builder(this).setTitle(AppStrings.t(this, "Biblioteca offline"))
                    .setMessage(AppStrings.t(this, "Ainda não há páginas salvas. Abra um site e toque em “Salvar offline”, ou importe um arquivo HTML/MHTML baixado."))
                    .setPositiveButton(AppStrings.t(this, "Importar"), (d,w) -> chooseImport())
                    .setNegativeButton(AppStrings.t(this, "Fechar"), null).show();
            return;
        }
        String[] names = new String[files.length];
        for (int i=0;i<files.length;i++) names[i] = files[i].getName();
        new AlertDialog.Builder(this).setTitle(AppStrings.t(this, "Páginas neste dispositivo"))
                .setItems(names, (d, which) -> {
                    File chosen = files[which];
                    File readable = chosen.isDirectory() ? new File(chosen, "index.html") : chosen;
                    lastArchive = readable.getAbsolutePath();
                    web.loadUrl(Uri.fromFile(readable).toString());
                    status.setText(AppStrings.t(this, chosen.isDirectory()
                            ? "Site salvo para uso offline." : "Arquivo local: " + chosen.getName()));
                    if (chosen.isDirectory()) return;
                    new AlertDialog.Builder(this).setTitle(chosen.getName())
                            .setItems(AppStrings.translateArray(this, new String[]{"Ler offline", "Exportar arquivo", "Compartilhar offline"}), (dialog, action) -> {
                                if (action == 0) web.loadUrl(Uri.fromFile(chosen).toString());
                                else if (action == 1) exportArchive(chosen);
                                else shareArchive(chosen);
                            }).show();
                }).setNegativeButton(AppStrings.t(this, "Fechar"), null).show();
    }

    private void shareArchive(File file) {
        try {
            Uri uri = OfflineArchiveProvider.uriFor(this, file);
            Intent share = new Intent(Intent.ACTION_SEND);
            String lower = file.getName().toLowerCase(Locale.ROOT);
            share.setType(lower.endsWith(".html") || lower.endsWith(".htm")
                    ? "text/html" : "application/x-mimearchive");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setClipData(ClipData.newUri(getContentResolver(), file.getName(), uri));
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(share, AppStrings.t(this, "Compartilhar página offline")));
        } catch (Exception e) {
            toast("Não foi possível compartilhar este arquivo offline.");
        }
    }

    private void chooseImport() {
        Intent pick = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pick.addCategory(Intent.CATEGORY_OPENABLE);
        pick.setType("*/*");
        pick.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"text/html", "application/octet-stream", "application/x-mimearchive", "message/rfc822"});
        startActivityForResult(pick, 4102);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4102 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try (InputStream in = getContentResolver().openInputStream(uri)) {
                String name = "imported-" + System.currentTimeMillis() + ".mht";
                String display = queryDisplayName(uri);
                if (display != null && display.matches("(?i).+\\.(mhtml|mht|html|htm)$")) name = display.replaceAll("[^A-Za-z0-9._-]", "_");
                File out = new File(archiveDir, name);
                try (OutputStream os = new FileOutputStream(out)) {
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = in.read(buffer)) != -1) os.write(buffer, 0, count);
                }
                lastArchive = out.getAbsolutePath();
                web.loadUrl(Uri.fromFile(out).toString());
                status.setText(AppStrings.t(this, "Importado para leitura local: " + out.getName()));
                toast("Para páginas salvas pelo Chrome, exporte ou compartilhe o arquivo; o armazenamento privado não pode ser lido diretamente.");
            } catch (Exception e) {
                toast("Não foi possível importar esse arquivo.");
            }
        } else if (requestCode == 4103 && resultCode == RESULT_OK && data != null && data.getData() != null && lastArchive != null) {
            try (InputStream in = new FileInputStream(lastArchive);
                 OutputStream out = getContentResolver().openOutputStream(data.getData(), "wt")) {
                if (out == null) throw new IllegalStateException();
                byte[] buffer = new byte[8192]; int n;
                while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
                toast("Arquivo offline exportado.");
            } catch (Exception e) { toast("Falha ao exportar o arquivo."); }
        }
    }

    private boolean isOfflineWebArchive(String name, String mimeType) {
        if (name != null) {
            String lower = name.toLowerCase(Locale.ROOT);
            if (lower.endsWith(".html") || lower.endsWith(".htm")
                    || lower.endsWith(".mhtml") || lower.endsWith(".mht")) return true;
        }
        if (mimeType == null) return false;
        return "text/html".equalsIgnoreCase(mimeType)
                || "application/x-mimearchive".equalsIgnoreCase(mimeType)
                || "message/rfc822".equalsIgnoreCase(mimeType)
                || "multipart/related".equalsIgnoreCase(mimeType);
    }

    private String offlineArchiveExtension(String name, String mimeType) {
        if (name != null) {
            String lower = name.toLowerCase(Locale.ROOT);
            if (lower.endsWith(".html")) return ".html";
            if (lower.endsWith(".htm")) return ".htm";
            if (lower.endsWith(".mhtml")) return ".mhtml";
            if (lower.endsWith(".mht")) return ".mht";
        }
        if ("text/html".equalsIgnoreCase(mimeType)) return ".html";
        return ".mht";
    }

    private String queryDisplayName(Uri uri) {
        try (android.database.Cursor c = getContentResolver().query(uri, null, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                int i = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (i >= 0) return c.getString(i);
            }
        } catch (Exception ignored) { }
        return null;
    }

    private void exportArchive(File file) {
        lastArchive = file.getAbsolutePath();
        Intent save = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        save.addCategory(Intent.CATEGORY_OPENABLE);
        save.setType("application/octet-stream");
        save.putExtra(Intent.EXTRA_TITLE, file.getName());
        startActivityForResult(save, 4103);
    }

    private void handleIncoming(Intent intent) {
        if (intent == null) return;
        Uri shared = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (shared != null) {
            try {
                String sharedName = queryDisplayName(shared);
                if (!isOfflineWebArchive(sharedName, intent.getType())) {
                    status.setText(AppStrings.t(this, "Este arquivo não é uma página HTML/MHTML offline. O envio de áudio e vídeo ainda não está implementado."));
                    return;
                }
                String extension = offlineArchiveExtension(sharedName, intent.getType());
                File out = new File(archiveDir, "shared-" + System.currentTimeMillis() + extension);
                try (InputStream in = getContentResolver().openInputStream(shared);
                     OutputStream os = new FileOutputStream(out)) {
                    if (in == null) throw new IllegalStateException();
                    byte[] b = new byte[8192]; int n;
                    while ((n = in.read(b)) != -1) os.write(b, 0, n);
                }
                lastArchive = out.getAbsolutePath();
                web.loadUrl(Uri.fromFile(out).toString());
                status.setText(AppStrings.t(this, "Página compartilhada aberta localmente."));
                return;
            } catch (Exception ignored) { }
        }
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (text != null) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("https?://\\S+").matcher(text);
            if (m.find()) {
                address.setText(m.group());
                status.setText(AppStrings.t(this, "O compartilhamento contém somente um endereço, não o arquivo offline do Chrome. Importe ou compartilhe o próprio arquivo HTML/MHTML para ler sem conexão."));
            }
        } else if (intent.getData() != null) {
            Uri uri = intent.getData();
            if ("content".equals(uri.getScheme())) {
                String display = queryDisplayName(uri);
                String extension = offlineArchiveExtension(display, getContentResolver().getType(uri));
                try (InputStream in = getContentResolver().openInputStream(uri)) {
                    File out = new File(archiveDir, "opened-" + System.currentTimeMillis() + extension);
                    try (OutputStream os = new FileOutputStream(out)) {
                        if (in == null) throw new IllegalStateException();
                        byte[] b = new byte[8192]; int n; while ((n=in.read(b))!=-1) os.write(b,0,n);
                    }
                    web.loadUrl(Uri.fromFile(out).toString());
                } catch (Exception ignored) { }
            }
        }
    }

    private void addIcon(Button button, int resource) {
        button.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        button.setCompoundDrawablePadding(dp(5));
        button.setCompoundDrawableTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
    }

    private Button button(String caption, int color) {
        Button b = new Button(this);
        b.setText(AppStrings.t(this, caption)); b.setTextSize(11); b.setAllCaps(false); b.setTextColor(Color.WHITE);
        android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(14));
        shape.setStroke(dp(1), color == blue ? Color.rgb(82, 151, 255) : Color.rgb(43, 55, 73));
        b.setBackground(shape);
        b.setPadding(dp(12), 0, dp(12), 0);
        b.setMinHeight(dp(42));
        return b;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(AppStrings.t(this, value)); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD);
        return t;
    }
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density + 0.5f); }
    private void toast(String message) { Toast.makeText(this, AppStrings.t(this, message), Toast.LENGTH_LONG).show(); }
}
