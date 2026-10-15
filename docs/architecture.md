# Arquitetura inicial

## Implementado nesta etapa
- Landing page estática responsiva em azul.
- App Android nativo com interface inicial e deep link `erikraftwifi://open`.
- Descoberta experimental de nós na mesma rede local usando Android NSD/DNS-SD (mDNS).
- Endpoint TCP efêmero que responde apenas metadados públicos de protocolo/capacidades; não recebe nem envia arquivos.

## Ainda não implementado
- Wi-Fi Direct / Wi-Fi P2P.
- Mesh routing, encaminhamento entre nós e gateways para Internet.
- Autenticação de pares, canal criptografado e transferência segura.
- Integração funcional com Drop Web/Desktop e Drop Android. O deep link é apenas um ponto de entrada.

## Direção técnica
1. Usar APIs de rede existentes; não inventar protocolo de rádio.
2. Separar descoberta, identidade de nó, sessão autenticada e transporte.
3. Definir capacidades por plataforma, pois Android, iOS e desktop diferem.
4. Nunca tratar presença na mesma LAN como prova de confiança.
5. Adicionar testes de protocolo e revisão de segurança antes de habilitar transferência.
