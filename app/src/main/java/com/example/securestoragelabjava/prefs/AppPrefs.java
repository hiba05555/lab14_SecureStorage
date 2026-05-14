package com.example.securestoragelabjava.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppPrefs {

    private static final String HC_PREFS_NAME = "hc_app_prefs";
    private static final String HC_KEY_NAME = "hc_pref_name";
    private static final String HC_KEY_LANG = "hc_pref_lang";
    private static final String HC_KEY_THEME = "hc_pref_theme";

    private AppPrefs() {}

    public static boolean save(Context context, String name, String lang, String theme, boolean sync) {
        SharedPreferences prefs = context.getSharedPreferences(HC_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit()
                .putString(HC_KEY_NAME, name)
                .putString(HC_KEY_LANG, lang)
                .putString(HC_KEY_THEME, theme);
        if (sync) return editor.commit();
        editor.apply();
        return true;
    }

    public static HCTriple load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(HC_PREFS_NAME, Context.MODE_PRIVATE);
        return new HCTriple(
                prefs.getString(HC_KEY_NAME, ""),
                prefs.getString(HC_KEY_LANG, "fr"),
                prefs.getString(HC_KEY_THEME, "light")
        );
    }

    public static void clear(Context context) {
        context.getSharedPreferences(HC_PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static final class HCTriple {
        public final String name;
        public final String lang;
        public final String theme;

        public HCTriple(String name, String lang, String theme) {
            this.name = name;
            this.lang = lang;
            this.theme = theme;
        }
    }
}