<p align="center">
  <img src="assets/ErikrafT_Wifi_Icon_Logo.svg" alt="Logotipo ErikrafT Wi-Fi™" width="120">
  <h1 align="center"><a href="https://wifi.erikraft.com/">ErikrafT Wi-Fi™</a></h1>
</p>

<p align="center"><a href="README.md">English</a> · <a href="README_PT-BR.md">Português (Brasil)</a></p>

Projeto open-source que explora conectividade local usando tecnologias Wi-Fi existentes. O protótipo experimental para Android anuncia o próprio nó e procura outros nós ErikrafT Wi-Fi na mesma rede local usando Android Network Service Discovery (DNS-SD/mDNS).

> **Estado do protótipo:** a descoberta LAN é experimental. O endpoint TCP responde apenas metadados públicos; não transfere arquivos nem autentica pares. Não o use para dados sensíveis. Wi-Fi Direct, rede mesh, gateways, transporte seguro e integração funcional com ErikrafT Drop™ ainda não estão implementados.

## Objetivo de conectividade

O objetivo de longo prazo é permitir que dispositivos compatíveis formem ou usem uma rede Wi-Fi e alcancem serviços da Internet por uma rota disponível, inclusive por um gateway autorizado ou por nós intermediários quando o roteamento mesh estiver implementado. O projeto prioriza compatibilidade com APIs e hardware existentes, em vez de depender de tecnologia de rádio proprietária.

**Limite importante:** Wi-Fi cria um enlace de rede, não acesso automático à Internet. Para abrir sites públicos, é necessária uma rota externa funcional, como um roteador, uma conexão compartilhada ou um gateway conectado à Internet. Sem essa rota, o modo offline pode oferecer comunicação e serviços locais, mas não acessar arbitrariamente a Web pública. Wi-Fi Direct, roteamento mesh e gateways ainda não estão implementados; consulte [compatibilidade e limites das plataformas](docs/platform-compatibility_PT-BR.md).

## Componentes

- **App Android:** aplicativo nativo Java com interface azul e descoberta LAN experimental por NSD/mDNS.
- **Identidade visual:** logotipos SVG colorido e branco em `assets/`. O app Android usa o ícone branco sobre azul `#4285F4`, alinhado ao ErikrafT Drop™ para Android.
- **Landing page:** site azul responsivo com estado do projeto, documentação bilíngue e QR codes de pagamento Bitcoin.
- **Cliente desktop:** protótipo inicial Tauri + Rust em `desktop/` com alvo Windows, Linux e macOS. É experimental e exige CI e validação em dispositivos reais; não é uma versão estável.
- **Integração planejada:** o deep link `erikraftwifi://open` é um ponto de entrada exploratório; isso não significa que a integração com ErikrafT Drop™ já funcione.
- **Builds:** o workflow de validação compila um APK debug. O workflow de release gera artefatos APK/AAB e pode assiná-los usando uma keystore em Base64 fornecida por secrets do GitHub Actions.

## Interface Android, Quick Settings e PiP

- A interface oferece uma entrada para configurações avançadas, permitindo consultar o estado real e os recursos pendentes sem apresentá-los como disponíveis.
- O usuário pode habilitar Picture-in-Picture (PiP) ao sair do app no Android 8.0 ou superior; a disponibilidade depende do aparelho e das configurações do sistema.
- O serviço Quick Settings adiciona um atalho para abrir o ErikrafT Wi-Fi. Ele usa um VectorDrawable Android baseado no símbolo branco de `assets/ErikrafT_Wifi_Icon_Logo_White.svg`, dimensionado para 24 dp; o SVG original não é alterado. O atalho não liga/desliga o Wi-Fi nem cria uma conexão sozinho.
- O app continua sendo um protótipo. As configurações avançadas não configuram mesh nem gateways porque essas funções ainda não existem no núcleo de rede.

## Biblioteca offline e globo de pares LAN

