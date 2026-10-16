# Arquitetura inicial

## Implementado nesta etapa
- Landing page estática responsiva em azul.
- App Android nativo com interface inicial e deep link `erikraftwifi://open`.
- Descoberta experimental de nós na mesma rede local usando Android NSD/DNS-SD (mDNS).
- Endpoint TCP efêmero que responde apenas metadados públicos de protocolo/capacidades; não recebe nem envia arquivos.
- Workflow de CI que tenta compilar o APK debug e workflow manual/tag para APK/AAB Android.

## Interface Android e atalhos

- **Acesso simples:** a tela principal mantém a descoberta LAN como ação principal e usa linguagem direta.
- **Configurações avançadas:** uma seção expansível explica as capacidades atuais e os recursos de rede ainda pendentes. Ela não finge oferecer controles de mesh/gateway que ainda não foram implementados.
- **Quick Settings:** o serviço `WifiQuickSettingsTileService` oferece um atalho para abrir o app. O símbolo branco é reproduzido como VectorDrawable Android no tamanho de 24 dp, sem modificar o SVG original. O atalho não alterna o rádio Wi-Fi.
- **PiP:** opcional e desativado por padrão; a preferência é persistida. Em Android 8.0+, ao sair do app, ele solicita Picture-in-Picture. Fabricantes e políticas do sistema podem limitar esse comportamento.


## Páginas offline e localização do app

- Salvar uma página renderizada pelo WebView Android como arquivo MHTML para leitura local.
- Importar arquivos HTML/MHTML que o seletor de documentos do Android disponibilize e exportar arquivos arquivados.
- Compartilhar um arquivo salvo por meio de URI de um `ContentProvider` somente leitura, concedendo acesso apenas àquele arquivo.
- O seletor de idioma do app disponibiliza 16 traduções offline para tela principal, diagnósticos, controles da biblioteca e status de descoberta. O globo interativo usa o mesmo idioma selecionado.
- Compartilhar um URL não é o mesmo que compartilhar a página baixada privadamente pelo Chrome. O app não pode acessar o armazenamento privado do Chrome; para ler offline, é necessário um arquivo HTML/MHTML exposto pelo Android.

## Ainda não implementado
- Wi-Fi Direct / Wi-Fi P2P.
- Mesh routing, encaminhamento entre nós e gateways para Internet.
- Autenticação de pares, canal criptografado, mensagens mesh seguras, transferência de mídia e chamadas de áudio/vídeo em tempo real.
- Integração funcional com ErikrafT Drop™ para Web/Desktop e ErikrafT Drop™ para Android. O deep link é apenas um ponto de entrada.
- Cliente desktop executável para Windows, Linux e macOS.
- Suporte a iOS ou qualquer capacidade de operar como estação-base de rede celular.

## Compatibilidade e limites
O app pode rodar em aparelhos que também tenham 2G/3G/4G/5G, mas isso não faz com que o app forneça essas redes celulares. O tráfego via Internet precisa de uma rota para um gateway conectado. Sem gateway, nós podem oferecer apenas conectividade local quando o transporte correspondente estiver implementado.

A implementação deve utilizar APIs oficiais do sistema e rádios existentes, sem alterar potência, frequências ou firmware. Para o contexto regulatório brasileiro, leia [platform-compatibility.md](platform-compatibility.md).

## Objetivo de conectividade de longo prazo

A meta é permitir conectividade interoperável com a Internet usando interfaces de rede existentes e rotas autorizadas, com suporte futuro a conexões diretas entre dispositivos, encaminhamento multi-hop e gateways quando tecnicamente possível. O cliente deve detectar e descrever capacidades reais por dispositivo e manter fallback para redes locais quando não houver rota externa.

Não existe uma técnica de software que garanta acesso a qualquer site público sem que algum caminho de rede alcance a Internet. Wi-Fi Direct pode criar um enlace local; mesh pode encaminhar tráfego entre nós se houver roteamento funcional; somente um gateway com upstream fornece saída à Internet. DNS, NAT, IPv4/IPv6, firewall, captive portals, TLS, políticas dos provedores e consentimento dos operadores precisam ser considerados e testados.

## Direção técnica
1. Usar APIs de rede existentes; não inventar protocolo de rádio.
2. Separar descoberta, identidade de nó, sessão autenticada e transporte.
3. Definir capacidades por plataforma, pois Android, iOS e desktop diferem.
4. Nunca tratar presença na mesma LAN como prova de confiança.
5. Adicionar testes de protocolo e revisão de segurança antes de habilitar transferência.
6. Não marcar funcionalidade como concluída apenas porque existe um workflow ou uma interface; exigir testes em hardware e evidências.
