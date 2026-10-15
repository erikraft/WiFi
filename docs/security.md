# Segurança do protótipo

A descoberta LAN usa NSD/DNS-SD para anunciar e localizar instâncias do ErikrafT Wi-Fi na rede local. O endpoint TCP anunciado responde somente metadados públicos de serviço, versão e capacidades; não recebe arquivos nem conteúdo do usuário.

**Limitações atuais**
- Não há autenticação ou identidade criptográfica de pares.
- O endpoint de metadados não é um canal seguro e não deve ser tratado como prova de identidade.
- Não há transferência de arquivos, Wi-Fi Direct, mesh routing ou gateways implementados.
- Dispositivos na mesma rede podem ser hostis ou estar comprometidos.

Antes de ativar comunicação de dados, implementar e testar identidade de pares, consentimento explícito, autenticação mútua, criptografia ponta a ponta, proteção contra replay, validação de payloads, limites de tamanho e tempo, cancelamento e logs sem conteúdo sensível. Estar na mesma rede Wi-Fi não é uma prova de confiança.
