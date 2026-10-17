# Offline website download

The Android Offline browser can download a bounded static copy of an HTTPS website into the app's private storage. Use **Download website offline** while a page is open. The downloader follows same-origin HTML links, stores linked static assets (including referenced external assets), rewrites supported HTML/CSS references to local files, and adds the saved site to the Offline Library. The back control navigates to the previous page; the language picker uses the app's 16 offline UI languages.

## Scope and limits

- The crawler visits at most 60 HTML pages and stores at most 500 linked resources, with an 80 MiB total download budget and a 20 MiB per-response limit.
- It downloads a static snapshot, not a full browser profile or server. Dynamic API content, JavaScript-rendered routes, POST forms, logins, cookies, DRM, service workers, WebSockets, and server-side actions cannot be guaranteed to work offline.
- Query-string variants can map to the same local path. Sites that depend on query parameters for page identity may not archive correctly.
- External static assets referenced by the saved HTML/CSS are copied when recognized; runtime requests made by scripts and unrecognized resource types may still need Internet.
- The feature does not bypass access controls. Only download pages you are authorized to access and save.
- A successful download is not a claim that every page or interaction is fully functional offline. Validate the saved site with airplane mode on the target device.

This is a bounded static-site downloader, not multi-hop mesh routing or an Internet gateway.
