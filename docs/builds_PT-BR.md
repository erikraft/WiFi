# Builds e assinatura

## Android: APK e AAB

O workflow `.github/workflows/android-release.yml` gera APK e Android App Bundle (AAB) em execução manual (**Actions → Android APK and AAB → Run workflow**) ou quando uma tag `v*` é publicada. O workflow usa Java 17, Android SDK 36 e Gradle 8.13.

### Assinatura de release com keystore em Base64

A chave privada não deve ser commitada no repositório, colocada no código ou enviada em logs. Em **Settings → Secrets and variables → Actions**, configure estes quatro repository secrets:

- `ANDROID_KEYSTORE_BASE64`: conteúdo Base64 do arquivo keystore JKS/PKCS12, em uma única linha.
- `ANDROID_KEY_ALIAS`: alias da chave dentro do keystore.
- `ANDROID_KEYSTORE_PASSWORD`: senha do keystore.
- `ANDROID_KEY_PASSWORD`: senha da chave.

Exemplo local para codificar o arquivo sem inserir a chave no Git:

```bash
base64 -w 0 erikraft-wifi-release.jks
```

No macOS, se o `base64` não aceitar `-w 0`, use:

```bash
base64 < erikraft-wifi-release.jks | tr -d '\\n'
```

Copie a saída diretamente para o secret `ANDROID_KEYSTORE_BASE64`. Não cole a keystore, senha ou chave privada em Issues, PRs, arquivos YAML ou no chat. Guarde uma cópia de segurança offline: perder a chave de assinatura pode impedir atualizações compatíveis da aplicação publicada.

Se os quatro secrets estiverem configurados, o workflow assina e verifica o APK e assina/verifica o AAB. Se estiverem ausentes, ele ainda gera artefatos de release **não assinados**, com aviso explícito; eles não devem ser distribuídos como versão final. Pull requests de forks não recebem secrets do repositório, por segurança.

A assinatura só prova a identidade da chave que assinou o pacote; não prova que o app foi testado em todos os dispositivos. Para Google Play, siga também o fluxo de Play App Signing.

Referências:
- Android App Signing: https://developer.android.com/studio/publish/app-signing
- GitHub Actions secrets: https://docs.github.com/en/actions/security-for-github-actions/security-guides/using-secrets-in-github-actions

## Windows, Linux e macOS

O repositório atual contém um aplicativo **Android nativo Java** e uma landing page estática. Ainda não existe um cliente desktop nativo ou multiplataforma, nem projeto Electron/Tauri/Qt, nem lógica desktop para descoberta e conexão. Portanto, criar um workflow que alegue produzir um executável funcional `.exe`, AppImage/`.deb` ou `.dmg` seria enganoso.

Para gerar executáveis de verdade, primeiro é necessário decidir e implementar um cliente desktop reutilizando um protocolo comum e seguro. Depois disso, adicionar builds separados em runners `windows-latest`, `ubuntu-latest` e `macos-latest`, com testes e empacotamento apropriados. Até essa etapa, o workflow deste repositório gera artefatos Android, e não executáveis desktop.

## Limites dos builds

- O build automatizado de PR compila o APK debug; não substitui testes físicos Android.
- O workflow de release não comprova descoberta mDNS, Wi-Fi Direct, mesh, acesso à Internet ou integração com Drop.
- Não publicar builds experimentais como estáveis sem testar instalação, assinatura, permissões e comportamento em aparelhos reais.
