# Security Policy

ErikrafT Wi-Fi is under active development. Some networking and offline features may be experimental or not implemented. Do not assume that peer discovery, a local-network connection, or a visualized peer implies authentication, end-to-end encryption, or secure mesh routing.

## Reporting a vulnerability

**Please do not disclose suspected vulnerabilities in public issues, pull requests, discussions, or social media.**

Report a vulnerability privately through GitHub's security reporting feature for this repository, if it is enabled: open the repository's **Security** tab and select **Report a vulnerability**. If private vulnerability reporting is unavailable, contact the repository maintainer privately through GitHub and ask for a secure reporting channel. Do not include exploit details in a public message.

Please include, when possible:

- A concise description of the potential impact.
- Affected app version, commit, Android version, and device model.
- Reproduction steps or a minimal proof of concept.
- Logs or screenshots with personal data and secrets removed.
- Any suggested mitigation, if known.

You may report without a proposed fix. Please allow maintainers reasonable time to investigate and coordinate a fix before public disclosure. We will make a good-faith effort to acknowledge reports, investigate them, and communicate status, but no response or resolution deadline is guaranteed.

## Scope and examples

Examples of security-relevant reports include:

- Unauthorized access to local files or offline archives.
- Unsafe handling of imported HTML/MHTML, shared content, URLs, or intents.
- Exposure of private information, credentials, or sensitive logs.
- Network services accepting unintended connections or leaking data.
- Missing authorization or authentication in peer communication.
- Cryptographic implementation errors or claims of encryption that do not match actual behavior.
- Vulnerabilities in dependencies used by this repository.

Reports about unsupported features should explain the security impact of existing code. A feature that has not been implemented is not, by itself, a vulnerability.

## Safe testing

Only test systems and devices you own or have explicit permission to assess. Avoid destructive testing, denial of service, accessing other people's data, or actions that could disrupt networks or services. Do not upload real credentials, private keys, or sensitive user data to issues or test artifacts.

## Security limitations

- A device discovered through NSD/mDNS is not thereby authenticated or trusted.
- The globe's peer positions are visual placements, not geographic locations or evidence of a secure mesh.
- LAN discovery does not itself provide encrypted messaging, end-to-end encryption, or secure multi-hop routing.
- Offline archive compatibility and imported-file behavior depend on Android and WebView behavior.

Please review the current code and documentation rather than assuming a feature is secure because it is planned or mentioned in a roadmap.
