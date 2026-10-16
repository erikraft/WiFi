package com.erikraft.wifi;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Build;
import android.util.Rational;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/** ErikrafT Wi-Fi Android prototype: LAN discovery only, no file transfer. */
public final class MainActivity extends Activity {
    private final int blue = Color.rgb(66, 133, 244);
    private int pageColor;
    private int surfaceColor;
    private int primaryTextColor;
    private int secondaryTextColor;
    private int softAccentColor;
    private int borderColor;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Set<String> peerKeys = new HashSet<>();
    private LinearLayout peerList;
    private TextView status;
    private Button discoverButton;
    private Button advancedButton;
    private Button pipButton;
    private TextView advancedDetails;
    private boolean advancedVisible;
    private boolean pipEnabled;
    private LanDiscovery discovery;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        boolean night = (getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        pageColor = night ? Color.rgb(13, 18, 29) : Color.rgb(246, 248, 252);
        surfaceColor = night ? Color.rgb(24, 31, 45) : Color.WHITE;
        primaryTextColor = night ? Color.rgb(239, 243, 250) : Color.rgb(24, 35, 53);
        secondaryTextColor = night ? Color.rgb(169, 182, 202) : Color.rgb(91, 105, 128);
        softAccentColor = night ? Color.rgb(35, 55, 86) : softAccentColor;
        borderColor = night ? Color.rgb(48, 61, 81) : Color.rgb(225, 232, 242);
        getWindow().setStatusBarColor(pageColor);
        getWindow().setNavigationBarColor(pageColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(night ? 0
                    : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        pipEnabled = getPreferences(MODE_PRIVATE).getBoolean("pip_enabled", false);
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
        page.setPadding(dp(22), dp(20), dp(22), dp(30));
        page.setBackgroundColor(pageColor);
        scroll.addView(page);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);
        page.addView(header, lp(-1, dp(54)));

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.erikraft.wifi.R.drawable.erikraft_wifi_mark);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        header.addView(logo, lp(dp(42), dp(44)));
        TextView brand = label("ErikrafT Wi-Fi™", 18, primaryTextColor, true);
        LinearLayout.LayoutParams brandParams = lp(0, -2);
        brandParams.weight = 1;
        brandParams.leftMargin = dp(12);
        header.addView(brand, brandParams);

        ImageButton settingsButton = new ImageButton(this);
        settingsButton.setContentDescription("Configurações");
        settingsButton.setImageResource(com.erikraft.wifi.R.drawable.ic_settings_24);
        settingsButton.setColorFilter(primaryTextColor);
        settingsButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(softAccentColor));
        settingsButton.setBackgroundResource(com.erikraft.wifi.R.drawable.bg_settings_button);
        header.addView(settingsButton, lp(dp(46), dp(46)));
        settingsButton.setOnClickListener(v -> {
            advancedVisible = !advancedVisible;
            advancedDetails.setVisibility(advancedVisible ? View.VISIBLE : View.GONE);
            Toast.makeText(this, advancedVisible ? "Configurações avançadas abertas."
                    : "Configurações avançadas recolhidas.", Toast.LENGTH_SHORT).show();
            if (advancedVisible) advancedDetails.getParent().requestChildFocus(advancedDetails, advancedDetails);
        });
        TextView title = label("Seus dispositivos.\nSua rede.", 32, primaryTextColor, true);
        LinearLayout.LayoutParams tp = lp(-1, -2); tp.topMargin = dp(34); page.addView(title, tp);
        TextView sub = label("Descubra dispositivos ErikrafT Wi-Fi conectados à mesma rede local.", 15, secondaryTextColor, false);
        LinearLayout.LayoutParams sp = lp(-1, -2); sp.topMargin = dp(12); page.addView(sub, sp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(20), dp(18), dp(20));
        card.setBackground(surface(0, surfaceColor, borderColor));
        LinearLayout.LayoutParams cp = lp(-1, -2); cp.topMargin = dp(26); page.addView(card, cp);
        card.addView(label("DESCOBERTA NA REDE LOCAL", 11, blue, true));
        status = label("Pronto para procurar dispositivos na rede Wi-Fi atual.", 14, primaryTextColor, false);
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

