# Desktop clients: Windows, Linux, and macOS

## Purpose and current status

The `desktop/` directory contains the initial Tauri 2 + Rust desktop client. It uses the same primary blue as the Android app, `#4285F4`, and targets Windows, Linux, and macOS. The first implementation mirrors the existing LAN-discovery contract; it is not a stable release.

Implemented in this first slice:
- A responsive, keyboard-accessible desktop window with English and Brazilian Portuguese labels selected from the operating-system locale.
- A TCP metadata endpoint that matches Android protocol version 1.
- mDNS/DNS-SD publication and discovery using `_erikraftwifi._tcp.local.`.
- Validation of the public metadata response before a peer is displayed.
- A CI matrix that attempts to produce bundles on Windows, Linux, and macOS.

Not implemented:
- File transfer, authenticated peer identities, encrypted sessions, Wi-Fi Direct, mesh routing, or Internet gateway forwarding.
- Signed Windows installers, macOS notarization, automatic updates, or release-channel management.
- Guaranteed support for every network interface, VPN, multicast policy, or firewall configuration.

The build matrix and real-device testing must pass before calling the desktop app release-ready.

## Architecture

- **Frontend:** static HTML/CSS/JavaScript, packaged locally by Tauri. Its visual tokens reuse the Android primary blue `#4285F4`; they are not a separate product palette.
- **Native runtime:** Rust via Tauri 2, for Windows, Linux, and macOS.
- **Discovery:** the pinned `mdns-sd` crate implements mDNS/DNS-SD. It advertises and browses the same service type as the Android prototype.
- **Metadata:** a small TCP JSON line with `service`, `protocol`, `capabilities`, and `fileTransfer`. The endpoint contains public metadata only and is deliberately not treated as a trusted channel.
- **No cloud dependency for discovery:** the native discovery flow runs on the local network. The website and external documentation links are not part of the discovery data path.

## Local development

Requirements:
- Node.js 22 or later.
- A stable Rust toolchain.
- Tauri platform dependencies: WebView2 on Windows; WebKitGTK development packages on Linux; and the Apple build tools/WebKit support on macOS.

From `desktop/`:

```bash
npm install
npm run tauri -- dev
```

Build a local release bundle:

```bash
npm run tauri -- build
```

The build may need operating-system package prerequisites listed in the official [Tauri prerequisites guide](https://v2.tauri.app/start/prerequisites/).

## Cross-platform validation matrix

| Area | Windows | Linux | macOS |
|---|---|---|---|
| CI build | Required | Required | Required |
| mDNS on the active interface | Physical LAN test | Physical LAN test | Physical LAN test |
| Firewall / permission prompts | Verify Windows Firewall behavior | Verify distro/firewall differences | Verify local-network privacy prompts and firewall behavior |
| Interface changes / sleep / resume | Test | Test | Test |
| Signing and release packaging | Code-signing plan required | Package formats and distro tests required | Signing and notarization required before public distribution |
| Accessibility / scaling | High-DPI and keyboard test | Fractional scaling and keyboard test | Retina scaling and keyboard test |

A successful compile is not proof that multicast discovery works on every network. Enterprise Wi-Fi, guest networks, VPNs, client isolation, and firewall rules may block mDNS or peer TCP connections.

## UI and accessibility

Use the Android application's primary blue `#4285F4` for shared identity, visible focus, and primary actions. Layouts must adapt to small windows as well as large screens; do not require the user to keep a fixed desktop width. Support keyboard navigation, legible contrast, reduced motion, operating-system scaling, and text that can wrap without clipping.

## Security gates before file transfer

Before any transfer feature is added:
1. Define a versioned and reviewed protocol contract.
2. Establish authenticated peer identity and explicit consent for each transfer.
3. Use an encrypted session with integrity protection and replay defenses.
4. Set limits for file size, timeouts, concurrent requests, and cancellation.
5. Keep paths and user data out of discovery metadata and logs.
6. Test hostile peers, malformed metadata, disconnects, and resource exhaustion.
7. Reuse Drop's existing transfer engine after reviewing its current contracts instead of creating a second file-transfer stack.

## Official technical references

- [Tauri 2 prerequisites](https://v2.tauri.app/start/prerequisites/)
- [Tauri security capabilities](https://v2.tauri.app/security/capabilities/)
- [mDNS-SD Rust crate](https://docs.rs/mdns-sd/0.21.5/mdns_sd/)
- [Android Network Service Discovery](https://developer.android.com/develop/connectivity/wifi/use-nsd)

## Release rule

The desktop prototype may be called **experimental** until the Windows/Linux/macOS build matrix succeeds and real-device testing confirms discovery, stopping, interface changes, and safe failure behavior. Never advertise file transfer or secure connectivity until those features are implemented and reviewed.
