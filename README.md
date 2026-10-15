<p align="center">
  <img src="assets/ErikrafT_Wifi_Icon_Logo.svg" alt="ErikrafT Wi-Fi™ logo" width="120">
  <h1><a href="https://wifi.erikraft.com/">ErikrafT Wi-Fi™</a></h1>
</p>

Projeto open-source para conectividade local usando tecnologias Wi-Fi existentes. O protótipo Android anuncia o próprio nó e procura outros nós ErikrafT Wi-Fi na mesma rede local usando Android Network Service Discovery (DNS-SD/mDNS).

> **Estado do protótipo:** descoberta LAN experimental. O endpoint TCP responde apenas metadados públicos; não transfere arquivos nem autentica pares. Não use para dados sensíveis. Wi-Fi Direct, mesh, gateways, transporte seguro e integração funcional com Drop ainda não estão implementados.

## Objetivo de conectividade

O objetivo de longo prazo é permitir que dispositivos compatíveis formem ou usem uma rede Wi-Fi e alcancem serviços da Internet por uma rota disponível, inclusive por um gateway autorizado ou por nós intermediários quando o roteamento mesh estiver implementado. O projeto deve priorizar compatibilidade com APIs e hardware existentes, em vez de depender de rádio proprietário.

**Limite fundamental:** Wi-Fi cria um enlace de rede, não uma conexão automática à Internet. Para abrir sites públicos, precisa existir uma rota externa funcional (roteador, conexão compartilhada ou gateway com Internet). Sem essa rota, o modo offline pode atender comunicação e serviços locais, mas não acessar arbitrariamente a Web pública. Wi-Fi Direct, roteamento mesh e gateways ainda não estão implementados; consulte [compatibilidade e limites](docs/platform-compatibility.md).

## Componentes
- **Android:** app nativo Java com interface azul e descoberta LAN experimental por NSD/mDNS.
- **Identidade visual:** logotipos SVG colorido e branco em `assets/`; o app Android usa o ícone branco sobre azul `#4285F4`, alinhado ao ErikrafT Drop™ for Android.
- **Landing page:** site responsivo azul com visão geral, logotipo oficial e links para os repositórios Drop.
- **Integração planejada:** deep link `erikraftwifi://open`; isso não significa que a integração com Drop já funcione.
- **Builds:** workflow de validação compila APK debug; workflow de release gera APK/AAB e usa assinatura Base64 via GitHub Actions secrets, se configurada.

## Interface Android, Quick Settings e PiP

## Interface Android, Quick Settings e PiP

- A interface oferece uma entrada de configurações avançadas para consultar o estado real e os recursos ainda pendentes, sem apresentá-los como já disponíveis.
- O usuário pode habilitar o Picture-in-Picture (PiP) ao sair do app em Android 8.0 ou superior; o recurso depende do suporte do aparelho e das configurações do sistema.
- O serviço Quick Settings adiciona um atalho para abrir o ErikrafT Wi-Fi. Ele usa um VectorDrawable Android com a geometria do símbolo branco de `assets/ErikrafT_Wifi_Icon_Logo_White.svg`, dimensionado para 24 dp; o arquivo SVG original não é alterado. O atalho não liga/desliga o Wi-Fi nem cria uma conexão sozinho.
- A interface ainda é um protótipo; o painel avançado não configura mesh ou gateways porque essas funções ainda não existem no núcleo de rede.

## Build e distribuição

Abra `app/` no Android Studio ou consulte [docs/builds.md](docs/builds.md) para executar os workflows e configurar a assinatura com segurança. Não adicione keystores, senhas ou chaves privadas ao Git.

A aplicação atual é Android nativa. Ainda não existe cliente desktop para gerar executáveis funcionais Windows, Linux ou macOS. Consulte [docs/platform-compatibility.md](docs/platform-compatibility.md) para limites e roadmap.

## Documentação
- [Arquitetura](docs/architecture.md)
- [Descoberta LAN](docs/lan-discovery.md)
- [Compatibilidade de plataformas e limites regulatórios](docs/platform-compatibility.md)
- [Builds e assinatura](docs/builds.md)
- [Segurança](docs/security.md)
- [Integração com Drop](docs/drop-integration.md)

## Repositórios relacionados
- [ErikrafT Drop™ Web/Desktop](https://github.com/erikraft/Drop)
- [ErikrafT Drop™ for Android](https://github.com/erikraft/Drop-Android)
- [Site ErikrafT](https://github.com/erikraft/site)

## Segurança e compatibilidade
Descoberta não é autenticação. O endpoint atual não fornece um canal autenticado ou criptografado. Não transfira arquivos privados até que autenticação, integridade e criptografia ponta a ponta estejam implementadas e testadas. Compatibilidade varia conforme Android, fabricante, permissões e hardware; não é tecnicamente correto prometer suporte universal.
