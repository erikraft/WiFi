# Prototype security

LAN discovery uses NSD/DNS-SD to advertise and locate ErikrafT Wi-Fi instances on the local network. The advertised TCP endpoint returns public service, version, and capability metadata only; it does not receive files or user content.

## Current limitations
- Peer authentication and cryptographic peer identity are not implemented.
- The metadata endpoint is not a secure channel and must not be treated as proof of identity.
- File transfer, Wi-Fi Direct, and multi-hop mesh routing are not implemented. The app can hand off to Android's native hotspot for direct sharing and advertise unauthenticated gateway metadata.
- Devices on the same network may be hostile or compromised.

Before enabling data communication, implement and test peer identity, explicit consent, mutual authentication, end-to-end encryption, replay protection, payload validation, size and time limits, cancellation, and logs that exclude sensitive content. Being on the same Wi-Fi network is not proof of trust.
