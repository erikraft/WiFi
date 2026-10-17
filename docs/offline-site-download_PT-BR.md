# Baixar sites para uso offline

O navegador Offline do Android pode baixar uma cópia estática limitada de um site HTTPS para o armazenamento privado do app. Com uma página aberta, toque em **Baixar site offline**. O downloader segue links HTML da mesma origem, salva recursos estáticos vinculados (incluindo recursos externos referenciados), reescreve referências HTML/CSS compatíveis para arquivos locais e adiciona o site à Biblioteca offline. O controle de voltar navega para a página anterior; o seletor de idioma usa os 16 idiomas offline da interface do app.

## Escopo e limites

- O rastreador visita no máximo 60 páginas HTML e salva até 500 recursos vinculados, com limite total de 80 MiB e limite de 20 MiB por resposta.
- O resultado é uma cópia estática, não um navegador completo nem um servidor. Conteúdo de APIs dinâmicas, rotas renderizadas por JavaScript, formulários POST, logins, cookies, DRM, service workers, WebSockets e ações no servidor não têm garantia de funcionar offline.
- Variações de URL que usam parâmetros de consulta podem ser salvas no mesmo caminho local. Sites que dependem desses parâmetros para identificar páginas podem não ser arquivados corretamente.
- Recursos estáticos externos referenciados no HTML/CSS são copiados quando reconhecidos; solicitações feitas em tempo de execução por scripts e tipos de recurso não reconhecidos ainda podem exigir Internet.
- O recurso não contorna controles de acesso. Baixe apenas páginas que você tem autorização para acessar e salvar.
- O download terminar com sucesso não significa que todas as páginas e interações funcionarão completamente offline. Teste o site salvo com o modo avião ativado no dispositivo de destino.

Este recurso é um downloader de site estático limitado, não roteamento mesh multi-hop nem gateway de Internet.
