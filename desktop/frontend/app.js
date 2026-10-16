const copy = {
  en: {
    platform: "DESKTOP PROTOTYPE", eyebrow: "LOCAL NETWORK · EXPERIMENTAL",
    headline: 'Your devices.<br><span>Your network.</span>',
    intro: "Discover compatible ErikrafT Wi-Fi nodes on the same local network. This prototype does not transfer files or provide Internet access by itself.",
    network: "NETWORK DISCOVERY", discovery: "Nearby devices",
    ready: "Ready to search the current local network.",
    start: "Discover devices", stop: "Stop discovery", clear: "Clear list",
    empty: "No compatible devices found yet.",
    prototypeTitle: "Prototype limitations",
    prototypeNote: "Discovery is not authentication. The metadata endpoint is not encrypted; do not send private data. Wi-Fi Direct, mesh routing, gateways, file transfer, and functional Drop integration are not implemented.",
    docs: "Read the project documentation ↗", footer: "Open source · Built by ErikrafT",
    unavailable: "This preview is not running inside the desktop app. Launch the Windows, Linux, or macOS build to use local network discovery.",
    starting: "Starting local-network discovery…", running: "Searching for ErikrafT Wi-Fi nodes on this LAN…",
    stopped: "Discovery stopped.", startError: "Could not start discovery. Check your network interface and permissions.",
    peerNote: "Discovered on the local network · file transfer unavailable"
  },
  "pt-BR": {
    platform: "PROTÓTIPO DESKTOP", eyebrow: "REDE LOCAL · EXPERIMENTAL",
    headline: "Seus dispositivos.<br><span>Sua rede.</span>",
    intro: "Descubra nós ErikrafT Wi-Fi compatíveis na mesma rede local. Este protótipo não transfere arquivos nem fornece acesso à Internet sozinho.",
    network: "DESCOBERTA NA REDE", discovery: "Dispositivos próximos",
    ready: "Pronto para procurar na rede local atual.",
    start: "Procurar dispositivos", stop: "Parar descoberta", clear: "Limpar lista",
    empty: "Nenhum dispositivo compatível encontrado ainda.",
    prototypeTitle: "Limitações do protótipo",
    prototypeNote: "Descoberta não é autenticação. O endpoint de metadados não é criptografado; não envie dados privados. Wi-Fi Direct, roteamento mesh, gateways, transferência de arquivos e integração funcional com Drop não estão implementados.",
    docs: "Leia a documentação do projeto ↗", footer: "Open source · Construído por ErikrafT",
    unavailable: "Esta prévia não está sendo executada dentro do app desktop. Abra a versão para Windows, Linux ou macOS para usar a descoberta na rede local.",
    starting: "Iniciando a descoberta na rede local…", running: "Procurando nós ErikrafT Wi-Fi nesta LAN…",
    stopped: "Descoberta parada.", startError: "Não foi possível iniciar a descoberta. Verifique a interface de rede e as permissões.",
    peerNote: "Encontrado na rede local · transferência indisponível"
  }
};
const lang = (navigator.language || "").toLowerCase().startsWith("pt") ? "pt-BR" : "en";
const t = key => (copy[lang] || copy.en)[key] || copy.en[key] || key;
document.documentElement.lang = lang;
document.querySelectorAll("[data-i18n]").forEach(el => {
  const key = el.dataset.i18n;
  const map = { "platform":"platform", "eyebrow":"eyebrow", "headline":"headline", "intro":"intro", "network":"network", "discovery":"discovery", "ready":"ready", "start":"start", "clear":"clear", "empty":"empty", "prototype-title":"prototypeTitle", "prototype-note":"prototypeNote", "docs":"docs", "footer":"footer" };
  const value = t(map[key] || key);
  if (value) el.innerHTML = value;
});
const statusNode = document.querySelector("#status");
const statusDot = document.querySelector("#status-dot");
const button = document.querySelector("#toggle-discovery");
const peerList = document.querySelector("#peer-list");
let running = false;
let unlistenStatus = null;
let unlistenPeer = null;
const peers = new Set();
function setStatus(message, active = running) {
  statusNode.textContent = message;
  statusDot.classList.toggle("running", active);
}
function clearPeers() {
  peers.clear();
  peerList.innerHTML = "";
  const empty = document.createElement("p");
  empty.className = "empty";
  empty.textContent = t("empty");
  peerList.appendChild(empty);
}
function addPeer(peer) {
  const key = `${peer.name}@${peer.address}:${peer.port}`;
  if (peers.has(key)) return;
  peers.add(key);
  if (peers.size === 1) peerList.innerHTML = "";
  const card = document.createElement("article");
  card.className = "peer";
  const name = document.createElement("div");
  name.className = "peer-name";
  name.textContent = peer.name;
  const address = document.createElement("div");
  address.className = "peer-address";
  address.textContent = `${peer.address}:${peer.port}`;
  const note = document.createElement("p");
  note.className = "peer-note";
  note.textContent = t("peerNote");
  card.append(name, address, note);
  peerList.appendChild(card);
}
async function connectNative() {
  const api = window.__TAURI__;
  if (!api?.core?.invoke || !api?.event?.listen) {
    button.disabled = true;
    document.querySelector("#clear-peers").disabled = true;
    setStatus(t("unavailable"), false);
    return;
  }
  unlistenStatus = await api.event.listen("wifi-discovery-status", event => {
    const message = event.payload;
    if (message === "running") setStatus(t("running"), true);
    else if (message === "stopped") setStatus(t("stopped"), false);
    else setStatus(String(message), running);
  });
  unlistenPeer = await api.event.listen("wifi-peer-found", event => addPeer(event.payload));
  document.querySelector("#clear-peers").addEventListener("click", clearPeers);
  button.addEventListener("click", async () => {
    button.disabled = true;
    if (!running) {
      clearPeers();
      setStatus(t("starting"), false);
      try {
        await api.core.invoke("start_discovery");
        running = true;
        button.textContent = t("stop");
        setStatus(t("running"), true);
      } catch {
        setStatus(t("startError"), false);
      }
    } else {
      try {
        await api.core.invoke("stop_discovery");
      } catch {
        setStatus(t("startError"), true);
      }
      running = false;
      button.textContent = t("start");
      setStatus(t("stopped"), false);
    }
    button.disabled = false;
  });
}
clearPeers();
connectNative();
window.addEventListener("beforeunload", () => {
  unlistenStatus?.();
  unlistenPeer?.();
});
