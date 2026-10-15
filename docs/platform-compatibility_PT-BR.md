# Compatibilidade de rede e limites regulatórios

## Princípio

ErikrafT Wi-Fi é um software que deve usar APIs e rádios já existentes nos dispositivos. O aplicativo não cria uma nova forma de onda de rádio, não transforma o telefone em uma estação-base 2G/3G/4G/5G e não deve tentar transmitir nas faixas licenciadas de operadoras.

Compatibilidade significa poder executar em diferentes aparelhos e aproveitar as interfaces de rede que o sistema operacional oferece. Não significa que todo aparelho suporte todos os modos, nem que o aplicativo controle o modem celular.

## Cenários

| Cenário | Comportamento possível | Limite |
|---|---|---|
| Sem Wi-Fi de infraestrutura e sem sinal celular | Dispositivos compatíveis podem conectar diretamente via Wi-Fi Direct, ou formar uma rede local usando equipamento compatível | Wi-Fi Direct ainda não está implementado neste protótipo; alcance e compatibilidade variam por aparelho |
| Cidade, sem dados móveis, com Wi-Fi conectado à Internet | O tráfego pode sair pelo Wi-Fi sem usar o pacote de dados móveis | Precisa de um ponto de acesso/gateway com Internet e autorização para usá-lo |
| Sem sinal de operadora, com outros nós locais | Serviços locais podem funcionar; uma malha pode encaminhar tráfego se houver nós e software de roteamento apropriados | Mesh routing ainda não está implementado. Sem gateway conectado, não há acesso à Internet pública |
| Celular 2G/3G/4G/5G | O app pode funcionar por Wi-Fi em aparelhos que também tenham essas tecnologias celulares | Não fornece nem substitui a rede móvel; o sistema operacional e o hardware controlam o modem |
| iOS, Android, Windows, Linux e macOS | Compatibilidade futura depende de APIs, permissões, hardware e cliente próprio em cada plataforma | Atualmente o cliente de rede é Android; a landing page não é um cliente de conectividade |

## Caminho sem construir torres

1. **Agora:** testar descoberta LAN entre aparelhos Android na mesma rede Wi-Fi.
2. **Próximo:** implementar Wi-Fi Direct com APIs oficiais e testar em hardware compatível.
3. **Depois:** criar um protocolo de sessão autenticada e criptografada antes de transportar arquivos ou tráfego de terceiros.
4. **Só então:** avaliar encaminhamento multi-hop e gateways, incluindo consumo de bateria, prevenção de loops, consentimento do usuário, limites de tráfego e segurança.
5. **Acesso à Internet:** reutilizar um roteador, telefone compartilhando conexão, rede comunitária autorizada ou provedor/gateway existente. Um nó sem rota externa não consegue inventar acesso à Internet.

Não é necessário comprar ou construir uma torre para começar o desenvolvimento. Ainda assim, gateways, enlaces de rádio dedicados, equipamentos de transmissão próprios ou oferta de serviço de telecomunicações podem ter obrigações específicas; isso depende da arquitetura e do uso real.

## Anatel e uso de radiofrequência no Brasil

Este projeto deve limitar-se a APIs normais do Android e a equipamentos de Wi-Fi homologados, operando conforme as regras aplicáveis. Não modificar firmware para aumentar potência, não desbloquear frequências, não transmitir em faixas licenciadas e não anunciar uma rede celular própria.

A Anatel informa que, em regra, produtos de telecomunicações — incluindo dispositivos que emitem radiofrequência, como Wi-Fi — estão sujeitos à homologação para comercialização/uso conforme a regulamentação aplicável. Usar um telefone ou roteador homologado não significa automaticamente que qualquer equipamento adicional, enlace ou modelo de prestação de serviço esteja autorizado. Antes de comercializar hardware, instalar enlaces dedicados ou oferecer conectividade a terceiros como serviço, confirme o enquadramento com a Anatel ou profissional regulatório qualificado.

Referências oficiais:
- Anatel — dúvidas frequentes sobre certificação e homologação: https://www.gov.br/anatel/pt-br/regulado/certificacao-de-produtos/duvidas-frequentes
- Anatel — regulamentação: https://www.gov.br/anatel/pt-br/regulado/certificacao-de-produtos/regulamentacao
- Android — Wi-Fi Direct: https://developer.android.com/develop/connectivity/wifi/wifi-direct
- Android — descoberta de serviços de rede (NSD): https://developer.android.com/develop/connectivity/wifi/use-nsd
- Android — permissões de Wi-Fi próximo: https://developer.android.com/develop/connectivity/wifi/wifi-permissions

## Estado real do repositório

- Implementado experimentalmente: descoberta NSD/DNS-SD na mesma LAN.
- Ainda pendente: Wi-Fi Direct, mesh routing, gateways, transporte seguro e integração funcional com ErikrafT Drop.
- Não existe cliente desktop executável neste momento.
- A compatibilidade precisa ser validada por versão de Android, fabricante e hardware; “compatível com tudo” não é uma promessa tecnicamente verificável.
