# Android Internet gateway

ErikrafT Wi-Fi includes an experimental integration with Android's native tethering. It helps a phone with real Internet access share that connection directly with another phone that has no mobile data of its own.

## Test direct sharing

1. On the gateway phone, connect to a network that actually reaches the Internet (mobile data, Ethernet, or Wi-Fi if the device supports sharing that upstream connection).
2. Open ErikrafT Wi-Fi → **Set up Internet gateway** → **Open hotspot settings**.
3. In Android settings, enable Hotspot/Tethering, set a strong password, and confirm that Android is sharing the external connection.
4. Return to the app and choose **Hotspot enabled — advertise gateway**. The app advertises the gateway only when Android reports validated Internet access.
5. On the client phone, join the gateway phone's hotspot Wi-Fi using the password shared by its owner. The client phone does not need its own mobile data.

IP forwarding/NAT is performed by Android, not by the ErikrafT Wi-Fi process. An ordinary app cannot silently enable tethering or configure system routes without privileged permissions, so the user must enable the feature in system settings. Sharing Wi-Fi as an upstream depends on the device, Android version, and manufacturer; some devices disable Wi-Fi client mode when the hotspot starts while others support Wi-Fi sharing/repeater mode.

## Meaning of the advertisement

During NSD/mDNS discovery, the app can publish TXT fields `internet=yes/no` and `gateway=android-tethering-v1/none`. Advertisement requires explicit user action and an active connection reported by Android with `NET_CAPABILITY_INTERNET` and `NET_CAPABILITY_VALIDATED`. These fields are unauthenticated local hints: another device may spoof them. They are not proof of identity, trust, or continued hotspot availability.

## Limits of this step

- **Direct sharing works through Android hotspot:** the client must join the Android hotspot Wi-Fi. This can provide Internet to a phone with no mobile data plan/connection of its own.
- **This is not app-level mesh forwarding:** ErikrafT Wi-Fi does not yet route IP packets through intermediate nodes, automatically build a multi-hop network, or tunnel all client Internet traffic through the app's TCP connection.
- **Consent and security:** the gateway owner must configure Android hotspot sharing. Use a password and share it only with authorized people.
- **Real upstream Internet remains necessary:** without an external connection on the gateway, the hotspot creates only a local network and cannot reach public websites.

Test with two physical devices and verify browsing from the client. A successful APK build alone cannot prove every manufacturer's tethering implementation works.
