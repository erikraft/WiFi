package com.erikraft.wifi;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

/** Quick Settings shortcut that opens ErikrafT Wi-Fi; it does not silently change system Wi-Fi settings. */
public final class WifiQuickSettingsTileService extends TileService {
    @Override public void onStartListening() {
        super.onStartListening();
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setLabel("ErikrafT Wi-Fi");
            tile.setContentDescription("Abrir o ErikrafT Wi-Fi");
            tile.setIcon(Icon.createWithResource(this, R.drawable.erikraft_wifi_qs_icon));
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }

    @Override public void onClick() {
        super.onClick();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityAndCollapse(intent);
    }
}
