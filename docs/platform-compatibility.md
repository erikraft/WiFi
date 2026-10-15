# Network compatibility and regulatory limitations

## Principle
ErikrafT Wi-Fi is intended to use network APIs and radios already available on devices. It does not create a new radio waveform, turn a phone into a 2G/3G/4G/5G base station, or transmit on licensed carrier bands.

Compatibility means running on different devices and using the network interfaces exposed by their operating systems. It does not mean every device supports every mode or that the app controls the cellular modem.

## Scenarios

| Scenario | Possible behavior | Limitation |
|---|---|---|
| No infrastructure Wi-Fi and no cellular signal | Compatible devices may connect through Wi-Fi Direct or form a local network with compatible equipment | Wi-Fi Direct is not implemented; range and compatibility vary |
| No mobile data, but Wi-Fi has Internet access | Traffic may use Wi-Fi without consuming the mobile data allowance | Requires an Internet-connected access point/gateway and permission to use it |
| No carrier signal, but local nodes exist | Local services may work; a mesh could forward traffic with suitable nodes and routing software | Mesh routing is not implemented. Without a connected gateway, there is no public Internet access |
| Phone with 2G/3G/4G/5G | The app may work over Wi-Fi on a device that also has cellular technology | It does not provide or replace mobile service; the OS and hardware control the modem |
| iOS, Android, Windows, Linux, and macOS | Future compatibility depends on APIs, permissions, hardware, and a client for each platform | The current network client is Android-only; the landing page is not a connectivity client |

## A path that does not require building towers
1. **Now:** test LAN discovery between Android devices on the same Wi-Fi network.
2. **Next:** implement Wi-Fi Direct using official APIs and test compatible hardware.
3. **Then:** create an authenticated, encrypted session protocol before carrying files or third-party traffic.
4. **Only after that:** evaluate multi-hop forwarding and gateways, including battery use, loop prevention, consent, traffic limits, and security.
5. **Internet access:** reuse a router, a phone sharing its connection, an authorized community network, or an existing provider/gateway. A node without an external route cannot create Internet access by itself.

Development can begin without building a tower. However, gateways, dedicated radio links, custom transmitting equipment, or providing connectivity to third parties may involve specific obligations depending on the architecture and actual use.

## Anatel and radio-frequency use in Brazil
Use normal Android APIs and approved Wi-Fi equipment in accordance with applicable rules. Do not modify firmware to increase power, unlock frequencies, transmit on licensed bands, or advertise a private cellular network.

Anatel states that telecommunications products—including devices that emit radio frequency, such as Wi-Fi equipment—are generally subject to certification/homologation for commercialization or use under applicable regulations. Using an approved phone or router does not automatically authorize every additional device, radio link, or service model. Before selling hardware, deploying dedicated links, or providing connectivity as a service, confirm requirements with Anatel or a qualified regulatory professional.

Official references:
- Anatel — certification and homologation FAQ: https://www.gov.br/anatel/pt-br/regulado/certificacao-de-produtos/duvidas-frequentes
- Anatel — regulations: https://www.gov.br/anatel/pt-br/regulado/certificacao-de-produtos/regulamentacao
- Android — Wi-Fi Direct: https://developer.android.com/develop/connectivity/wifi/wifi-direct
- Android — network service discovery (NSD): https://developer.android.com/develop/connectivity/wifi/use-nsd
- Android — nearby Wi-Fi permissions: https://developer.android.com/develop/connectivity/wifi/wifi-permissions

## Actual repository status
- Experimentally implemented: NSD/DNS-SD discovery on the same LAN.
- Pending: Wi-Fi Direct, mesh routing, gateways, secure transport, and functional ErikrafT Drop integration.
- No executable desktop client is available.
- Compatibility must be validated by Android version, manufacturer, and hardware; universal support is not a technically verifiable promise.
