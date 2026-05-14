package com.example.securestoragelabjava.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securestoragelabjava.R;
import com.example.securestoragelabjava.cache.CacheStore;
import com.example.securestoragelabjava.files.InternalTextStore;
import com.example.securestoragelabjava.files.StudentsJsonStore;
import com.example.securestoragelabjava.model.Student;
import com.example.securestoragelabjava.prefs.AppPrefs;
import com.example.securestoragelabjava.prefs.SecurePrefs;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String HC_TAG = "HC_SecureStorage";
    private final List<String> hcLangs = Arrays.asList("fr", "en", "ar");

    private TextInputEditText etName, etToken;
    private Spinner spLang;
    private Switch swDark;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etToken = findViewById(R.id.etToken);
        spLang = findViewById(R.id.spLang);
        swDark = findViewById(R.id.swDark);
        tvResult = findViewById(R.id.tvResult);

        spLang.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, hcLangs));

        findViewById(R.id.btnSavePrefs).setOnClickListener(v -> hcSavePrefs());
        findViewById(R.id.btnLoadPrefs).setOnClickListener(v -> hcLoadPrefs());
        findViewById(R.id.btnSaveJson).setOnClickListener(v -> hcSaveJson());
        findViewById(R.id.btnLoadJson).setOnClickListener(v -> hcLoadJson());
        findViewById(R.id.btnClear).setOnClickListener(v -> hcClearAll());

        hcLoadPrefs();
    }

    private void hcSavePrefs() {
        String name = etName.getText().toString().trim();
        String lang = hcLangs.get(Math.max(0, spLang.getSelectedItemPosition()));
        String theme = swDark.isChecked() ? "dark" : "light";

        boolean ok = AppPrefs.save(this, name, lang, theme, false);

        String token = etToken.getText().toString();
        if (!token.isBlank()) {
            try {
                SecurePrefs.saveToken(this, token);
            } catch (Exception e) {
                tvResult.setText("❌ Erreur chiffrement : " + e.getMessage());
                return;
            }
        }

        Log.d(HC_TAG, "Prefs sauvegardées ok=" + ok + " name=" + name + " lang=" + lang);

        try {
            CacheStore.write(this, "hc_last_ui.txt", "name=" + name + " lang=" + lang);
        } catch (Exception ignored) {}

        tvResult.setText("✅ Sauvegarde terminée\n" +
                "👤 Nom : " + name + "\n" +
                "🌐 Langue : " + lang + "\n" +
                "🎨 Thème : " + theme + "\n" +
                "🔐 Token : stocké chiffré");
    }

    private void hcLoadPrefs() {
        AppPrefs.HCTriple triple = AppPrefs.load(this);
        etName.setText(triple.name);
        swDark.setChecked("dark".equals(triple.theme));
        int idx = hcLangs.indexOf(triple.lang);
        spLang.setSelection(idx >= 0 ? idx : 0);

        int tokenLen = 0;
        try {
            String t = SecurePrefs.loadToken(this);
            tokenLen = t == null ? 0 : t.length();
        } catch (Exception ignored) {}

        tvResult.setText("📂 Préférences chargées\n" +
                "👤 Nom : " + triple.name + "\n" +
                "🌐 Langue : " + triple.lang + "\n" +
                "🎨 Thème : " + triple.theme + "\n" +
                "🔐 Token longueur : " + tokenLen);
    }

    private void hcSaveJson() {
        List<Student> students = Arrays.asList(
                new Student(1, "Hiba", 22),
                new Student(2, "Amina", 20),
                new Student(3, "Omar", 21)
        );
        try {
            StudentsJsonStore.save(this, students);
            InternalTextStore.writeUtf8(this, "hc_note.txt", "JSON sauvegardé avec succès.");
            tvResult.setText("✅ JSON sauvegardé\n📊 Étudiants : " + students.size());
        } catch (Exception e) {
            tvResult.setText("❌ Erreur JSON : " + e.getMessage());
        }
    }

    private void hcLoadJson() {
        List<Student> students = StudentsJsonStore.load(this);
        String note;
        try {
            note = InternalTextStore.readUtf8(this, "hc_note.txt");
        } catch (Exception e) {
            note = "(fichier absent)";
        }

        StringBuilder sb = new StringBuilder("📋 JSON chargé\n");
        sb.append("📝 Note : ").append(note).append("\n");
        sb.append("👥 Étudiants : ").append(students.size()).append("\n");
        for (Student s : students) {
            sb.append("  • ").append(s.name).append(" (").append(s.age).append(" ans)\n");
        }
        tvResult.setText(sb.toString());
    }

    private void hcClearAll() {
        AppPrefs.clear(this);
        try { SecurePrefs.clear(this); } catch (Exception ignored) {}
        StudentsJsonStore.delete(this);
        InternalTextStore.delete(this, "hc_note.txt");
        int purged = CacheStore.purge(this);

        etName.setText("");
        etToken.setText("");
        swDark.setChecked(false);
        spLang.setSelection(0);

        tvResult.setText("🗑️ Nettoyage terminé\n" +
                "✅ Prefs effacées\n" +
                "✅ Token sécurisé effacé\n" +
                "✅ Fichiers supprimés\n" +
                "✅ Cache purgé : " + purged + " fichier(s)");
    }
}