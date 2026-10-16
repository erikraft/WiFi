# Visão internacional de regulamentação e preparação para mercados

## Escopo e aviso

ErikrafT Wi-Fi é um projeto de software internacional. Não existe uma aprovação única que autorize automaticamente toda implementação Wi-Fi, equipamento de rádio, serviço de rede ou distribuição de software no mundo inteiro. Os requisitos dependem do país/território, hardware de rádio, faixa de frequência, potência, categoria do produto, importação/exportação, dados tratados e de o projeto ser apenas software, incluir equipamento ou prestar um serviço de conectividade.

Este documento é uma lista prática de engenharia com pontos de partida oficiais selecionados, **não é aconselhamento jurídico nem um catálogo exaustivo país por país**. Antes de um lançamento comercial em um novo mercado, verifique as regras vigentes com a autoridade local ou assessoria qualificada. Não deduza autorização legal a partir deste README ou da homologação de um telefone/roteador existente.

## Separe as camadas do produto

1. **Cliente somente de software:** podem se aplicar regras sobre descoberta na rede local, interface, escolhas de telemetria, avisos de privacidade, acessibilidade, proteção do software e distribuição.
2. **Software que controla rádios existentes e homologados/certificados:** use APIs documentadas do sistema operacional e mantenha potência, frequências, firmware e parâmetros regulatórios sob controle do sistema/hardware. A certificação do aparelho não permite contornar restrições do sistema.
3. **Hardware/firmware de rádio novo ou modificado:** autorização de rádio, ensaios, marcação de conformidade, espectro, compatibilidade eletromagnética, exposição, importação e rotulagem podem ser exigidos antes da venda ou uso.
4. **Conectividade oferecida a terceiros:** licenciamento de operador/provedor, comunicações públicas, interceptação legal, serviços de emergência, proteção ao consumidor, neutralidade de rede e retenção de dados podem depender do modelo e da jurisdição.
5. **Transferência ou roteamento de arquivos:** proteção de dados, cibersegurança, comunicação de abuso, transferência internacional de dados e tratamento de conteúdo do usuário precisam ser avaliados separadamente das aprovações de rádio.

O protótipo atual deve usar interfaces Wi-Fi existentes de forma normal. Não desbloqueie bandas licenciadas, aumente potência por métodos não suportados, substitua firmware de rádio, simule uma estação-base celular nem encaminhe tráfego de terceiros sem arquitetura e consentimento explícitos.

## Lista de verificação internacional

Antes de cada release ou expansão de mercado, registre:

- Países e territórios onde o build será distribuído, anunciado ou terá suporte.
- Se o produto é somente software, vendido junto com hardware ou parte de um serviço de conectividade.
- Interfaces de rádio e padrões fornecidos pelo sistema/hardware; o código do app não deve pressupor comportamento de RF personalizado.
- Requisitos de segurança do produto, compatibilidade eletromagnética, equipamento de rádio, marcação de conformidade, instruções, dados do importador e avisos traduzidos.
- Papéis de privacidade (controlador/operador), categorias de dados, finalidades, retenção, exclusão, direitos dos usuários, privacidade infantil quando pertinente e resposta a incidentes.
- Obrigações de cibersegurança, canal para vulnerabilidades, política de versões suportadas, inventário de componentes (SBOM) e autenticidade de atualizações.
- Controles de exportação, sanções, regras de criptografia, lojas de aplicativos, avisos sobre pagamentos/doações, proteção do consumidor e acessibilidade.
- Registro datado das evidências com regra oficial, categoria de produto aplicável, responsável pela decisão e data da última revisão.

Nunca confie em uma afirmação genérica de que “Wi-Fi é isento de licença”; condições de espectro e potência variam conforme faixa, país, equipamento e uso.

## Pontos de partida oficiais por mercado

Estes links são pontos de partida para pesquisa, não uma afirmação de que as regras listadas cobrem todos os produtos ou países.

