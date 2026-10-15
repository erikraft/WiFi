# Arquitetura inicial

## Implementado nesta etapa
- Landing page estática responsiva em azul.
- App Android nativo com interface inicial e deep link `erikraftwifi://open`.
- Descoberta experimental de nós na mesma rede local usando Android NSD/DNS-SD (mDNS).
- Endpoint TCP efêmero que responde apenas metadados públicos de protocolo/capacidades; não recebe nem envia arquivos.
- Workflow de CI que tenta compilar o APK debug e workflow manual/tag para APK/AAB Android.

## Ainda não implementado
- Wi-Fi Direct / Wi-Fi P2P.
- Mesh routing, encaminhamento entre nós e gateways para Internet.
- Autenticação de pares, canal criptografado e transferência segura.
- Integração funcional com Drop Web/Desktop e Drop Android. O deep link é apenas um ponto de entrada.
- Cliente desktop executável para Windows, Linux e macOS.
- Suporte a iOS ou qualquer capacidade de operar como estação-base de rede celular.

## Compatibilidade e limites
O app pode rodar em aparelhos que também tenham 2G/3G/4G/5G, mas isso não faz com que o app forneça essas redes celulares. O tráfego via Internet precisa de uma rota para um gateway conectado. Sem gateway, nós podem oferecer apenas conectividade local quando o transporte correspondente estiver implementado.

A implementação deve utilizar APIs oficiais do sistema e rádios existentes, sem alterar potência, frequências ou firmware. Para o contexto regulatório brasileiro, leia [platform-compatibility.md](platform-compatibility.md).

## Direção técnica
1. Usar APIs de rede existentes; não inventar protocolo de rádio.
2. Separar descoberta, identidade de nó, sessão autenticada e transporte.
3. Definir capacidades por plataforma, pois Android, iOS e desktop diferem.
4. Nunca tratar presença na mesma LAN como prova de confiança.
5. Adicionar testes de protocolo e revisão de segurança antes de habilitar transferência.
6. Não marcar funcionalidade como concluída apenas porque existe um workflow ou uma interface; exigir testes em hardware e evidências.