        advancedButton = new Button(this);
        advancedButton.setText("Configurações avançadas");
        advancedButton.setAllCaps(false);
        advancedButton.setTextColor(blue);
        advancedButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(softAccentColor));
        LinearLayout.LayoutParams abp = lp(-1, -2); abp.topMargin = dp(10); card.addView(advancedButton, abp);

        pipButton = new Button(this);
        pipButton.setAllCaps(false);
        pipButton.setTextColor(blue);
        pipButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(softAccentColor));
        pipButton.setText(pipEnabled ? "Desativar PiP ao sair do app" : "Ativar PiP ao sair do app");
        LinearLayout.LayoutParams pip = lp(-1, -2); pip.topMargin = dp(8); card.addView(pipButton, pip);
        pipButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Toast.makeText(this, "PiP exige Android 8.0 ou superior.", Toast.LENGTH_LONG).show();
                return;
            }
            pipEnabled = !pipEnabled;
            getPreferences(MODE_PRIVATE).edit().putBoolean("pip_enabled", pipEnabled).apply();
            pipButton.setText(pipEnabled ? "Desativar PiP ao sair do app" : "Ativar PiP ao sair do app");
            Toast.makeText(this, pipEnabled
                    ? "PiP ativado. Ao sair do app, a janela pode permanecer flutuante."
                    : "PiP desativado.", Toast.LENGTH_SHORT).show();
        });

        advancedDetails = label("Avançado: a descoberta atual usa NSD/mDNS na rede local. Wi-Fi Direct, roteamento mesh, seleção de gateway, encaminhamento de tráfego e transporte criptografado ainda não estão implementados. Só use gateways confiáveis e autorizados.", 13, secondaryTextColor, false);
        advancedDetails.setVisibility(View.GONE);
        LinearLayout.LayoutParams adp = lp(-1, -2); adp.topMargin = dp(10); card.addView(advancedDetails, adp);
        advancedButton.setOnClickListener(v -> {
            advancedVisible = !advancedVisible;
            advancedDetails.setVisibility(advancedVisible ? View.VISIBLE : View.GONE);
            advancedButton.setText(advancedVisible ? "Ocultar configurações avançadas" : "Configurações avançadas");
        });

        TextView peersTitle = label("Dispositivos encontrados", 18, primaryTextColor, true);
        LinearLayout.LayoutParams ptp = lp(-1, -2); ptp.topMargin = dp(26); page.addView(peersTitle, ptp);
        peerList = new LinearLayout(this);
        peerList.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams plp = lp(-1, -2); plp.topMargin = dp(10); page.addView(peerList, plp);
        peerList.addView(label("Nenhum dispositivo encontrado ainda.", 13, Color.rgb(112, 126, 147), false));

        TextView note = label("PROTÓTIPO · A descoberta LAN é experimental. Wi-Fi Direct, Mesh, gateways e transferência segura ainda não estão implementados. A descoberta não autentica dispositivos; não envie dados privados.", 12, secondaryTextColor, false);
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
        row.setBackground(surface(0, surfaceColor, borderColor));
        LinearLayout.LayoutParams rp = lp(-1, -2); rp.bottomMargin = dp(8); peerList.addView(row, rp);
        row.addView(label(name, 14, primaryTextColor, true));
        TextView endpoint = label(address + ":" + port, 12, secondaryTextColor, false);
        LinearLayout.LayoutParams ep = lp(-1, -2); ep.topMargin = dp(4); row.addView(endpoint, ep);
        row.addView(label("Encontrado na rede local · transferência indisponível", 11, secondaryTextColor, false));
    }

    @Override protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (pipEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                PictureInPictureParams params = new PictureInPictureParams.Builder()
                        .setAspectRatio(new Rational(9, 16))
                        .build();
                enterPictureInPictureMode(params);
            } catch (IllegalStateException ignored) {
                // PiP can be unavailable on some devices, window modes, or system configurations.
            }
        }
    }

    @Override protected void onDestroy() {
        if (discovery != null) discovery.destroy();
        super.onDestroy();
    }

    private android.graphics.drawable.GradientDrawable surface(int ignored, int fill, int stroke) {
        android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
        shape.setColor(fill);
        shape.setCornerRadius(dp(18));
        shape.setStroke(dp(1), stroke);
        return shape;
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