| Mercado | Ponto de partida oficial | O que investigar |
|---|---|---|
| Estados Unidos | [Autorização de equipamentos da FCC](https://www.fcc.gov/oet/ea/rfdevice) | Se um dispositivo ou rádio modificado exige autorização; separar software de obrigações do hardware RF |
| União Europeia / EEE | [Diretiva de Equipamentos de Rádio (RED)](https://single-market-economy.ec.europa.eu/sectors/electrical-and-electronic-engineering-industries-eei/radio-equipment-directive-red_en) | Aplicabilidade a equipamentos colocados no mercado, avaliação de conformidade, normas harmonizadas, instruções e marcação CE |
| Grã-Bretanha | [Orientação sobre Radio Equipment Regulations 2017](https://www.gov.uk/government/publications/radio-equipment-regulations-2017/radio-equipment-regulations-2017-great-britain) | Requisitos atuais para equipamentos de rádio no mercado da Grã-Bretanha; verificar a Irlanda do Norte separadamente |
| Canadá | [Recursos de certificação e engenharia da ISED](https://ised-isde.canada.ca/site/certification-engineering-bureau/en) | Normas de equipamento de rádio, certificação, marcação e condições de uso sem licença aplicáveis |
| Austrália | [Normas e equipamentos da ACMA](https://www.acma.gov.au/standards-and-equipment) | Normas aplicáveis, registros de conformidade, obrigações de fornecedores e marcações |
| Índia | [Department of Telecommunications — Equipment Type Approval](https://eservices.dot.gov.in/equipment-type-approval-eta) | Requisitos de ETA para produtos que operam em determinadas faixas sem licença e categoria exata do equipamento |
| República da Coreia | [Avaliação de conformidade da National Radio Research Agency](https://www.rra.go.kr/en/cas/intro.do) | Se é necessária certificação, registro ou outra via de avaliação de conformidade |
| Todos os outros países e territórios | Regulador oficial de telecomunicações/rádio e autoridade de fiscalização de mercado/segurança do produto do destino | Bandas, limites, aprovações, importação, idioma, privacidade e condições de distribuição locais |

Acordos de reconhecimento mútuo podem simplificar alguns ensaios ou processos de aceitação, mas não presuma que a certificação de um país autoriza o produto em todos os mercados. Confirme os requisitos aplicáveis no país de destino.

## Privacidade, cibersegurança e distribuição de software

Conformidade de rádio é apenas uma parte da preparação internacional. Revise os regimes de proteção de dados aplicáveis segundo a localização das pessoas, os dados coletados e quem os controla. Exemplos potencialmente relevantes, conforme o caso, incluem GDPR da União Europeia, legislação de proteção de dados do Reino Unido, leis de privacidade da Califórnia, LGPD do Brasil e leis de outras jurisdições. Esta lista é inicial, não uma conclusão sobre aplicabilidade.

Para o protótipo atual:
- A descoberta LAN expõe nome do par, endereço IP, porta e metadados públicos de capacidade aos dispositivos participantes.
- Descoberta não é autenticação; o endpoint TCP de metadados não é criptografado.
- Não adicione análise de uso ou envio remoto de detalhes dos pares locais sem revisão de privacidade separada e desenho explícito.
- Não coloque conteúdo de arquivos, credenciais, tokens ou dados pessoais em registros TXT do mDNS.
- Qualquer transferência ou roteamento futuro exige modelagem de ameaças, consentimento, padrões seguros e tratamento de incidentes.
- Mantenha uma página de release clara sobre sistemas suportados, limitações, privacidade e forma de reportar vulnerabilidades.

## Critério para lançamento internacional

Um mercado não é “suportado” apenas porque a tradução existe ou o programa inicia. Antes de adicionar um país à lista de mercados suportados, conclua e date a revisão regulatória/privacidade aplicável, valide o canal de distribuição, teste idioma e suporte local e verifique se o desenho do software respeita eventuais restrições daquele país.

## Normas técnicas relacionadas

- [Autorização de equipamentos da FCC](https://www.fcc.gov/oet/ea/rfdevice)
- [Diretiva Europeia de Equipamentos de Rádio](https://single-market-economy.ec.europa.eu/sectors/electrical-and-electronic-engineering-industries-eei/radio-equipment-directive-red_en)
- [Regras de equipamento de rádio do Reino Unido](https://www.gov.uk/government/publications/radio-equipment-regulations-2017/radio-equipment-regulations-2017-great-britain)
- [Diretrizes de Acessibilidade para Conteúdo Web 2.2 do W3C](https://www.w3.org/Translations/WCAG22-pt-BR/)
