# Responsive website and donation experience

## Goals

The public site should explain the real status of ErikrafT Wi-Fi, link to technical documentation, and work on desktop, tablet, and mobile without inventing controls or implying unfinished features are available.

Keep the existing brand direction:
- Primary blue: `#4285F4`, matching the Android app's visual identity.
- Existing dark landing-page background, blue grid accents, DM Sans and Space Mono typography.
- Preserve the current device illustration but fail closed on narrow, touch-first, or short viewports where it could collide or distract.
- Keep language switching between English and Brazilian Portuguese and ensure new sections have translations.

## Responsive layout requirements

- Desktop: use available width for hero copy and the existing illustration only when the scene has enough room.
- Tablet: collapse multi-column cards progressively; keep navigation, language switch, donation cards, and documentation links from colliding.
- Mobile: stack cards, wallet information, and QR codes; allow addresses and descriptions to wrap; keep tap targets large enough and avoid horizontal page scrolling.
- Xiaomi Redmi 9C-like viewport: test a CSS viewport around 360 px wide and short landscape sizes as representative conditions. Physical resolution and browser density can change the CSS viewport, so test actual screenshots rather than treating one dimension as universal.
- Touch-first browsers: do not show the decorative phone/laptop scene on the public website. This scene is illustrative only, not an interactive device preview.

## Documentation section

Show a dedicated documentation area with English and `_PT-BR.md` links. Prioritize desktop clients, global regulatory overview, Drop integration roadmap, responsive web guidance, architecture, and security. If a document is only a plan, its page title and copy must describe it as a plan.

## Bitcoin donation QR codes

- Generate the QR from the visible address as a Bitcoin payment URI: `bitcoin:<address>`. Do not prefill an amount unless an explicit amount has been requested and reviewed.
- Use the currently published wallet addresses; do not synthesize or silently change addresses.
- Use a pinned QR-generation dependency instead of a remote image service. Keep the address and copy control visible when QR rendering fails.
- Embed the existing Bitcoin SVG logo in the centre of the QR on a solid white patch. Use the high error-correction setting and leave enough clear modules around the centre mark.
- Validate the generated QR against the URI payload on multiple wallet apps and physical devices. A logo overlay can make a QR harder to scan; successful rendering alone is not validation.
- The URI opens a compatible wallet only when the operating system or browser has a handler. The visible address is the fallback.
- Warn that Bitcoin transactions are irreversible and the user must verify the recipient address and network before confirming.

The `bitcoin:` URI format is specified by the Bitcoin URI documents; see the current [BIP 321 specification](https://github.com/bitcoin/bips/blob/master/bip-0321.mediawiki) and the [Bitcoin developer payment-processing guide](https://developer.bitcoin.org/devguide/payment_processing.html).

## Accessibility and content

- Target WCAG 2.2 AA as an engineering goal; assess automated checks and manual use with keyboard, screen reader, zoom, and reduced motion.
- Keep visible focus styles, semantically grouped sections, descriptive link labels, language metadata, text contrast, and alt text.
- Never rely on colour alone for experimental/planned statuses.
- State whether a capability is implemented, experimental, in progress, or planned.
- Keep all docs links valid, and check desktop, tablet, mobile, narrow (320–380 px), and short-height screenshots before changing release claims.

Reference: [W3C WCAG 2.2](https://www.w3.org/TR/WCAG22/).