O app Android inclui uma biblioteca experimental para leitura offline:
- Abrir um endereço, salvar uma captura MHTML pelo WebView, reabrir páginas salvas, importar HTML/MHTML, exportar arquivos e compartilhar uma página salva pela folha de compartilhamento do Android.
- Compartilhar diretamente um arquivo HTML/MHTML salvo com apps Android compatíveis, usando um URI de conteúdo somente leitura com permissão temporária por arquivo.
- Receber URLs ou arquivos compartilhados por apps Android compatíveis, preservando a extensão `.html` / `.mhtml` dos arquivos de páginas compartilhadas.
- Abrir o globo interativo com rotação, zoom e seleção de nós.
- O globo agora recebe pares reais descobertos por NSD/mDNS na mesma rede local, atualiza quando aparecem/desaparecem e exibe endereço/porta ao selecionar um par. As posições são apenas uma disposição visual determinística — não são coordenadas GPS nem um mapa geográfico. Pontos decorativos continuam sendo ilustrativos.
- A tela principal Android, biblioteca offline, diagnósticos e status de descoberta contam com seletor de idioma dentro do app e 16 variantes offline; os controles do globo também têm traduções correspondentes.

**Limitações de leitura offline:** o MHTML inclui somente os recursos que o WebView consegue capturar. Páginas com login, DRM, conteúdo dinâmico ou dependência de servidores podem não funcionar offline. O armazenamento privado de páginas baixadas pelo Chrome não pode ser acessado diretamente por outro app; exporte/compartilhe um arquivo que o Android disponibilize.

**Estado de mesh e chamadas:** a visualização de pares LAN não é uma rede mesh multi-hop. Ainda não há chat mesh offline, chamadas de áudio/vídeo sem Internet, sinalização WebRTC, roteamento Bluetooth mesh, transporte Wi-Fi Aware ou encaminhamento por gateway. Essas capacidades exigem implementação e testes adicionais em vários dispositivos.

## Build e distribuição

Abra `app/` no Android Studio ou siga [o guia de build e assinatura](docs/builds_PT-BR.md). Nunca adicione keystores, senhas ou chaves privadas ao Git.

O app Android continua experimental. Um protótipo desktop inicial em `desktop/` tem como alvo Windows, Linux e macOS com a mesma identidade azul `#4285F4`, mas precisa passar pelos builds de plataforma e testes em dispositivos reais antes de ser considerado pronto para lançamento. Consulte [clientes desktop](docs/desktop-clients_PT-BR.md) e [compatibilidade de plataformas](docs/platform-compatibility_PT-BR.md).

## Documentação

- [Arquitetura](docs/architecture_PT-BR.md) · [English](docs/architecture.md)
- [Descoberta LAN](docs/lan-discovery_PT-BR.md) · [English](docs/lan-discovery.md)
- [Compatibilidade de plataformas e limites regulatórios](docs/platform-compatibility_PT-BR.md) · [English](docs/platform-compatibility.md)
- [Builds e assinatura](docs/builds_PT-BR.md) · [English](docs/builds.md)
- [Segurança](docs/security_PT-BR.md) · [English](docs/security.md)
- [Integração com ErikrafT Drop™](docs/drop-integration_PT-BR.md) · [English](docs/drop-integration.md)
- [Clientes desktop](docs/desktop-clients_PT-BR.md) · [English](docs/desktop-clients.md)
- [Visão internacional de regulamentação](docs/international-regulatory-overview_PT-BR.md) · [English](docs/international-regulatory-overview.md)
- [Roadmap de integração com ErikrafT Drop™](docs/drop-integration-roadmap_PT-BR.md) · [English](docs/drop-integration-roadmap.md)
- [Experiência Web responsiva](docs/web-experience_PT-BR.md) · [English](docs/web-experience.md)
- [Guia do projeto para sistemas de IA](llms.txt) · [Português](llms_PT-BR.txt)

## Repositórios relacionados

- [ErikrafT Drop™ Web/Desktop](https://github.com/erikraft/Drop)
- [ErikrafT Drop™ para Android](https://github.com/erikraft/Drop-Android)

## Segurança e compatibilidade

Descoberta não é autenticação. O endpoint atual não fornece um canal autenticado ou criptografado. Não transfira arquivos privados até que autenticação, proteção de integridade e criptografia ponta a ponta estejam implementadas e testadas. A compatibilidade varia conforme a versão do Android, fabricante, permissões e hardware; não se deve presumir suporte universal.
