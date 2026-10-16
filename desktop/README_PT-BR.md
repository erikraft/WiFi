# ErikrafT Wi-Fi™ Desktop

Este diretório inicia o cliente desktop multiplataforma para Windows, Linux e macOS usando Tauri 2 e Rust. A identidade visual usa o azul principal do app Android, `#4285F4`, e o primeiro alvo é o protocolo existente de descoberta LAN.

## Escopo atual

- Anuncia um serviço local do tipo `_erikraftwifi._tcp.local.`.
- Fornece o mesmo contrato JSON de metadados públicos do protótipo Android (protocolo 1).
- Procura serviços compatíveis na LAN e valida a resposta TCP de metadados antes de exibir um par.
- Usa rede nativa em Rust em vez de um serviço Web remoto para descoberta local.
- Não transfere arquivos, não autentica pares, não criptografa o endpoint de metadados, não implementa Wi-Fi Direct nem roteamento mesh e não fornece gateway de Internet.

Esta é uma implementação inicial, não um produto pronto para lançamento. Builds multiplataforma, comportamento mDNS, seleção de rota, solicitações de firewall e descoberta de pares precisam ser verificados na matriz do GitHub Actions e em dispositivos reais Windows, Linux e macOS antes de uma publicação.

## Requisitos

- Node.js 22 ou superior.
- Toolchain estável do Rust.
- Pré-requisitos da plataforma Tauri: WebView2 no Windows; bibliotecas de desenvolvimento WebKitGTK no Linux; Xcode Command Line Tools e suporte WebKit no macOS.

## Build local

A partir deste diretório:

```bash
npm install
npm run tauri -- dev
```

Para gerar pacotes de release:

```bash
npm run tauri -- build
```

Consulte a [documentação do cliente desktop](../docs/desktop-clients_PT-BR.md) e o [guia global de segurança](../docs/security_PT-BR.md).
