# Descoberta de dispositivos na LAN (Android)

O app usa `NsdManager` (Network Service Discovery / DNS-SD sobre mDNS) para anunciar o próprio nó e localizar outros serviços `_erikraftwifi._tcp.` na rede local. A descoberta funciona somente entre instâncias compatíveis alcançáveis pela mesma rede multicast; isolamento de clientes no roteador, VPNs, firewall, economia de energia e políticas do fabricante podem impedir resultados.

## O que o protótipo faz
1. Abre um socket TCP em uma porta efêmera.
2. Anuncia o nome do nó e essa porta por NSD.
3. Procura serviços com o tipo ErikrafT Wi-Fi e exibe nome, endereço e porta resolvidos.
4. O socket responde apenas um pequeno objeto JSON com versão e capacidades públicas; não aceita arquivos ou dados do usuário. O anúncio DNS-SD pode incluir os campos TXT `internet=yes/no` e `gateway=android-tethering-v1/none`, calculados a partir da conectividade validada do Android e de uma opção explícita do usuário. Esses valores não são autenticados.

## O que não faz
- Não descobre automaticamente qualquer dispositivo da rede; o outro dispositivo precisa anunciar o mesmo tipo de serviço.
- Não cria um hotspot nem muda de rede.
- Não estabelece conexão Wi-Fi Direct.
- Não autentica pares nem cifra o endpoint de metadados.
- Não transfere arquivos nem roteia tráfego Mesh. O anúncio de gateway é somente um sinal local; o compartilhamento direto de Internet é feito pelo hotspot/tethering nativo do Android, não por roteamento do app.

## Teste manual
1. Instale a mesma versão do app em dois dispositivos Android.
2. Conecte ambos à mesma rede Wi-Fi, sem isolamento entre clientes.
3. Abra o app em ambos e toque em **Procurar dispositivos**.
4. Confirme se cada dispositivo aparece no outro. Repita após parar a descoberta e mudar de rede.

A ausência de resultados não prova defeito no app: valide o multicast/mDNS da rede e as permissões do sistema. O teste não é substituto para testes automatizados em dispositivos físicos.

## Segurança
O endpoint é apenas um protótipo de metadados públicos e não deve ser considerado confiável. Não envie dados privados. Antes de implementar transferência, adicionar autenticação mútua, criptografia ponta a ponta, consentimento, validação estrita e testes de segurança.
