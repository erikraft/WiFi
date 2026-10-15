# Builds and signing

## Android: APK and AAB
The `.github/workflows/android-release.yml` workflow builds an APK and Android App Bundle (AAB) when manually triggered (**Actions → Android APK and AAB → Run workflow**) or when a `v*` tag is published. It uses Java 17, Android SDK 36, and Gradle 8.13.

### Signing releases with a Base64 keystore
Never commit a private key, place it in source code, or expose it in logs. Under **Settings → Secrets and variables → Actions**, configure these repository secrets:

- `ANDROID_KEYSTORE_BASE64`: Base64 contents of the JKS/PKCS12 keystore, on one line.
- `ANDROID_KEY_ALIAS`: key alias inside the keystore.
- `ANDROID_KEYSTORE_PASSWORD`: keystore password.
- `ANDROID_KEY_PASSWORD`: key password.

Encode the file locally:
```bash
base64 -w 0 erikraft-wifi-release.jks
```
On macOS, if `base64` does not support `-w 0`, use:
```bash
base64 < erikraft-wifi-release.jks | tr -d '\\n'
```

Copy the output into `ANDROID_KEYSTORE_BASE64`. Never paste the keystore, passwords, or private key into issues, pull requests, YAML files, or chat. Keep an offline backup: losing the signing key may prevent compatible updates to the published app.

When all four secrets are configured, the workflow signs and verifies the APK and signs/verifies the AAB. If they are missing, it still creates **unsigned** release artifacts with an explicit warning; do not distribute these as a final release. Fork pull requests do not receive repository secrets.

Signing identifies the key used to sign a package; it does not prove that the app has been tested on every device. For Google Play, also follow Play App Signing.

References:
- Android App Signing: https://developer.android.com/studio/publish/app-signing
- GitHub Actions secrets: https://docs.github.com/en/actions/security-for-github-actions/security-guides/using-secrets-in-github-actions

## Windows, Linux, and macOS
The repository contains a **native Java Android app** and a static landing page. It has no desktop client, Electron/Tauri/Qt project, or desktop discovery/connection logic. Claiming that a workflow produces a functional `.exe`, AppImage/`.deb`, or `.dmg` would be misleading.

A real desktop release first requires choosing and implementing a desktop client that reuses a common, secure protocol. Separate builds can then be added on `windows-latest`, `ubuntu-latest`, and `macos-latest` with appropriate tests and packaging. Until then, this repository produces Android artifacts, not desktop executables.

## Build limitations
- Automated pull-request builds compile the debug APK; they do not replace physical-device testing.
- The release workflow does not prove mDNS discovery, Wi-Fi Direct, mesh networking, Internet access, or Drop integration.
- Do not publish experimental builds as stable without testing installation, signing, permissions, and behavior on real devices.
