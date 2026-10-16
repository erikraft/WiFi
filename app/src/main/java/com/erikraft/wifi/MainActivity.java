package com.erikraft.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/** ErikrafT Wi-Fi Android prototype: LAN discovery only, no file transfer. */
public final class MainActivity extends Activity {
    private final int blue = Color.rgb(66, 133, 244);
    private final int background = Color.rgb(12, 18, 30);
    private final int surface = Color.rgb(22, 31, 48);
    private final int primaryText = Color.rgb(238, 243, 251);
    private final int secondaryText = Color.rgb(166, 180, 201);
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
        getWindow().setStatusBarColor(background);
        getWindow().setNavigationBarColor(background);
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
        page.setPadding(dp(24), dp(28), dp(24), dp(32));
        page.setBackgroundColor(background);
        scroll.addView(page);

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.erikraft.wifi.R.drawable.erikraft_wifi_app_icon);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams logoParams = lp(dp(58), dp(61));
        logoParams.bottomMargin = dp(14);
        page.addView(logo, logoParams);
        LinearLayout topBar = new LinearLayout(this);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        TextView brand = label("ErikrafT Wi-Fi™", 18, blue, true);
        topBar.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));
        Button websiteButton = new Button(this);
        websiteButton.setText("Site oficial ↗");
        websiteButton.setAllCaps(false);
        websiteButton.setTextColor(primaryText);
        websiteButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(surface));
        topBar.addView(websiteButton, lp(-2, -2));
        websiteButton.setOnClickListener(v -> {
            Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wifi.erikraft.com/"));
            startActivity(web);
        });
        page.addView(topBar);
        TextView title = label("Seus dispositivos.\nSua rede.", 34, primaryText, true);
        LinearLayout.LayoutParams tp = lp(-1, -2); tp.topMargin = dp(34); page.addView(title, tp);
        TextView sub = label("Descubra dispositivos ErikrafT Wi-Fi conectados à mesma rede local.", 16, secondaryText, false);
        LinearLayout.LayoutParams sp = lp(-1, -2); sp.topMargin = dp(12); page.addView(sub, sp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(20), dp(18), dp(20));
        card.setBackgroundColor(surface);
        LinearLayout.LayoutParams cp = lp(-1, -2); cp.topMargin = dp(26); page.addView(card, cp);
        card.addView(label("DESCOBERTA NA REDE LOCAL", 11, blue, true));
        status = label("Pronto para procurar dispositivos na rede Wi-Fi atual.", 14, primaryText, false);
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
        advancedButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(35, 52, 78)));
        LinearLayout.LayoutParams abp = lp(-1, -2); abp.topMargin = dp(10); card.addView(advancedButton, abp);

        Button settingsButton = new Button(this);
        settingsButton.setText("Configurações e diagnóstico");
        settingsButton.setAllCaps(false);
        settingsButton.setTextColor(primaryText);
        settingsButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(35, 52, 78)));
        LinearLayout.LayoutParams settingsParams = lp(-1, -2);
        settingsParams.topMargin = dp(10);
        card.addView(settingsButton, settingsParams);
        settingsButton.setOnClickListener(v -> showSettingsDialog());

        pipButton = new Button(this);
        pipButton.setAllCaps(false);
        pipButton.setTextColor(blue);
        pipButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(surface));
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

        advancedDetails = label("Avançado: a descoberta atual usa NSD/mDNS na rede local. Wi-Fi Direct, roteamento mesh, seleção de gateway, encaminhamento de tráfego e transporte criptografado ainda não estão implementados. Só use gateways confiáveis e autorizados.", 13, secondaryText, false);
        advancedDetails.setVisibility(View.GONE);
        LinearLayout.LayoutParams adp = lp(-1, -2); adp.topMargin = dp(10); card.addView(advancedDetails, adp);
        advancedButton.setOnClickListener(v -> {
            advancedVisible = !advancedVisible;
            advancedDetails.setVisibility(advancedVisible ? View.VISIBLE : View.GONE);
            advancedButton.setText(advancedVisible ? "Ocultar configurações avançadas" : "Configurações avançadas");
        });

        TextView peersTitle = label("Dispositivos encontrados", 18, primaryText, true);
        LinearLayout.LayoutParams ptp = lp(-1, -2); ptp.topMargin = dp(26); page.addView(peersTitle, ptp);
        peerList = new LinearLayout(this);
        peerList.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams plp = lp(-1, -2); plp.topMargin = dp(10); page.addView(peerList, plp);
        peerList.addView(label("Nenhum dispositivo encontrado ainda.", 13, secondaryText, false));

        TextView note = label("Estado: descoberta LAN experimental. Wi-Fi Direct, Mesh, gateways e transferência segura ainda não estão implementados. A descoberta não autentica dispositivos; não envie dados privados.", 12, secondaryText, false);
        LinearLayout.LayoutParams np = lp(-1, -2); np.topMargin = dp(24); page.addView(note, np);

        TextView links = label("Projeto experimental · Descoberta LAN", 13, blue, true);
        LinearLayout.LayoutParams lnp = lp(-1, -2); lnp.topMargin = dp(16); page.addView(links, lnp);
        setContentView(scroll);

        if ("erikraftwifi".equals(getIntent().getScheme())) {
            Toast.makeText(this, "ErikrafT Wi-Fi aberto pelo link de integração.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSettingsDialog() {
        String[] options = {"Diagnóstico do dispositivo", "Idioma do aplicativo", "Configurações do Android"};
        new AlertDialog.Builder(this).setTitle("Configurações")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) showDiagnostics();
                    else if (which == 1) showLanguageSettings();
                    else startActivity(new Intent(Settings.ACTION_SETTINGS));
                })
                .setNegativeButton("Fechar", null)
                .show();
    }

    private void showDiagnostics() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities caps = cm == null ? null : cm.getNetworkCapabilities(cm.getActiveNetwork());
        String network = caps == null ? "Sem rede ativa detectada"
                : caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ? "Wi-Fi conectado"
                : caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ? "Ethernet conectada"
                : "Outra rede ativa";
        String report = "ErikrafT Wi-Fi — Diagnóstico\n\n"
                + "Android: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")\n"
                + "Dispositivo: " + Build.MANUFACTURER + " " + Build.MODEL + "\n"
                + "Rede: " + network + "\n"
                + "Descoberta LAN: experimental (NSD/mDNS)\n"
                + "Autenticação de pares: não implementada\n"
                + "Transporte criptografado: não implementado\n"
                + "Wi-Fi Direct / mesh / gateway: não implementados\n"
                + "Transferência de arquivos: não implementada";
        new AlertDialog.Builder(this).setTitle("Diagnóstico").setMessage(report)
                .setPositiveButton("OK", null)
                .setNeutralButton("Compartilhar", (d, w) -> {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, report);
                    startActivity(Intent.createChooser(share, "Compartilhar diagnóstico"));
                }).show();
    }

    private void showLanguageSettings() {
        if (Build.VERSION.SDK_INT >= 33) {
            try {
                Intent intent = new Intent("android.settings.APP_LOCALE_SETTINGS");
                intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return;
            } catch (Exception ignored) {
                // Fall through to system locale settings on OEM builds.
            }
        }
        Toast.makeText(this,
                "A seleção de idioma do sistema está disponível; as traduções completas do app ainda precisam ser adicionadas.",
                Toast.LENGTH_LONG).show();
        try {
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        } catch (Exception ignored) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
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
        row.setBackgroundColor(surface);
        LinearLayout.LayoutParams rp = lp(-1, -2); rp.bottomMargin = dp(8); peerList.addView(row, rp);
        row.addView(label(name, 14, primaryText, true));
        TextView endpoint = label(address + ":" + port, 12, secondaryText, false);
        LinearLayout.LayoutParams ep = lp(-1, -2); ep.topMargin = dp(4); row.addView(endpoint, ep);
        row.addView(label("Encontrado na rede local · transferência indisponível", 11, secondaryText, false));
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