# Lab 15 — Secure Storage Lab Java

## Présentation

Application Android de persistance locale sécurisée utilisant SharedPreferences, EncryptedSharedPreferences, fichiers internes, cache et stockage externe app-specific.

---

## Ce qui a été réalisé

## Structure du projet

| Package | Fichier |
|---------|---------|
| model | Student.java |
| prefs | AppPrefs.java |
| prefs | SecurePrefs.java |
| files | InternalTextStore.java |
| files | StudentsJsonStore.java |
| cache | CacheStore.java |
| external | ExternalAppFilesStore.java |
| ui | MainActivity.java |

### Fonctionnalités
| Fonctionnalité | Description |
|---|---|
| SharedPreferences | Sauvegarde nom, langue, thème |
| EncryptedSharedPreferences | Stockage chiffré du token (AES256) |
| Fichiers internes | Texte UTF-8 + JSON étudiant |
| Cache | Stockage temporaire purgeable |
| Stockage externe | Export app-specific |

### Personnalisation HC
- `LOG_TAG` : `HC_SecureStorage`
- Préfixe `hc_` sur tous les fichiers et clés
- Interface moderne avec palette violette/cyan

---

## Démonstration

### Vidéo
📦 Fichier demo disponible : [demo_lab15.zip](demo_lab15.zip)
---

## Technologies utilisées
- Android Studio
- Java
- SharedPreferences
- EncryptedSharedPreferences (AES256-GCM)
- JSON (org.json)
- MasterKey (Android Keystore)
