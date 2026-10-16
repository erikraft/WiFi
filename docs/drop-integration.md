# ErikrafT Drop™ integration

Targets: [ErikrafT Drop™ for Web/Desktop/...](https://github.com/erikraft/Drop) and [ErikrafT Drop™ for Android](https://github.com/erikraft/Drop-Android).

## Current status
Integration is not functional yet. The Android app registers the `erikraftwifi://open` deep link as an exploratory entry point; this does not create a data API or a secure session.

## Next steps
- Define a versioned API/IPC contract and permission model.
- Have ErikrafT Drop™ query capabilities and availability without assuming Wi-Fi Direct or mesh support.
- Require user consent and validate the origin before transfers.
- Reuse ErikrafT Drop™'s transfer mechanism where possible instead of duplicating file-transfer logic.
- Test Web/Desktop and Android separately and document fallback behavior.
