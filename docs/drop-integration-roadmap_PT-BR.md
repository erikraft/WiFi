# Roadmap de integração ErikrafT Wi-Fi™ ↔ ErikrafT Drop™

## Limites dos produtos

ErikrafT Wi-Fi deve ser responsável por **presença na rede e capacidades de conectividade**. ErikrafT Drop deve continuar responsável pela **transferência de arquivos iniciada pelo usuário e pela experiência de sessão/transferência já existente**. Os projetos não devem criar motores duplicados de transferência nem tratar a descoberta de um dispositivo como consentimento para enviar arquivos.

Alvos:
- [Drop Web/Desktop](https://github.com/erikraft/Drop)
- [Drop Android](https://github.com/erikraft/Drop-Android)

## Estado atual

- O cliente Wi-Fi Android registra `erikraftwifi://open` como ponto de entrada.
- O protótipo Android anuncia e descobre nós usando `_erikraftwifi._tcp.` e a versão 1 do protocolo.
- O protótipo desktop está sendo construído em torno do mesmo contrato de metadados públicos.
- Os metadados de descoberta não são autenticados nem criptografados.
- Ainda não existe um contrato Drop API/IPC versionado e funcional nem uma passagem segura para transferência.

## Responsabilidades recomendadas

| Componente | Responsável por | Não deve afirmar |
|---|---|---|
| ErikrafT Wi-Fi para Android | Descoberta NSD, metadados do nó e verificações de capacidades específicas do Wi-Fi | Transferência segura ou suporte a mesh/gateway antes de implementados |
| ErikrafT Wi-Fi Desktop | Descoberta mDNS no desktop e endpoint local de metadados | Paridade com Android antes de testes em Windows, Linux e macOS |
| Drop Android | Fluxos existentes de transferência Android, consentimento, acesso a arquivos e estado da transferência | Confiar em um resultado da descoberta sem validá-lo |
| Drop Web/Desktop | Fluxos atuais de transferência web/desktop e transportes suportados | Tratar um par descoberto na rede local como identidade autenticada |
| Contrato compartilhado | Dados de capacidades versionados e passagem explícita entre apps | Segredos ou conteúdo de arquivos nos metadados de descoberta ou deep links |

## Plano por etapas

### Etapa 1 — Inventário de contratos
- Inspecionar primeiro os caminhos atuais de código do Drop Web/Desktop e Drop Android.
- Identificar quais APIs de descoberta, pareamento, transporte, ciclo de vida e transferência já existem.
- Reutilizar contratos estabelecidos quando possível; não criar protocolo paralelo antes de entendê-los.
- Registrar diferenças entre plataformas e cobertura dos testes.

### Etapa 2 — Metadados de descoberta versionados
- Especificar um esquema estável com versão de protocolo, identidade do app, capacidades suportadas e versão de compatibilidade.
- Tratar os metadados como entrada não confiável; limitar tamanho, rejeitar valores malformados e manter dados pessoais fora dos logs.
- Criar testes para versões desconhecidas, instâncias duplicadas, nomes conflitantes, serviços perdidos, endpoints antigos e JSON inválido.
- Não publicar nomes de usuário, nomes de arquivos, credenciais, tokens de sessão ou dados privados além do necessário para descobrir o nó.

### Etapa 3 — Passagem explícita para o Drop
- Permitir que o usuário escolha um par descoberto e abra deliberadamente a interface adequada do Drop.
- Manter o deep link como navegação, não como token de autorização. Não colocar credenciais ou conteúdo de arquivo na URI.
- Revalidar o par no Drop e mostrar o destino e a operação antes do envio.
- Se o Drop não puder usar com segurança os dados de capacidades, voltar ao fluxo atual de pareamento/transferência suportado.

### Etapa 4 — Sessão autenticada
- Definir identidade dos pares, autenticação mútua, estabelecimento de sessão criptográfica, proteção contra replay, timeouts e cancelamento.
- Exigir consentimento no Drop no momento da transferência.
- Testar compatibilidade entre plataformas, pares hostis, reconexões, suspensão do app e transferências interrompidas.

### Etapa 5 — Reutilização opcional de transporte
- Avaliar se o transporte atual do Drop pode ser reutilizado sem contornar criptografia, pareamento, cotas ou estado da transferência.
- Só considerar Wi-Fi Direct ou rotas multi-hop depois de revisar separadamente arquitetura de segurança e rede.
- Preservar os fallbacks existentes do Drop; um caminho específico de Wi-Fi não pode quebrar os fluxos LAN, WebRTC, salas ou outras funções já suportadas.

## Critérios de aceitação

A integração só estará concluída quando o contrato estiver documentado e versionado, as duas bases do Drop forem revisadas, consentimento e autenticação forem verificados e testes automatizados e em dispositivos reais cobrirem Android-Android, desktop-Android e combinações desktop suportadas. Registrar um deep link, sozinho, não constitui integração.
