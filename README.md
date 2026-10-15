# ErikrafT Wi-Fi™

Projeto open-source para conectividade local usando Wi-Fi e descoberta de dispositivos, com evolução planejada para Wi-Fi Direct, gateways e integração com ErikrafT Drop™.

> **Estado do protótipo:** descoberta LAN e interface inicial são experimentais. O transporte ainda não deve ser usado para dados sensíveis. Wi-Fi Direct, Mesh e integração funcional com Drop continuam no roadmap; não são anunciados como concluídos.

## Componentes
- **Android:** app nativo Java, com interface azul inspirada no ErikrafT Drop™ for Android.
- **Landing page:** site responsivo azul com visão geral, estado do projeto e links para os repositórios Drop.
- **Integração planejada:** deep link `erikraftwifi://open` e documentação de contrato para Drop Web/Desktop e Drop Android.

Abra `app/` no Android Studio. Para a landing page, abra `index.html` ou use um servidor estático. Leia `docs/architecture.md`, `docs/drop-integration.md` e `docs/security.md` antes de ampliar o protocolo.

## Repositórios relacionados
- [ErikrafT Drop™ Web/Desktop](https://github.com/erikraft/Drop)
- [ErikrafT Drop™ for Android](https://github.com/erikraft/Drop-Android)
- [Site ErikrafT](https://github.com/erikraft/site)

## Segurança
Descoberta de pares não é autenticação. Não transfira arquivos privados até que autenticação, integridade e criptografia ponta a ponta estejam implementadas e testadas.