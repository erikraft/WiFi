# ErikrafT Wi-Fi™

Projeto open-source para conectividade local usando tecnologias Wi-Fi existentes. O protótipo Android agora anuncia o próprio nó e procura outros nós ErikrafT Wi-Fi na mesma rede local usando Android Network Service Discovery (DNS-SD/mDNS).

> **Estado do protótipo:** descoberta LAN experimental implementada no Android. O endpoint TCP serve apenas metadados públicos de capacidades; não transfere arquivos nem autentica pares. Não use para dados sensíveis. Wi-Fi Direct, Mesh, gateways e integração funcional com Drop continuam no roadmap.

## Componentes
- **Android:** app nativo Java com interface azul inspirada no ErikrafT Drop™ for Android e descoberta LAN por NSD/mDNS.
- **Landing page:** site responsivo azul com visão geral, estado do projeto e links para os repositórios Drop.
- **Integração planejada:** deep link `erikraftwifi://open` e documentação de contrato para Drop Web/Desktop e Drop Android.

Abra `app/` no Android Studio. Para a landing page, abra `index.html` ou use um servidor estático. Leia `docs/architecture.md`, `docs/lan-discovery.md`, `docs/drop-integration.md` e `docs/security.md` antes de ampliar o protocolo.

## Repositórios relacionados
- [ErikrafT Drop™ Web/Desktop](https://github.com/erikraft/Drop)
- [ErikrafT Drop™ for Android](https://github.com/erikraft/Drop-Android)
- [Site ErikrafT](https://github.com/erikraft/site)

## Segurança
Descoberta não é autenticação. O endpoint atual não transporta arquivos, mas não fornece um canal autenticado ou criptografado. Não transfira arquivos privados até que autenticação, integridade e criptografia ponta a ponta estejam implementadas e testadas.
