# Android UI, diagnostics, and localization

## Design direction

The Android client should adopt a polished native dark interface inspired by
the information hierarchy and restraint of modern peer-to-peer apps, while
keeping ErikrafT Wi-Fi's own blue identity (`#4285F4`). Reference projects in
`References/` are third-party and are not official ErikrafT products.

## Launcher icon

Android 8.0+ uses an adaptive launcher icon with separate foreground and
background layers. Android 13+ can use the monochrome layer for themed icons
when the launcher and user settings support it. The OS controls the final
themed tint; an app cannot force every launcher to use arbitrary theme colors.

## Diagnostics and settings

Diagnostics must report observed device/API/network state and implementation
status separately. They must not describe LAN discovery as authenticated,
encrypted, mesh-routed, or as a file-transfer feature. Unsupported capabilities
must be labeled as not implemented.

## Localization

A locale picker alone does not constitute a translation. Every visible string
must use Android resources, and each locale must have reviewed translations
before it is advertised as fully supported. Missing translations should fall
back to the default resource set without crashing. The current Java prototype
still contains hard-coded Portuguese strings; full extraction and translation
coverage is a separate required implementation step.
