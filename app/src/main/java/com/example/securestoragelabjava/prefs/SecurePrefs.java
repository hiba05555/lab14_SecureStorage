package com.example.securestoragelabjava.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class SecurePrefs {

    private static final String HC_SECURE_PREFS = "hc_secure_prefs";
    private static final String HC_KEY_TOKEN = "hc_api_token";

    private SecurePrefs() {}

    private static SharedPreferences getSecurePrefs(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        return EncryptedSharedPreferences.create(
                context,
                HC_SECURE_PREFS,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void saveToken(Context context, String token) throws Exception {
        getSecurePrefs(context).edit().putString(HC_KEY_TOKEN, token).apply();
    }

    public static String loadToken(Context context) throws Exception {
        return getSecurePrefs(context).getString(HC_KEY_TOKEN, "");
    }

    public static void clear(Context context) throws Exception {
        getSecurePrefs(context).edit().clear().apply();
    }
}
