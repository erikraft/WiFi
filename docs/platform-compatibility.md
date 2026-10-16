# Network and platform compatibility

## Principle

ErikrafT Wi-Fi uses network interfaces and radio hardware already provided by the operating system. It does not invent a radio protocol, turn a phone into a cellular base station, or bypass radio controls. Compatibility means that a client can run on a platform and use the APIs that platform exposes; it does not mean all devices support every network mode.

## Current platform status

| Platform | Repository component | Current status |
|---|---|---|
| Android | `app/` | Native Java prototype; experimental LAN service discovery using NSD/DNS-SD (mDNS) |
| Windows | `desktop/` | Initial Tauri + Rust prototype; CI build and hardware discovery testing required |
| Linux | `desktop/` | Initial Tauri + Rust prototype; distribution/WebKitGTK and hardware testing required |
| macOS | `desktop/` | Initial Tauri + Rust prototype; signing, notarization, permission, and hardware testing required |
| iOS | None | No client implemented |
| Website | `index.html` | Project information and documentation, not a network-connectivity client |

The desktop prototype shares the Android metadata contract, but this is not proof of full feature parity or release readiness.

## Connectivity scenarios

| Scenario | Possible behaviour | Limitation |
|---|---|---|
| Same LAN with multicast enabled | Compatible clients may announce and discover services | Client isolation, VPNs, firewalls, sleep policies, and router settings may block discovery |
| Wi-Fi without mobile data, but with an Internet-connected router | Traffic may use Wi-Fi | Requires a working upstream route and permission to use the network |
| No external Internet route | Local services may work when their transports are implemented | The software cannot create a route to the public Internet by itself |
| Direct device-to-device link | A supported OS/hardware mode may be suitable | Wi-Fi Direct is not implemented in this project |
| Multiple nodes / mesh | A future implementation could relay traffic with suitable routing | Mesh routing, relay consent, loop avoidance, and gateway support are not implemented |
| Cellular-capable phone | The app may use Wi-Fi on that device | The app does not provide or control cellular service |

## Safe technical path

1. Validate current LAN discovery and the public metadata contract on each supported platform.
2. Implement and test an explicit, versioned capability model.
3. Design peer authentication and encrypted sessions before sending private content.
4. Evaluate direct links and routing only against official platform APIs and documented hardware capabilities.
5. Add gateway forwarding only after consent, security, rate limiting, battery, abuse, and failure modes are reviewed.

For market-specific radio, product, privacy, and distribution requirements, see the [international regulatory overview](international-regulatory-overview.md). Certification of an existing phone or router does not certify arbitrary new radio hardware or authorize bypassing its normal controls.

## Official technical references

- [Android Network Service Discovery](https://developer.android.com/develop/connectivity/wifi/use-nsd)
- [Android Wi-Fi Direct](https://developer.android.com/develop/connectivity/wifi/wifi-direct)
- [Tauri 2 platform prerequisites](https://v2.tauri.app/start/prerequisites/)
- [Tauri security capabilities](https://v2.tauri.app/security/capabilities/)

## Evidence required for compatibility claims

A CI compile is only build evidence. For each supported OS, record the OS version, device/model, network topology, firewall/VPN state, discovery start/stop results, duplicate/lost service handling, suspend/resume, and logs. Avoid the claim “works on every device”; publish only configurations that have been actually tested.
