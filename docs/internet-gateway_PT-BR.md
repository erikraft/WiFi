# Gateway de Internet no Android

O ErikrafT Wi-Fi inclui uma integração experimental com o compartilhamento nativo do Android. Ela permite preparar um telefone Android com Internet para compartilhá-la diretamente com outro telefone que não tenha dados móveis próprios.

## Como testar o compartilhamento direto

1. No telefone gateway, conecte-se a uma rede que realmente tenha acesso à Internet (dados móveis, Ethernet ou Wi-Fi quando o aparelho suportar compartilhar essa conexão).
2. Abra ErikrafT Wi-Fi → **Configurar gateway de Internet** → **Abrir configurações do hotspot**.
3. Nas configurações do Android, ative o Hotspot/Ancoragem, defina uma senha forte e confirme que o sistema está compartilhando a conexão externa.
4. Volte ao app e escolha **Já ativei o hotspot — anunciar**. O app só anuncia o gateway quando o Android reporta Internet validada.
5. No telefone cliente, conecte-se ao Wi-Fi do hotspot usando a senha fornecida pelo proprietário do gateway. O telefone cliente não precisa de dados móveis próprios.

O encaminhamento IP/NAT é realizado pelo sistema Android, não pelo processo ErikrafT Wi-Fi. Um app comum não pode ativar tethering silenciosamente nem configurar rotas de sistema sem permissões privilegiadas; por isso a ativação exige uma ação explícita nas configurações. A disponibilidade de compartilhar Wi-Fi como upstream depende do aparelho, versão do Android e fabricante. Alguns aparelhos desativam Wi-Fi cliente quando o hotspot é ligado; outros oferecem repetidor/compartilhamento Wi-Fi.

## O que o anúncio significa

Durante a descoberta NSD/mDNS, o app pode publicar campos TXT `internet=yes/no` e `gateway=android-tethering-v1/none`. O anúncio exige ação explícita do usuário e que a conexão ativa seja marcada pelo Android com `NET_CAPABILITY_INTERNET` e `NET_CAPABILITY_VALIDATED`. Esses campos são apenas um indício local não autenticado; serviços da rede podem falsificá-los. Não os use como prova de identidade, confiança ou garantia de que o hotspot continua ativo.

## Limites desta etapa

- **Compartilhamento direto:** o cliente deve conectar-se à rede Wi-Fi do hotspot Android. Isso pode fornecer Internet a um telefone sem dados móveis próprios.
- **Não é encaminhamento mesh do aplicativo:** o ErikrafT Wi-Fi ainda não roteia pacotes IP de um cliente por nós intermediários, não cria automaticamente uma rede multi-hop e não transporta todo o tráfego de Internet pela conexão TCP do app.
- **Consentimento e segurança:** o proprietário do dispositivo gateway deve configurar o hotspot no Android; use senha e compartilhe-a somente com pessoas autorizadas.
- **Internet externa obrigatória:** sem uma conexão upstream real no gateway, o hotspot só oferece uma rede local e não pode abrir sites públicos.

Teste o cenário com dois dispositivos físicos e confirme navegação no cliente; a compilação do APK, sozinha, não prova que cada fabricante permite esse modo de compartilhamento.
