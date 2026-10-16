package com.erikraft.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Integrates with Android's user-controlled hotspot/tethering settings.
 * An ordinary app cannot enable system tethering or install IP routes itself.
 */
final class GatewayManager {
    private static final String PREFS = "gateway_settings";
    private static final String KEY_ADVERTISE = "gateway_advertisement_enabled";

    interface Listener {
        void onAdvertisementChanged(boolean enabled);
    }

    private GatewayManager() { }

    static boolean hasValidatedInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return false;
        Network active = manager.getActiveNetwork();
        if (active == null) return false;
        NetworkCapabilities capabilities = manager.getNetworkCapabilities(active);
        return capabilities != null
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    static boolean isGatewayAdvertisementEnabled(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(KEY_ADVERTISE, false);
    }

    static String getStatusText(Context context) {
        return AppStrings.t(context, hasValidatedInternet(context)
                ? "Internet validada neste dispositivo. Confirme que o Hotspot/Ancoragem do Android está ativo."
                : "Sem Internet validada neste dispositivo. Um gateway não cria Internet sem uma conexão externa.");
    }

    static void showSetupDialog(Activity activity, Listener listener) {
        if (isGatewayAdvertisementEnabled(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle(AppStrings.t(activity, "Configurar gateway de Internet"))
                    .setMessage(AppStrings.t(activity, "Este anúncio é apenas uma indicação local e não prova identidade nem disponibilidade contínua. A conexão é direta pelo hotspot nativo; mesh multi-hop do app ainda não está implementada."))
                    .setPositiveButton(AppStrings.t(activity, "Parar anúncio de gateway"), (dialog, which) -> {
                        activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                                .putBoolean(KEY_ADVERTISE, false).apply();
                        if (listener != null) listener.onAdvertisementChanged(false);
                        Toast.makeText(activity, AppStrings.t(activity, "Anúncio do gateway desativado."), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(AppStrings.t(activity, "Cancelar"), null)
                    .show();
            return;
        }

        String upstreamMessage = AppStrings.t(activity, hasValidatedInternet(activity)
                ? "Este dispositivo tem acesso validado à Internet. Abra as configurações do Android, ative o Hotspot/Ancoragem e confirme que o compartilhamento está funcionando."
                : "Este dispositivo não tem acesso validado à Internet. Conecte-o à Internet antes de oferecê-lo como gateway.");
        String message = upstreamMessage + "\n\n"
                + AppStrings.t(activity, "O celular cliente deve conectar-se ao Wi-Fi do hotspot e usar a senha definida no Android. Ele não precisa de dados móveis próprios.")
                + "\n\n"
                + AppStrings.t(activity, "Este anúncio é apenas uma indicação local e não prova identidade nem disponibilidade contínua. A conexão é direta pelo hotspot nativo; mesh multi-hop do app ainda não está implementada.");
        new AlertDialog.Builder(activity)
                .setTitle(AppStrings.t(activity, "Configurar gateway de Internet"))
                .setMessage(message)
                .setPositiveButton(AppStrings.t(activity, "Abrir configurações do hotspot"), (dialog, which) -> openHotspotSettings(activity))
                .setNeutralButton(AppStrings.t(activity, "Já ativei o hotspot — anunciar"), (dialog, which) -> {
                    if (!hasValidatedInternet(activity)) {
                        new AlertDialog.Builder(activity)
                                .setTitle(AppStrings.t(activity, "Configurar gateway de Internet"))
                                .setMessage(AppStrings.t(activity, "Não é possível anunciar: conecte este dispositivo à Internet primeiro."))
                                .setPositiveButton(AppStrings.t(activity, "OK"), null)
                                .show();
                        return;
                    }
                    activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                            .putBoolean(KEY_ADVERTISE, true).apply();
                    if (listener != null) listener.onAdvertisementChanged(true);
                    Toast.makeText(activity, AppStrings.t(activity, "Já ativei o hotspot — anunciar"), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(AppStrings.t(activity, "Cancelar"), null)
                .show();
    }

    private static void openHotspotSettings(Activity activity) {
        try {
            activity.startActivity(new Intent(Settings.ACTION_TETHER_SETTINGS));
        } catch (Exception first) {
            try {
                activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            } catch (Exception second) {
                try {
                    activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                } catch (Exception third) {
                    Toast.makeText(activity, AppStrings.t(activity, "Não foi possível abrir as configurações do Android."), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
