# ErikrafT Wi-Fi™ Desktop

This directory starts the cross-platform desktop client for Windows, Linux, and macOS using Tauri 2 and Rust. Its visual system uses the Android app's primary blue, `#4285F4`, and its first target is the existing LAN-discovery protocol.

## Current scope

- Advertises a local service of type `_erikraftwifi._tcp.local.`.
- Serves the same public metadata JSON contract as the Android prototype (protocol 1).
- Browses the LAN for compatible services and checks the TCP metadata before showing a peer.
- Uses native Rust networking rather than a remote web service for local discovery.
- Does not transfer files, authenticate peers, encrypt the metadata endpoint, implement Wi-Fi Direct or mesh routing, or provide an Internet gateway.

This is an early implementation, not a release-ready product. Cross-platform builds, mDNS behavior, network route selection, firewall prompts, and peer discovery must be verified in the GitHub Actions matrix and on real Windows, Linux, and macOS devices before public release.

## Requirements

- Node.js 22 or later.
- Rust stable toolchain.
- Tauri's platform prerequisites: WebView2 on Windows; WebKitGTK development libraries on Linux; Xcode Command Line Tools and WebKit support on macOS.

## Build locally

From this directory:

```bash
npm install
npm run tauri -- dev
```

Create release bundles:

```bash
npm run tauri -- build
```

See [desktop client documentation](../docs/desktop-clients.md) and the global [security guide](../docs/security.md).
