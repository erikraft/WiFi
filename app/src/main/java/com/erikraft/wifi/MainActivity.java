package com.erikraft.wifi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/** ErikrafT Wi-Fi Android prototype: LAN discovery only, no file transfer. */
public final class MainActivity extends Activity {
    private final int blue = Color.rgb(23, 105, 255);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Set<String> peerKeys = new HashSet<>();
    private LinearLayout peerList;
    private TextView status;
    private Button discoverButton;
    private LanDiscovery discovery;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(12, 39, 91));
        getWindow().setNavigationBarColor(Color.rgb(12, 39, 91));
        discovery = new LanDiscovery(this, new LanDiscovery.Listener() {
            @Override public void onStatus(String value) {
                mainHandler.post(() -> {
                    if (status != null) status.setText(value);
                    if (discoverButton != null) discoverButton.setText(
                            value.startsWith("Descoberta parada") ? "Procurar dispositivos" : "Parar descoberta");
                });
            }
            @Override public void onPeer(String name, String address, int port) {
                mainHandler.post(() -> addPeer(name, address, port));
            }
        });

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(24), dp(28), dp(24), dp(32));
        page.setBackgroundColor(Color.rgb(246, 249, 255));
        scroll.addView(page);

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.erikraft.wifi.R.drawable.erikraft_wifi_app_icon);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams logoParams = lp(dp(58), dp(61));
        logoParams.bottomMargin = dp(14);
        page.addView(logo, logoParams);
        page.addView(label("ErikrafT Wi-Fi™", 18, blue, true));
        TextView title = label("Seus dispositivos.\nSua rede.", 34, Color.rgb(16, 26, 43), true);
        LinearLayout.LayoutParams tp = lp(-1, -2); tp.topMargin = dp(34); page.addView(title, tp);
        TextView sub = label("Descubra dispositivos ErikrafT Wi-Fi conectados à mesma rede local.", 16, Color.rgb(91, 105, 128), false);
        LinearLayout.LayoutParams sp = lp(-1, -2); sp.topMargin = dp(12); page.addView(sub, sp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(20), dp(18), dp(20));
        card.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams cp = lp(-1, -2); cp.topMargin = dp(26); page.addView(card, cp);
        card.addView(label("DESCOBERTA NA REDE LOCAL", 11, blue, true));
        status = label("Pronto para procurar dispositivos na rede Wi-Fi atual.", 14, Color.rgb(43, 57, 78), false);
        LinearLayout.LayoutParams stp = lp(-1, -2); stp.topMargin = dp(12); card.addView(status, stp);
        discoverButton = new Button(this);
        discoverButton.setText("Procurar dispositivos");
        discoverButton.setAllCaps(false);
        discoverButton.setTextColor(Color.WHITE);
        discoverButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(blue));
        LinearLayout.LayoutParams bp = lp(-1, -2); bp.topMargin = dp(18); card.addView(discoverButton, bp);
        discoverButton.setOnClickListener(v -> {
            if (discoverButton.getText().toString().startsWith("Parar")) {
                discovery.stop();
                peerKeys.clear();
                if (peerList != null) peerList.removeAllViews();
            } else {
                if (!isNetworkAvailable()) {
                    status.setText("Conecte-se a uma rede Wi-Fi e tente novamente.");
                    return;
                }
                peerKeys.clear();
                peerList.removeAllViews();
                discovery.start();
            }
        });

        TextView peersTitle = label("Dispositivos encontrados", 18, Color.rgb(16, 26, 43), true);
        LinearLayout.LayoutParams ptp = lp(-1, -2); ptp.topMargin = dp(26); page.addView(peersTitle, ptp);
        peerList = new LinearLayout(this);
        peerList.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams plp = lp(-1, -2); plp.topMargin = dp(10); page.addView(peerList, plp);
        peerList.addView(label("Nenhum dispositivo encontrado ainda.", 13, Color.rgb(112, 126, 147), false));

        TextView note = label("Estado: descoberta LAN experimental. Wi-Fi Direct, Mesh, gateways e transferência segura ainda não estão implementados. A descoberta não autentica dispositivos; não envie dados privados.", 12, Color.rgb(112, 126, 147), false);
        LinearLayout.LayoutParams np = lp(-1, -2); np.topMargin = dp(24); page.addView(note, np);

        TextView links = label("Integração futura: ErikrafT Drop™ e Drop™ for Android", 13, blue, true);
        LinearLayout.LayoutParams lnp = lp(-1, -2); lnp.topMargin = dp(16); page.addView(links, lnp);
        setContentView(scroll);

        if ("erikraftwifi".equals(getIntent().getScheme())) {
            Toast.makeText(this, "ErikrafT Wi-Fi aberto pelo link de integração.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return caps != null && (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private void addPeer(String name, String address, int port) {
        String key = name + "@" + address;
        if (!peerKeys.add(key) || peerList == null) return;
        if (peerList.getChildCount() == 1
                && ((TextView) peerList.getChildAt(0)).getText().toString().startsWith("Nenhum dispositivo")) {
            peerList.removeAllViews();
        }
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(dp(14), dp(14), dp(14), dp(14));
        row.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams rp = lp(-1, -2); rp.bottomMargin = dp(8); peerList.addView(row, rp);
        row.addView(label(name, 14, Color.rgb(25, 42, 66), true));
        TextView endpoint = label(address + ":" + port, 12, Color.rgb(91, 105, 128), false);
        LinearLayout.LayoutParams ep = lp(-1, -2); ep.topMargin = dp(4); row.addView(endpoint, ep);
        row.addView(label("Encontrado na rede local · transferência indisponível", 11, Color.rgb(112, 126, 147), false));
    }

    @Override protected void onDestroy() {
        if (discovery != null) discovery.destroy();
        super.onDestroy();
    }

    private TextView label(String s, int size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(s); v.setTextSize(size); v.setTextColor(color);
        if (bold) v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        v.setGravity(Gravity.START);
        return v;
    }
    private LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w, h); }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
}