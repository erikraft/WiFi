# Initial architecture

## Implemented in this stage
- Responsive static landing page with a blue visual identity.
- Native Android app with an initial interface and the `erikraftwifi://open` deep link.
- Experimental discovery of nodes on the same local network using Android NSD/DNS-SD (mDNS).
- Ephemeral TCP endpoint that returns public protocol and capability metadata only; it does not receive or send files.
- CI workflow that attempts to compile a debug APK, plus a manual/tag-triggered workflow for Android APK/AAB artifacts.

## Android interface and shortcuts
- **Main screen:** keeps LAN discovery as the primary action and uses direct wording.
- **Advanced settings:** explains current capabilities and pending networking features. It does not expose pretend controls for unimplemented mesh or gateway functionality.
- **Quick Settings:** `WifiQuickSettingsTileService` provides a shortcut to open the app. The white symbol is reproduced as an Android VectorDrawable at 24 dp without modifying the original SVG. The shortcut does not toggle Wi-Fi.
- **PiP:** optional and disabled by default; the preference is persisted. On Android 8.0+, the app requests Picture-in-Picture when the user leaves it. Device manufacturers and system policies may restrict this behavior.

## Offline pages and app localization

- Save a rendered page from Android WebView as an MHTML archive for local reading.
- Import HTML/MHTML files exposed by Android's document picker and export saved archives.
- Share a saved archive through a read-only `ContentProvider` URI grant; receivers get read-only access to that single file.
- The Android app language picker provides 16 offline translations for the native screen, diagnostics, archive controls, and discovery status. The interactive globe uses the same selected locale.
- Sharing a URL is not equivalent to sharing Chrome's privately stored downloaded page. The app cannot access Chrome's private storage; offline reading requires an actual HTML/MHTML file that Android exposes.

## Not implemented yet
- Wi-Fi Direct / Wi-Fi P2P.
- Mesh routing, forwarding between nodes, and Internet gateways.
- Peer authentication, encrypted channels, secure LAN/mesh messaging, media transfer, and real-time audio/video calls.
- Functional integration with ErikrafT Drop™ for Web/Desktop and ErikrafT Drop™ for Android. The deep link is only an entry point.
- Executable desktop clients for Windows, Linux, and macOS.
- iOS support or the ability to operate as a cellular-network base station.

## Compatibility and limitations
The app may run on devices that also support 2G/3G/4G/5G, but this does not make it a provider of those cellular networks. Internet traffic needs a route to a connected gateway. Without a gateway, nodes can provide local connectivity only when the relevant transport is implemented.

The implementation should use official operating-system APIs and existing radios, without changing transmission power, frequencies, or firmware. For Brazilian regulatory context, see [platform compatibility](platform-compatibility.md).

## Long-term connectivity goal
The goal is to let compatible devices reach the Internet through existing network interfaces and authorized routes, with future support for direct device links, multi-hop forwarding, and gateways where technically feasible. The client should detect actual per-device capabilities and fall back to local networking when no external route is available.

No software technique can guarantee access to public websites without a network path to the Internet. Wi-Fi Direct can create a local link; mesh networking can forward traffic between nodes if routing works; only a gateway with upstream connectivity provides Internet access. DNS, NAT, IPv4/IPv6, firewalls, captive portals, TLS, provider policies, and operator consent must be considered and tested.

## Technical direction
1. Use existing network APIs; do not invent a radio protocol.
2. Separate discovery, node identity, authenticated sessions, and transport.
3. Define capabilities per platform because Android, iOS, and desktop differ.
4. Never treat presence on the same LAN as proof of trust.
5. Add protocol tests and security review before enabling transfers.
6. Require hardware tests and evidence before marking a feature complete; a workflow or interface alone is not proof.
