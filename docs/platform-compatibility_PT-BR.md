# Compatibilidade de rede e plataformas

## Princípio

ErikrafT Wi-Fi usa interfaces de rede e hardware de rádio já fornecidos pelo sistema operacional. Não inventa um protocolo de rádio, não transforma um telefone em estação-base celular nem contorna os controles do rádio. Compatibilidade significa que o cliente pode executar em uma plataforma e usar as APIs expostas por ela; não significa que todos os aparelhos suportam todos os modos de rede.

## Estado atual por plataforma

| Plataforma | Componente do repositório | Estado atual |
|---|---|---|
| Android | `app/` | Protótipo nativo Java; descoberta experimental de serviços LAN por NSD/DNS-SD (mDNS) |
| Windows | `desktop/` | Protótipo inicial Tauri + Rust; build em CI e testes de descoberta em hardware necessários |
| Linux | `desktop/` | Protótipo inicial Tauri + Rust; testes de distribuição/WebKitGTK e hardware necessários |
| macOS | `desktop/` | Protótipo inicial Tauri + Rust; testes de assinatura, notarização, permissões e hardware necessários |
| iOS | Nenhum | Nenhum cliente implementado |
| Site | `index.html` | Informações do projeto e documentação, não um cliente de conectividade |

O protótipo desktop compartilha o contrato de metadados do Android, mas isso não prova paridade de recursos nem prontidão para lançamento.

## Cenários de conectividade

| Cenário | Comportamento possível | Limite |
|---|---|---|
| Mesma LAN com multicast habilitado | Clientes compatíveis podem anunciar e descobrir serviços | Isolamento de clientes, VPNs, firewalls, suspensão e configuração do roteador podem bloquear a descoberta |
| Wi-Fi sem dados móveis, mas com roteador conectado à Internet | O tráfego pode usar Wi-Fi | Exige rota externa funcional e autorização para usar a rede |
| Sem rota externa para a Internet | Serviços locais podem funcionar quando os transportes correspondentes estiverem implementados | O software não cria sozinho uma rota para a Internet pública |
| Link direto entre dispositivos | Um modo suportado pelo sistema/hardware pode ser adequado | Wi-Fi Direct não está implementado neste projeto |
| Vários nós / mesh | Uma implementação futura poderia encaminhar tráfego com roteamento apropriado | Roteamento mesh, consentimento de retransmissão, prevenção de loops e gateways não estão implementados |
| Telefone com capacidade celular | O app pode usar Wi-Fi nesse aparelho | O app não fornece nem controla o serviço celular |

## Caminho técnico seguro

1. Validar a descoberta LAN atual e o contrato de metadados públicos em cada plataforma suportada.
2. Implementar e testar um modelo explícito de capacidades, com versão.
3. Projetar autenticação dos pares e sessões criptografadas antes de enviar conteúdo privado.
4. Avaliar links diretos e roteamento apenas com APIs oficiais de plataforma e capacidades documentadas do hardware.
5. Adicionar encaminhamento por gateway somente após revisar consentimento, segurança, limites de tráfego, bateria, abuso e falhas.

Para requisitos de rádio, produto, privacidade e distribuição específicos de mercados, consulte a [visão internacional de regulamentação](international-regulatory-overview_PT-BR.md). A certificação de um telefone ou roteador existente não certifica qualquer hardware de rádio novo nem autoriza contornar os controles normais desses equipamentos.

## Referências técnicas oficiais

- [Descoberta de serviços de rede Android](https://developer.android.com/develop/connectivity/wifi/use-nsd)
- [Wi-Fi Direct no Android](https://developer.android.com/develop/connectivity/wifi/wifi-direct)
- [Pré-requisitos de plataforma Tauri 2](https://v2.tauri.app/start/prerequisites/)
- [Capacidades de segurança Tauri](https://v2.tauri.app/security/capabilities/)

## Evidências necessárias para declarar compatibilidade

Compilar na CI é apenas evidência de build. Para cada sistema operacional, registre versão do sistema, modelo do dispositivo, topologia da rede, estado de firewall/VPN, resultados de iniciar/parar descoberta, tratamento de serviços duplicados/perdidos, suspensão/retomada e logs. Evite afirmar “funciona em todos os dispositivos”; publique somente configurações realmente testadas.
