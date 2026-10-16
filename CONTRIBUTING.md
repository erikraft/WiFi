# Contributing to ErikrafT Wi-Fi

Thank you for your interest in contributing to ErikrafT Wi-Fi! Contributions can include bug reports, documentation, translations, tests, accessibility improvements, and code.

## Before you start

1. Search existing [issues](https://github.com/erikraft/WiFi/issues) and pull requests to avoid duplicate work.
2. For substantial changes, open an issue or discussion first so the approach can be agreed upon.
3. Keep changes focused. Avoid unrelated refactors, new UI invented without a clear need, and rewrites when the existing implementation can be extended.
4. Be explicit about what is implemented, what is experimental, and what remains a proposal. Do not describe planned networking features as available.

## Development setup

- Use a compatible Android Studio installation and the JDK/Gradle versions specified by the repository's Gradle configuration.
- Open the repository's `app/` project in Android Studio, allow Gradle sync to finish, and build the debug variant.
- Never commit signing keys, keystores, passwords, API tokens, private keys, local machine configuration, or personal data.

Check the README and the build documentation in `docs/` for repository-specific instructions. If setup instructions are missing or outdated, please report that.

## Issues and bug reports

When filing a bug, include as much of the following as is safe and relevant:

- A clear title and concise description.
- Exact steps to reproduce and the expected versus actual behavior.
- Android version, device model, app version, and relevant environment details.
- Relevant logs with secrets, tokens, personal information, and identifying data removed.
- Whether the issue is reproducible and whether it affects a release or only a development build.

Do not post security vulnerabilities in public issues. Follow [SECURITY.md](SECURITY.md) instead.

## Pull requests

- Create a focused branch from `main`; do not bundle unrelated issues into one PR.
- Explain the problem, the approach, user-visible effects, and any limitations in the PR description.
- Link related issues and include screenshots or recordings when they materially help explain a UI change.
- Update documentation when behavior, setup, supported devices, permissions, or limitations change.
- Preserve existing functionality and responsive behavior unless the change explicitly requires otherwise.
- Do not add dependencies without a clear need. Prefer maintained, reputable upstream projects and pinned versions; never use floating `latest` versions.
- Do not copy code or assets without checking their license and attribution requirements.
- Do not claim mesh routing, offline calls, encryption, peer authentication, or other security/networking capabilities unless they are implemented and appropriately tested.

## Validation

Before requesting review, run the checks relevant to your change:

- Build the Android debug variant.
- Run available automated tests and repository validation workflows.
- Validate changed XML, HTML, JavaScript, and documentation as applicable.
- Test on a real device when hardware, Android-version behavior, permissions, networking, or WebView behavior is involved.
- For networking changes, clearly document what was tested: same device, two devices on one LAN, no Internet, multiple Android versions, and so on.

If you cannot run a check, say so in the PR. Do not report a check as passing unless it actually completed successfully.

## Review and merge

Maintainers may ask for changes, additional tests, or clarification. Please keep discussion respectful and respond to review feedback constructively. A pull request may be declined if it is unsafe, out of scope, difficult to maintain, or lacks enough validation.

By participating, you agree to follow the [Code of Conduct](CODE_OF_CONDUCT.md).

## License

By submitting a contribution, you confirm that you have the right to submit it under the license applicable to the project. Unless the repository states otherwise, your contribution is provided under the same license as the project; this does not transfer ownership of your work to the maintainers.
