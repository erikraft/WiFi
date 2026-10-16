# Clientes desktop: Windows, Linux e macOS

## Objetivo e estado atual

O diretório `desktop/` contém o cliente desktop inicial baseado em Tauri 2 + Rust. Ele usa o mesmo azul principal do app Android, `#4285F4`, e tem como alvo Windows, Linux e macOS. A primeira implementação reproduz o contrato de descoberta LAN existente; ainda não é uma versão estável.

Implementado nesta primeira etapa:
- Janela responsiva e acessível por teclado, com rótulos em inglês e português brasileiro escolhidos pelo idioma do sistema.
- Endpoint TCP de metadados compatível com a versão 1 do protocolo Android.
- Anúncio e descoberta mDNS/DNS-SD usando `_erikraftwifi._tcp.local.`.
- Validação da resposta de metadados públicos antes de mostrar um par.
- Matriz de CI que tenta gerar pacotes no Windows, Linux e macOS.

Ainda não implementado:
- Transferência de arquivos, identidade autenticada de pares, sessões criptografadas, Wi-Fi Direct, roteamento mesh ou encaminhamento por gateway de Internet.
- Instaladores Windows assinados, notarização macOS, atualizações automáticas ou gerenciamento de canais de release.
- Suporte garantido a todas as interfaces de rede, VPNs, políticas de multicast ou configurações de firewall.

A matriz de build e os testes em dispositivos reais precisam passar antes que o app desktop seja considerado pronto para lançamento.

## Arquitetura

- **Frontend:** HTML/CSS/JavaScript estático empacotado localmente pelo Tauri. Os tokens visuais reutilizam o azul `#4285F4` do app Android; não formam uma paleta de produto separada.
- **Runtime nativo:** Rust via Tauri 2, para Windows, Linux e macOS.
- **Descoberta:** a versão fixada do crate `mdns-sd` implementa mDNS/DNS-SD. Ela anuncia e procura o mesmo tipo de serviço que o protótipo Android.
- **Metadados:** uma linha JSON TCP com `service`, `protocol`, `capabilities` e `fileTransfer`. O endpoint contém apenas metadados públicos e não deve ser tratado como canal confiável.
- **Sem dependência de nuvem para descoberta:** o fluxo nativo de descoberta opera na rede local. O site e os links externos da documentação não fazem parte do caminho de dados da descoberta.

## Desenvolvimento local

Requisitos:
- Node.js 22 ou superior.
- Toolchain estável do Rust.
- Dependências da plataforma Tauri: WebView2 no Windows; pacotes de desenvolvimento WebKitGTK no Linux; ferramentas de build Apple e suporte WebKit no macOS.

Dentro de `desktop/`:

```bash
npm install
npm run tauri -- dev
```

Para gerar um pacote local de release:

```bash
npm run tauri -- build
```

O build pode exigir os pré-requisitos de sistema listados no [guia oficial de pré-requisitos do Tauri](https://v2.tauri.app/start/prerequisites/).

## Matriz de validação multiplataforma

| Área | Windows | Linux | macOS |
|---|---|---|---|
| Build em CI | Obrigatório | Obrigatório | Obrigatório |
| mDNS na interface ativa | Teste em LAN física | Teste em LAN física | Teste em LAN física |
| Firewall / permissões | Verificar Windows Firewall | Verificar diferenças entre distribuições/firewalls | Verificar avisos de rede local e firewall |
| Troca de interface / suspensão / retomada | Testar | Testar | Testar |
| Assinatura e pacote de release | Exige plano de assinatura de código | Exige formatos e testes por distribuição | Exige assinatura e notarização |
| Acessibilidade / escala | Testar HiDPI e teclado | Testar escala fracionária e teclado | Testar Retina e teclado |

Compilar com sucesso não prova que multicast funcione em todas as redes. Wi-Fi corporativo, redes de convidados, VPNs, isolamento de clientes e regras de firewall podem bloquear mDNS ou conexões TCP entre pares.

## Interface e acessibilidade

Use o azul `#4285F4` do app Android para a identidade compartilhada, foco visível e ações primárias. O layout deve adaptar-se a janelas pequenas e grandes; não exija largura fixa. Ofereça navegação por teclado, contraste legível, movimento reduzido, escala do sistema e textos que possam quebrar linha sem cortes.

## Barreiras de segurança antes de transferir arquivos

Antes de adicionar qualquer função de transferência:
1. Definir um contrato de protocolo versionado e revisado.
2. Estabelecer identidade autenticada dos pares e consentimento explícito para cada transferência.
3. Usar uma sessão criptografada com integridade e defesa contra replay.
4. Definir limites de tamanho, timeouts, pedidos simultâneos e cancelamento.
5. Manter caminhos e dados do usuário fora dos metadados de descoberta e logs.
6. Testar pares hostis, metadados malformados, desconexões e esgotamento de recursos.
7. Reutilizar o motor de transferência existente do Drop após revisar seus contratos atuais, em vez de criar uma segunda pilha de transferência.

## Referências técnicas oficiais

- [Pré-requisitos Tauri 2](https://v2.tauri.app/start/prerequisites/)
- [Capacidades de segurança do Tauri](https://v2.tauri.app/security/capabilities/)
- [Crate Rust mDNS-SD](https://docs.rs/mdns-sd/0.21.5/mdns_sd/)
- [Descoberta de serviços de rede Android](https://developer.android.com/develop/connectivity/wifi/use-nsd)

## Regra de release

O protótipo desktop deve ser chamado de **experimental** até que a matriz Windows/Linux/macOS passe e os testes em dispositivos reais confirmem descoberta, parada, troca de interface e falhas seguras. Não anuncie transferência de arquivos nem conectividade segura antes de implementar e revisar essas funções.
