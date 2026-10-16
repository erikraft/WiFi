# Site responsivo e experiência de doação

## Objetivos

O site público deve explicar o estado real do ErikrafT Wi-Fi, oferecer links para documentação técnica e funcionar em desktop, tablet e celular sem inventar controles nem sugerir que recursos incompletos estão disponíveis.

Preserve a direção visual existente:
- Azul principal: `#4285F4`, alinhado à identidade visual do app Android.
- Fundo escuro da landing page, detalhes de grade azul e tipografia DM Sans e Space Mono.
- Preserve a ilustração de dispositivos existente, mas oculte-a preventivamente em telas estreitas, touch-first ou com pouca altura, onde possa se sobrepor ou distrair.
- Mantenha a troca entre inglês e português brasileiro e traduções para as novas seções.

## Requisitos responsivos

- Desktop: use a largura disponível para o texto do hero e a ilustração existente somente quando houver espaço suficiente.
- Tablet: reduza progressivamente as colunas dos cards; navegação, idioma, cartões de doação e links de documentação não podem se sobrepor.
- Mobile: empilhe cards, informações da carteira e QR codes; permita quebra de linhas nos endereços e descrições; mantenha alvos de toque adequados e evite rolagem horizontal da página.
- Viewport semelhante ao Xiaomi Redmi 9C: teste um viewport CSS em torno de 360 px de largura e cenários de paisagem com pouca altura. Resolução física e densidade do navegador podem mudar o viewport CSS; teste capturas reais em vez de tratar uma dimensão como universal.
- Navegadores touch-first: não mostre a cena decorativa com telefone/notebook no site público. A cena é ilustrativa, não uma prévia interativa dos dispositivos.

## Seção de documentação

Mostre uma área dedicada à documentação com links em inglês e arquivos `_PT-BR.md`. Priorize clientes desktop, visão regulatória global, roadmap de integração com Drop, orientação responsiva, arquitetura e segurança. Se um documento for apenas um plano, o título e o texto devem identificá-lo como plano.

## QR Codes para doação em Bitcoin

- Gere o QR a partir do endereço visível como URI de pagamento Bitcoin: `bitcoin:<endereço>`. Não preencha valor automaticamente sem solicitação explícita e revisão.
- Use os endereços de carteira já publicados; não invente nem altere endereços silenciosamente.
- Use uma dependência de geração de QR com versão fixada em vez de um serviço remoto de imagens. Mantenha endereço e botão de cópia visíveis quando a geração do QR falhar.
- Incorpore o logotipo SVG de Bitcoin existente no centro do QR, sobre uma área branca sólida. Use correção de erro alta e deixe módulos livres suficientes ao redor do símbolo.
- Valide o QR gerado conferindo o conteúdo da URI em várias carteiras e aparelhos reais. A sobreposição de um logo pode dificultar a leitura; renderizar sem erro não é validação.
- A URI só abre uma carteira compatível quando o sistema ou navegador possui um manipulador registrado. O endereço visível é a alternativa.
- Avise que transações Bitcoin são irreversíveis e que o usuário precisa confirmar o destinatário e a rede antes de prosseguir.

O formato de URI `bitcoin:` está descrito nos documentos de URI Bitcoin; consulte a [especificação BIP 321 atual](https://github.com/bitcoin/bips/blob/master/bip-0321.mediawiki) e o [guia de pagamentos para desenvolvedores Bitcoin](https://developer.bitcoin.org/devguide/payment_processing.html).

## Acessibilidade e conteúdo

- Use WCAG 2.2 AA como objetivo de engenharia; combine verificações automatizadas com testes manuais por teclado, leitor de tela, zoom e movimento reduzido.
- Mantenha foco visível, seções semanticamente agrupadas, links descritivos, metadados de idioma, contraste e texto alternativo.
- Nunca dependa somente de cor para indicar estados experimental/planejado.
- Indique claramente se um recurso está implementado, experimental, em desenvolvimento ou planejado.
- Mantenha os links válidos e confira capturas em desktop, tablet, celular, telas estreitas (320–380 px) e pouca altura antes de mudar as afirmações do release.

Referência: [W3C WCAG 2.2 em português](https://www.w3.org/Translations/WCAG22-pt-BR/).
