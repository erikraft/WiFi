# Integração com ErikrafT Drop™

Alvos: [Drop Web/Desktop](https://github.com/erikraft/Drop) e [Drop Android](https://github.com/erikraft/Drop-Android).

## Estado atual
A integração ainda não é funcional. O app Android registra o deep link erikraftwifi://open como ponto de entrada exploratório; isso não cria uma API de dados nem uma sessão segura.

## Próximas etapas
- Definir contrato de API/IPC versionado e modelo de permissões.
- Fazer Drop consultar capacidades e disponibilidade sem presumir Wi-Fi Direct ou Mesh.
- Exigir consentimento e validar a origem antes de transferências.
- Reutilizar o mecanismo de transferência do Drop quando possível, evitando duplicar lógica de arquivos.
- Testar Web/Desktop e Android separadamente e documentar fallbacks.