# Arquitetura inicial

Esta PR estabelece uma base visual e de documentação, não um sistema de conectividade completo.

- Landing page estática responsiva em azul.
- App Android nativo com interface inicial e deep link erikraftwifi://open.
- Descoberta LAN, Wi-Fi Direct, mesh routing, gateways e transferência segura não estão implementados nesta entrega.

## Direção técnica
1. Usar APIs de rede existentes; não inventar protocolo de rádio.
2. Separar descoberta, identidade de nó, sessão autenticada e transporte.
3. Definir capacidades por plataforma, pois Android, iOS e desktop diferem.
4. Adicionar testes de protocolo antes de habilitar comunicação real.