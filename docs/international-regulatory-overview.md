# International regulatory and market-readiness overview

## Scope and disclaimer

ErikrafT Wi-Fi is an international software project. There is no single approval that automatically authorizes every Wi-Fi implementation, radio device, network service, or software release worldwide. Requirements depend on the country/territory, radio hardware, frequency band, power level, product category, import/export path, data processed, and whether the project is only software or includes equipment or a connectivity service.

This document is a practical engineering checklist with selected official entry points, **not legal advice and not an exhaustive country-by-country catalogue**. Before a commercial release in a new market, verify current rules with the local authority or qualified counsel. Do not infer legal permission from this README or from approval of an existing phone/router.

## Separate the product layers

1. **Software-only client:** ordinary local-network discovery, UI, telemetry choices, privacy notices, accessibility, consumer disclosures, software security, and distribution rules may apply.
2. **Software controlling existing certified radios:** use documented operating-system APIs and keep power, frequencies, firmware, and regulatory parameters under OS/hardware control. Device certification does not grant permission to bypass OS restrictions.
3. **New or modified radio hardware/firmware:** radio authorization, testing, conformity marking, spectrum, electromagnetic compatibility, exposure, importation, and labelling duties may apply before sale or use.
4. **Connectivity offered to other people:** operator/ISP licensing, public communications, lawful-intercept, emergency service, consumer protection, net neutrality, and data-retention issues may depend on the service model and jurisdiction.
5. **File-transfer or routing features:** data protection, cybersecurity, abuse reporting, cross-border data handling, and rules for handling user content must be assessed separately from radio approvals.

The current prototype should use existing Wi-Fi interfaces normally. Do not unlock licensed bands, raise transmit power through unsupported methods, replace radio firmware, impersonate a cellular base station, or forward third-party traffic without an explicit architecture and consent model.

## International engineering checklist

Before each release or market expansion, record:

- Countries and territories where the build will be distributed, advertised, or supported.
- Whether it is software-only, bundled with hardware, or part of a connectivity service.
- The exact radio interfaces and standards supplied by the OS/hardware; no custom RF behaviour should be inferred from app code.
- Any requirements for product safety, electromagnetic compatibility, radio equipment, conformity marking, user instructions, importer details, and translated disclosures.
- Privacy roles (controller/processor), data categories, purposes, retention, deletion, user rights, children’s privacy where relevant, and incident response.
- Cybersecurity obligations, vulnerability intake, supported-version policy, software bill of materials (SBOM), and update authenticity.
- Export controls, sanctions, cryptography rules, app-store rules, payment/donation disclosures, consumer law, and accessibility requirements.
- A dated evidence record with the official rule, applicable product type, decision owner, and last review date.

Never rely on a generic “Wi-Fi is licence-exempt” statement; spectrum and power conditions vary by band, country, equipment, and use.

## Selected official starting points by market

These are entry points for research, not a claim that the listed rules cover every product or every country.

| Market | Official starting point | What to investigate |
|---|---|---|
| United States | [FCC equipment authorization](https://www.fcc.gov/oet/ea/rfdevice) | Whether a device or modified radio requires equipment authorization; separate software obligations from RF hardware obligations |
| European Union / EEA | [Radio Equipment Directive (RED)](https://single-market-economy.ec.europa.eu/sectors/electrical-and-electronic-engineering-industries-eei/radio-equipment-directive-red_en) | Applicability to radio equipment placed on the market, conformity assessment, harmonised standards, instructions, and CE marking |
| Great Britain | [Radio Equipment Regulations 2017 guidance](https://www.gov.uk/government/publications/radio-equipment-regulations-2017/radio-equipment-regulations-2017-great-britain) | Current requirements for radio equipment placed on the Great Britain market; check Northern Ireland separately |
| Canada | [ISED certification and engineering resources](https://ised-isde.canada.ca/site/certification-engineering-bureau/en) | Radio equipment standards, certification, labelling, and any applicable licence-exempt operating conditions |
| Australia | [ACMA standards and equipment](https://www.acma.gov.au/standards-and-equipment) | Applicable equipment standards, compliance records, supplier obligations, and required markings |
| India | [Department of Telecommunications — Equipment Type Approval](https://eservices.dot.gov.in/equipment-type-approval-eta) | ETA requirements for products operating in relevant de-licensed bands and the exact equipment category |
| Republic of Korea | [National Radio Research Agency — conformity assessment](https://www.rra.go.kr/en/cas/intro.do) | Whether certification, registration, or another conformity-assessment route applies |
| All other countries and territories | The official telecommunications/radio regulator and market-surveillance/product-safety authority for each destination | Local bands, limits, approvals, import rules, language requirements, privacy law, and distribution conditions |

Mutual-recognition agreements can simplify some testing or acceptance procedures, but do not assume that one country's certification automatically authorizes a product in every market. Confirm the applicable destination-country requirements.

## Privacy, cybersecurity, and software distribution

Radio-equipment compliance is only one part of international readiness. Review applicable data-protection frameworks and local laws based on where people are located, what data is collected, and who controls it. Examples that may be relevant by scope include the EU GDPR, UK data-protection law, California privacy law, Brazil's LGPD, and privacy laws in other jurisdictions. This is a starting list, not a determination of applicability.

For this prototype:
- LAN discovery exposes a peer name, IP address, port, and public capability metadata to participating devices.
- Discovery is not authentication; the TCP metadata endpoint is not encrypted.
- Do not add analytics or remote reporting of local peer details without a separate privacy review and explicit design.
- Do not send file contents, credentials, tokens, or personal data in mDNS TXT records.
- Any future transfer or routing capability requires threat modelling, user consent, secure defaults, and incident handling.
- Maintain a clear release page describing supported operating systems, limitations, privacy behavior, and how to report vulnerabilities.

## International release gate

A market is not “supported” just because its language is translated or the software starts. Before adding a country to a supported-market list, complete and date the applicable regulatory/privacy review, validate the distribution channel, test local language and support content, and verify that no country-specific restriction is contradicted by the software design.

## Related technical standards

- [FCC equipment authorization](https://www.fcc.gov/oet/ea/rfdevice)
- [EU Radio Equipment Directive](https://single-market-economy.ec.europa.eu/sectors/electrical-and-electronic-engineering-industries-eei/radio-equipment-directive-red_en)
- [UK radio equipment rules](https://www.gov.uk/government/publications/radio-equipment-regulations-2017/radio-equipment-regulations-2017-great-britain)
- [W3C Web Content Accessibility Guidelines 2.2](https://www.w3.org/TR/WCAG22/)
