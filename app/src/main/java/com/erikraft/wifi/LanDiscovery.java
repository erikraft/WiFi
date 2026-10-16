package com.erikraft.wifi;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Discovers ErikrafT Wi-Fi peers on the current LAN using Android NSD/mDNS.
 * The tiny TCP endpoint only reports public protocol metadata; it does not
 * accept files or transmit user content. It is not an authenticated channel.
 */
final class LanDiscovery {
    interface Listener {
        void onStatus(String status);
        void onPeer(String name, String address, int port);
        default void onPeerLost(String name) { }
    }

    private static final String SERVICE_TYPE = "_erikraftwifi._tcp.";
    private final Context context;
    private final Listener listener;
    private final NsdManager nsd;
    private final String nodeName = "ErikrafT-WiFi-" + UUID.randomUUID().toString().substring(0, 8);
    private final Set<String> resolving = Collections.synchronizedSet(new HashSet<>());
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private WifiManager.MulticastLock multicastLock;
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager.RegistrationListener registrationListener;
    private ServerSocket serverSocket;
    private volatile boolean running;

    LanDiscovery(Context context, Listener listener) {
        this.context = context.getApplicationContext();
        this.listener = listener;
        this.nsd = (NsdManager) this.context.getSystemService(Context.NSD_SERVICE);
    }

    synchronized void start() {
        if (running) return;
        if (nsd == null) {
            listener.onStatus("Descoberta indisponível neste dispositivo.");
            return;
        }
        try {
            WifiManager wifi = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                multicastLock = wifi.createMulticastLock("ErikrafTWiFiDiscovery");
                multicastLock.setReferenceCounted(false);
                multicastLock.acquire();
            }
            serverSocket = new ServerSocket(0);
            running = true;
            startMetadataEndpoint(serverSocket);
            registerLocalService(serverSocket.getLocalPort());
            discoverServices();
            listener.onStatus("Procurando dispositivos ErikrafT Wi-Fi na rede local…");
        } catch (Exception e) {
            stop();
            listener.onStatus("Não foi possível iniciar a descoberta. Verifique se o Wi-Fi está conectado.");
        }
    }

    private void startMetadataEndpoint(ServerSocket socket) {
        worker.execute(() -> {
            while (running && !socket.isClosed()) {
                try (Socket client = socket.accept();
                     OutputStreamWriter writer = new OutputStreamWriter(
                             client.getOutputStream(), StandardCharsets.UTF_8)) {
                    JSONObject response = new JSONObject();
                    response.put("service", "ErikrafT Wi-Fi");
                    response.put("protocol", 1);
                    response.put("capabilities", new String[]{"discovery"});
                    response.put("fileTransfer", false);
                    writer.write(response.toString());
                    writer.write("\n");
                    writer.flush();
                } catch (Exception ignored) {
                    if (running && !socket.isClosed()) {
                        listener.onStatus("A descoberta está ativa; um dispositivo da rede não respondeu.");
                    }
                }
            }
        });
    }

    private void registerLocalService(int port) {
        NsdServiceInfo service = new NsdServiceInfo();
        service.setServiceName(nodeName);
        service.setServiceType(SERVICE_TYPE);
        service.setPort(port);
        registrationListener = new NsdManager.RegistrationListener() {
            @Override public void onRegistrationFailed(NsdServiceInfo s, int error) {
                listener.onStatus("Não foi possível anunciar este dispositivo (código " + error + ").");
            }
            @Override public void onUnregistrationFailed(NsdServiceInfo s, int error) { }
            @Override public void onServiceRegistered(NsdServiceInfo s) {
                listener.onStatus("Este dispositivo está visível na rede local. Procurando pares…");
            }
            @Override public void onServiceUnregistered(NsdServiceInfo s) { }
        };
        nsd.registerService(service, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    private void discoverServices() {
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override public void onDiscoveryStarted(String type) { }
            @Override public void onStartDiscoveryFailed(String type, int error) {
                listener.onStatus("Falha ao iniciar a descoberta (código " + error + ").");
                try { nsd.stopServiceDiscovery(this); } catch (Exception ignored) { }
            }
            @Override public void onStopDiscoveryFailed(String type, int error) { }
            @Override public void onDiscoveryStopped(String type) { }
            @Override public void onServiceLost(NsdServiceInfo service) {
                resolving.remove(service.getServiceName());
                listener.onPeerLost(service.getServiceName());
            }
            @Override public void onServiceFound(NsdServiceInfo service) {
                String name = service.getServiceName();
                if (!SERVICE_TYPE.equals(service.getServiceType()) || nodeName.equals(name)
                        || !resolving.add(name)) return;
                nsd.resolveService(service, new NsdManager.ResolveListener() {
                    @Override public void onResolveFailed(NsdServiceInfo s, int error) {
                        resolving.remove(name);
                    }
                    @Override public void onServiceResolved(NsdServiceInfo resolved) {
                        InetAddress host = resolved.getHost();
                        if (host != null) {
                            listener.onPeer(resolved.getServiceName(), host.getHostAddress(),
                                    resolved.getPort());
                        }
                    }
                });
            }
        };
        nsd.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    synchronized void stop() {
        running = false;
        if (nsd != null && discoveryListener != null) {
            try { nsd.stopServiceDiscovery(discoveryListener); } catch (Exception ignored) { }
            discoveryListener = null;
        }
        if (nsd != null && registrationListener != null) {
            try { nsd.unregisterService(registrationListener); } catch (Exception ignored) { }
            registrationListener = null;
        }
        if (serverSocket != null) {
            try { serverSocket.close(); } catch (Exception ignored) { }
            serverSocket = null;
        }
        if (multicastLock != null && multicastLock.isHeld()) {
            try { multicastLock.release(); } catch (Exception ignored) { }
            multicastLock = null;
        }
        resolving.clear();
        listener.onStatus("Descoberta parada.");
    }

    void destroy() {
        stop();
        worker.shutdownNow();
    }
}