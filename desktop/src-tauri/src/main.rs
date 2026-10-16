use mdns_sd::{ServiceDaemon, ServiceEvent, ServiceInfo};
use serde::{Deserialize, Serialize};
use std::{
    io::{BufRead, BufReader, Write},
    net::{SocketAddr, TcpListener, TcpStream},
    sync::{
        atomic::{AtomicBool, Ordering},
        Mutex,
        Arc,
    },
    thread,
    time::{Duration, SystemTime, UNIX_EPOCH},
};
use tauri::{Emitter, Manager, State};

const SERVICE_TYPE: &str = "_erikraftwifi._tcp.local.";
const PROTOCOL_VERSION: u8 = 1;

#[derive(Default)]
struct DiscoveryState {
    runtime: Mutex<Option<DiscoveryRuntime>>,
}

struct DiscoveryRuntime {
    daemon: ServiceDaemon,
    stop: Arc<AtomicBool>,
}

impl Drop for DiscoveryRuntime {
    fn drop(&mut self) {
        self.stop.store(true, Ordering::Relaxed);
        let _ = self.daemon.shutdown();
    }
}

#[derive(Deserialize)]
#[serde(rename_all = "camelCase")]
struct PeerMetadata {
    service: String,
    protocol: u8,
    capabilities: Vec<String>,
    file_transfer: bool,
}

#[derive(Serialize, Clone)]
#[serde(rename_all = "camelCase")]
struct PeerFound {
    name: String,
    address: String,
    port: u16,
    capabilities: Vec<String>,
    file_transfer: bool,
}

fn read_peer_metadata(address: SocketAddr) -> Option<PeerMetadata> {
    let mut stream = TcpStream::connect_timeout(&address, Duration::from_millis(900)).ok()?;
    let _ = stream.set_read_timeout(Some(Duration::from_millis(900)));
    let mut line = String::new();
    BufReader::new(stream).read_line(&mut line).ok()?;
    let metadata: PeerMetadata = serde_json::from_str(line.trim()).ok()?;
    if metadata.service != "ErikrafT Wi-Fi" || metadata.protocol != PROTOCOL_VERSION {
        return None;
    }
    Some(metadata)
}

#[tauri::command]
fn start_discovery(app: tauri::AppHandle, state: State<'_, DiscoveryState>) -> Result<String, String> {
    let mut runtime_guard = state.runtime.lock().map_err(|_| "Discovery state is unavailable.")?;
    if runtime_guard.is_some() {
        return Ok("running".into());
    }

    let listener = TcpListener::bind(("0.0.0.0", 0)).map_err(|error| error.to_string())?;
    listener.set_nonblocking(true).map_err(|error| error.to_string())?;
    let port = listener.local_addr().map_err(|error| error.to_string())?.port();

    let timestamp = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .map_err(|error| error.to_string())?
        .as_millis() as u64;
    let node_name = format!("ErikWiFi-{:06x}", timestamp & 0x00ff_ffff);
    let host_name = format!("{}.local.", node_name.to_ascii_lowercase());

    let daemon = ServiceDaemon::new().map_err(|error| error.to_string())?;
    let properties = [
        ("protocol", PROTOCOL_VERSION.to_string()),
        ("capabilities", "discovery".to_string()),
        ("fileTransfer", "false".to_string()),
    ];
    let service = ServiceInfo::new(
        SERVICE_TYPE,
        &node_name,
        &host_name,
        "",
        port,
        &properties[..],
    )
    .map_err(|error| error.to_string())?
    .enable_addr_auto();
    daemon.register(service).map_err(|error| error.to_string())?;
    let receiver = daemon.browse(SERVICE_TYPE).map_err(|error| error.to_string())?;

    let stop = Arc::new(AtomicBool::new(false));
    let server_stop = Arc::clone(&stop);
    let server_app = app.clone();
    thread::spawn(move || {
        let payload = b"{\"service\":\"ErikrafT Wi-Fi\",\"protocol\":1,\"capabilities\":[\"discovery\"],\"fileTransfer\":false}\n";
        while !server_stop.load(Ordering::Relaxed) {
            match listener.accept() {
                Ok((mut stream, _)) => {
                    let _ = stream.set_write_timeout(Some(Duration::from_secs(1)));
                    let _ = stream.write_all(payload);
                }
                Err(error) if error.kind() == std::io::ErrorKind::WouldBlock => {
                    thread::sleep(Duration::from_millis(80));
                }
                Err(error) => {
                    let _ = server_app.emit("wifi-discovery-status", format!("Metadata endpoint: {error}"));
                    break;
                }
            }
        }
    });

    let browse_stop = Arc::clone(&stop);
    let browse_app = app.clone();
    let self_name = node_name.clone();
    thread::spawn(move || {
        while !browse_stop.load(Ordering::Relaxed) {
            if let Ok(event) = receiver.recv_timeout(Duration::from_millis(250)) {
                if let ServiceEvent::ServiceResolved(info) = event {
                    if info.get_fullname().starts_with(&format!("{self_name}.")) {
                        continue;
                    }
                    for address in info.get_addresses() {
                        if address.is_loopback() || address.is_unspecified() || !address.is_ipv4() {
                            continue;
                        }
                        let socket_address = SocketAddr::new(*address, info.get_port());
                        if let Some(metadata) = read_peer_metadata(socket_address) {
                            let display_name = info.get_fullname().split('.').next().unwrap_or("ErikrafT Wi-Fi").to_string();
                            let peer = PeerFound {
                                name: display_name,
                                address: address.to_string(),
                                port: info.get_port(),
                                capabilities: metadata.capabilities,
                                file_transfer: metadata.file_transfer,
                            };
                            let _ = browse_app.emit("wifi-peer-found", peer);
                        }
                    }
                }
            }
        }
    });

    *runtime_guard = Some(DiscoveryRuntime { daemon, stop });
    let _ = app.emit("wifi-discovery-status", "running");
    Ok("running".into())
}

#[tauri::command]
fn stop_discovery(app: tauri::AppHandle, state: State<'_, DiscoveryState>) -> Result<String, String> {
    let mut runtime_guard = state.runtime.lock().map_err(|_| "Discovery state is unavailable.")?;
    if let Some(runtime) = runtime_guard.take() {
        drop(runtime);
    }
    let _ = app.emit("wifi-discovery-status", "stopped");
    Ok("stopped".into())
}

fn main() {
    tauri::Builder::default()
        .manage(DiscoveryState::default())
        .invoke_handler(tauri::generate_handler![start_discovery, stop_discovery])
        .run(tauri::generate_context!())
        .expect("failed to run ErikrafT Wi-Fi desktop client");
}
