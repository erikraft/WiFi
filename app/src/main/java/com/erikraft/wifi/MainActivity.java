package com.erikraft.wifi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** UI shell only; discovery and secure transport are not enabled. */
public final class MainActivity extends Activity {
    private final int blue = Color.rgb(23, 105, 255);
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(12, 39, 91));
        getWindow().setNavigationBarColor(Color.rgb(12, 39, 91));
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true);
        LinearLayout page = new LinearLayout(this); page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(24), dp(28), dp(24), dp(28)); page.setBackgroundColor(Color.rgb(246, 249, 255)); scroll.addView(page);
        page.addView(label("ErikrafT Wi-Fi™", 18, blue, true));
        TextView title = label("Seus dispositivos.\nSua rede.", 34, Color.rgb(16, 26, 43), true);
        LinearLayout.LayoutParams tp = lp(-1, -2); tp.topMargin = dp(42); page.addView(title, tp);
        TextView sub = label("Uma base aberta para conectividade local e descoberta de dispositivos próximos.", 16, Color.rgb(91, 105, 128), false);
        LinearLayout.LayoutParams sp = lp(-1, -2); sp.topMargin = dp(14); page.addView(sub, sp);
        LinearLayout card = new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(dp(18), dp(20), dp(18), dp(20)); card.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams cp = lp(-1, -2); cp.topMargin = dp(30); page.addView(card, cp);
        card.addView(label("ESTADO DO PROJETO", 11, blue, true));
        TextView status = label("Interface inicial pronta\nDescoberta e transporte: não ativados", 15, Color.rgb(43, 57, 78), false);
        LinearLayout.LayoutParams stp = lp(-1, -2); stp.topMargin = dp(12); card.addView(status, stp);
        Button info = new Button(this); info.setText("Sobre a integração com Drop"); info.setAllCaps(false); info.setTextColor(Color.WHITE); info.setBackgroundTintList(android.content.res.ColorStateList.valueOf(blue));
        LinearLayout.LayoutParams bp = lp(-1, -2); bp.topMargin = dp(24); page.addView(info, bp);
        info.setOnClickListener(v -> android.widget.Toast.makeText(this, "A integração funcional está no roadmap; nenhum transporte seguro está ativo.", android.widget.Toast.LENGTH_LONG).show());
        TextView note = label("Wi-Fi Direct, Mesh e transferência segura ainda estão no roadmap. Não use este protótipo para dados sensíveis.", 12, Color.rgb(112, 126, 147), false);
        LinearLayout.LayoutParams np = lp(-1, -2); np.topMargin = dp(22); page.addView(note, np);
        setContentView(scroll);
    }
    private TextView label(String s, int size, int color, boolean bold) { TextView v = new TextView(this); v.setText(s); v.setTextSize(size); v.setTextColor(color); if (bold) v.setTypeface(Typeface.DEFAULT, Typeface.BOLD); v.setGravity(Gravity.START); return v; }
    private LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w, h); }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
}