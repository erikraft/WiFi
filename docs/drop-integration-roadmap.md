# ErikrafT Wi-Fi™ ↔ ErikrafT Drop™ integration roadmap

## Product boundaries

ErikrafT Wi-Fi should own **network presence and connectivity capabilities**. ErikrafT Drop should remain the owner of **user-initiated file transfer and its existing transfer/session UX**. The projects should not grow duplicate file-transfer engines or treat finding a device as consent to send files.

Targets:
- [Drop Web/Desktop](https://github.com/erikraft/Drop)
- [Drop Android](https://github.com/erikraft/Drop-Android)

## Current state

- The Android Wi-Fi client registers `erikraftwifi://open` as an entry point.
- The Android prototype advertises and discovers nodes using `_erikraftwifi._tcp.` and protocol version 1.
- The desktop prototype is being built around the same public-metadata contract.
- Discovery metadata is unauthenticated and unencrypted.
- A working, versioned Drop API/IPC contract and secure transfer handoff do not exist yet.

## Recommended responsibilities

| Component | Owns | Must not claim |
|---|---|---|
| ErikrafT Wi-Fi on Android | Android NSD discovery, node metadata, Wi-Fi-specific capability checks | Secure file transfer or mesh/gateway support before implementation |
| ErikrafT Wi-Fi Desktop | Desktop mDNS discovery and local metadata endpoint | Equivalent Android parity until it is tested on Windows, Linux, and macOS |
| Drop Android | Existing Android file-transfer workflows, user consent, file access, transfer status | Trusting a Wi-Fi discovery result without validating it |
| Drop Web/Desktop | Existing web/desktop transfer workflows and supported networking transports | Using a desktop local-network peer as an authenticated identity |
| Shared integration contract | Versioned capability data and an explicit handoff | Secrets or file contents inside discovery metadata or deep links |

## Staged plan

### Stage 1 — Contract inventory
- Inspect current Drop Web/Desktop and Drop Android code paths first.
- Identify which peer discovery, pairing, transport, lifecycle, and transfer APIs already exist.
- Reuse established contracts where possible; do not create a parallel protocol before understanding them.
- Record platform differences and their test coverage.

### Stage 2 — Versioned discovery metadata
- Specify a stable schema with protocol version, application identity, supported capabilities, and compatibility version.
- Treat metadata as untrusted input; bound payload sizes, reject malformed values, and keep logs free of personal data.
- Add tests for unknown versions, duplicate instances, conflicting names, lost services, stale endpoints, and malformed JSON.
- Do not publish user names, filenames, credentials, session tokens, or private network data beyond what discovery requires.

### Stage 3 — Explicit handoff into Drop
- Let the user choose a discovered peer and deliberately open the relevant Drop surface.
- Keep the deep link as a navigation handoff, not an authorization token. Do not put credentials or file contents in the URI.
- Revalidate the peer in Drop and show the actual target and requested operation before sending.
- If Drop cannot safely consume the capability information, fall back to its current supported pairing/transfer flow.

### Stage 4 — Authenticated session
- Define peer identity, mutual authentication, cryptographic session establishment, replay protection, timeout rules, and cancellation.
- Require consent in Drop at the moment of transfer.
- Test cross-platform compatibility, hostile peers, reconnects, app suspension, and interrupted transfers.

### Stage 5 — Optional transport reuse
- Evaluate whether Drop's current transport can be reused without bypassing encryption, pairing, quotas, or transfer state.
- Only consider Wi-Fi Direct or multi-hop routes after the security and network architecture is reviewed separately.
- Preserve existing Drop fallbacks; a Wi-Fi-specific path must not break normal LAN, WebRTC, room, or other already-supported flows.

## Acceptance criteria

Integration is not complete until the contract is documented and versioned, both Drop codebases are reviewed, user consent and authentication are verified, and automated plus physical-device tests cover Android-to-Android, desktop-to-Android, and supported desktop combinations. A registered deep link alone is not integration.
