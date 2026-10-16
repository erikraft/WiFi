<p align="center">
  <img src="assets/ErikrafT_Wifi_Icon_Logo.svg" alt="ErikrafT Wi-Fi™ logo" width="120">
  <h1 align="center"><a href="https://wifi.erikraft.com/">ErikrafT Wi-Fi™</a></h1>
</p>

<p align="center"><a href="README.md">English</a> · <a href="README_PT-BR.md">Português (Brasil)</a></p>

An open-source project exploring local connectivity using existing Wi-Fi technologies. The experimental Android prototype advertises its own node and discovers other ErikrafT Wi-Fi nodes on the same local network using Android Network Service Discovery (DNS-SD/mDNS).

> **Prototype status:** LAN discovery is experimental and the TCP endpoint returns public metadata only, without file transfer or peer authentication. The app now helps the user configure Android's native hotspot/tethering for direct Internet sharing, with optional local gateway advertisement. App-level multi-hop IP routing, secure transport, and functional ErikrafT Drop™ integration are still not implemented.

## Connectivity goal

The long-term goal is to let compatible devices form or use a Wi-Fi network and reach Internet services through an available route, including an authorized gateway or intermediate nodes once mesh routing is implemented. The project prioritizes compatibility with existing APIs and hardware rather than relying on proprietary radio technology.

**Important limitation:** Wi-Fi creates a network link, not automatic Internet access. Public websites require a working external route, such as a router, shared connection, or Internet-connected gateway. Without that route, offline mode may support local communication and services, but it cannot access the public Web arbitrarily. Direct sharing uses Android's native hotspot and requires a real upstream connection on the gateway device. The app does not yet route IP traffic through a multi-hop mesh. See [Android Internet gateway](docs/internet-gateway.md) and [platform compatibility and limitations](docs/platform-compatibility.md).

## Components

- **Android app:** native Java app with a blue interface and experimental LAN discovery using NSD/mDNS.
- **Visual identity:** color and white SVG logos in `assets/`. The Android app uses the white icon on blue `#4285F4`, consistent with ErikrafT Drop™ for Android.
- **Landing page:** responsive blue website with project status, bilingual documentation, and Bitcoin payment QR codes.
- **Desktop clients:** an initial Tauri + Rust prototype in `desktop/` targets Windows, Linux, and macOS. It is experimental and requires CI plus real-device validation; it is not a stable release.
- **Planned integration:** the `erikraftwifi://open` deep link is an exploratory entry point; it does not mean ErikrafT Drop™ integration already works.
- **Builds:** the validation workflow compiles a debug APK. The release workflow creates APK/AAB artifacts and can sign them using a Base64 keystore supplied through GitHub Actions secrets.

## Android interface, Quick Settings, and PiP

- The interface provides an advanced-settings entry to inspect actual status and pending capabilities without presenting them as available features.
- Users can enable Picture-in-Picture (PiP) when leaving the app on Android 8.0 or later; availability depends on device support and system settings.
- The Quick Settings service adds a shortcut to open ErikrafT Wi-Fi. It uses an Android VectorDrawable based on the white symbol in `assets/ErikrafT_Wifi_Icon_Logo_White.svg`, sized to 24 dp; the original SVG is not modified. The shortcut does not turn Wi-Fi on or off and does not create a connection by itself.
- The app remains a prototype. Advanced settings do not configure mesh networking or gateways because those capabilities do not yet exist in the networking core.

## Android offline web library and geographic globe

The Android app includes an experimental offline web library:
- Open a website URL and save a WebView MHTML archive for later local reading.
- Reopen saved archives, import HTML/MHTML files through Android's document picker, and export an archive to a user-selected location.
- Share a saved HTML/MHTML archive directly with compatible Android apps through a read-only, per-file content URI grant.
- Accept shared text/URL or a shared file from compatible Android apps, retaining `.html` / `.mhtml` extensions for shared archive files.
- Open the interactive geographic globe with simplified latitude/longitude coast outlines that rotate with the sphere. It receives real peers discovered by Android NSD/mDNS on the same local network, updates when peers appear/disappear, and shows their LAN address/port when selected. Peer markers use illustrative visual placements only—not GPS coordinates or actual geographic locations. The globe does not implement mesh routing.
- The Android home screen, offline-library controls, diagnostics and discovery statuses support an in-app language picker with 16 offline language variants; the globe controls also include matching translations.

**Offline archive limitations:** the archive contains only resources WebView successfully captures. Login-only content, DRM, dynamic pages, some cross-origin resources, and pages requiring a live backend may not work offline. Chrome's private “downloaded page” store is sandboxed by Android; the app cannot silently read Chrome's private data. Export/share an actual HTML/MHTML file or select a downloaded file that Android exposes in the document picker. A standalone HTML file whose companion asset folder is missing may render incompletely.

**Mesh and calls status:** the globe is now connected to real NSD/mDNS peer discovery on the same LAN, but it is not a geographic map and does not establish a multi-hop mesh. The existing network core remains experimental LAN discovery. It does not yet provide offline multi-hop chat, audio/video calls, WebRTC signaling, Bluetooth mesh routing, or Wi-Fi Aware transport. Calls with no Internet are technically possible on compatible local peer links, but require a real local signaling and media transport implementation, platform permission handling, and multi-device testing; this release does not claim that capability.

## Build and distribution

Open `app/` in Android Studio or follow [the build and signing guide](docs/builds.md). Never add keystores, passwords, or private keys to Git.

The Android app remains experimental. An initial desktop prototype in `desktop/` targets Windows, Linux, and macOS with the same `#4285F4` blue identity, but must pass platform builds and real-device tests before being treated as release-ready. See [desktop clients](docs/desktop-clients.md) and [platform compatibility](docs/platform-compatibility.md).

## Documentation

- [Architecture](docs/architecture.md) · [Português](docs/architecture_PT-BR.md)
- [LAN discovery](docs/lan-discovery.md) · [Português](docs/lan-discovery_PT-BR.md)
- [Android Internet gateway](docs/internet-gateway.md) · [Português (Brasil)](docs/internet-gateway_PT-BR.md)
- [Platform compatibility and regulatory limitations](docs/platform-compatibility.md) · [Português](docs/platform-compatibility_PT-BR.md)
- [Builds and signing](docs/builds.md) · [Português](docs/builds_PT-BR.md)
- [Security](docs/security.md) · [Português](docs/security_PT-BR.md)
- [ErikrafT Drop™ integration](docs/drop-integration.md) · [Português](docs/drop-integration_PT-BR.md)
- [Desktop clients](docs/desktop-clients.md) · [Português](docs/desktop-clients_PT-BR.md)
- [International regulatory overview](docs/international-regulatory-overview.md) · [Português](docs/international-regulatory-overview_PT-BR.md)
- [ErikrafT Drop™ integration plan](docs/drop-integration-roadmap.md) · [Português](docs/drop-integration-roadmap_PT-BR.md)
- [Responsive website experience](docs/web-experience.md) · [Português](docs/web-experience_PT-BR.md)
- [AI-readable project guide](llms.txt) · [Português](llms_PT-BR.txt)

## Related repositories

- [ErikrafT Drop™ Web/Desktop](https://github.com/erikraft/Drop)
- [ErikrafT Drop™ for Android](https://github.com/erikraft/Drop-Android)

## Security and compatibility

Discovery is not authentication. The current endpoint does not provide an authenticated or encrypted channel. Do not transfer private files until authentication, integrity protection, and end-to-end encryption have been implemented and tested. Compatibility varies by Android version, manufacturer, permissions, and hardware; universal support must not be assumed.
