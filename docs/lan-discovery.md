# LAN device discovery (Android)

The app uses `NsdManager` (Network Service Discovery / DNS-SD over mDNS) to advertise its own node and discover other `_erikraftwifi._tcp.` services on the local network. Discovery works only between compatible instances reachable through the same multicast network. Router client isolation, VPNs, firewalls, battery-saving features, and manufacturer policies may prevent results.

## What the prototype does
1. Opens a TCP socket on an ephemeral port.
2. Advertises the node name and port through NSD.
3. Searches for ErikrafT Wi-Fi services and displays resolved names, addresses, and ports.
4. Returns a small JSON object containing public version and capability metadata only; it does not accept files or user data. DNS-SD may include TXT fields `internet=yes/no` and `gateway=android-tethering-v1/none`, based on Android's validated connectivity and explicit user opt-in. These values are unauthenticated.

## What it does not do
- Automatically discover every device on the network; other devices must advertise the same service type.
- Create a hotspot or switch networks.
- Establish a Wi-Fi Direct connection.
- Authenticate peers or encrypt the metadata endpoint.
- Transfer files, route mesh traffic, or provide an Internet gateway.

## Manual test
1. Install the same app version on two Android devices.
2. Connect both to the same Wi-Fi network, with client isolation disabled.
3. Open the app on both and tap **Discover devices**.
4. Check whether each device appears on the other. Repeat after stopping discovery and changing networks.

No results do not necessarily indicate an app defect: verify multicast/mDNS behavior and system permissions. This manual check does not replace automated tests on physical devices.

## Security
The endpoint is only a public-metadata prototype and must not be trusted. Do not send private data. Before implementing transfers, add mutual authentication, end-to-end encryption, consent, strict validation, and security tests.
