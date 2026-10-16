import sharp from "sharp";
import { fileURLToPath } from "node:url";
import path from "node:path";

const here = path.dirname(fileURLToPath(import.meta.url));
const root = path.resolve(here, "..", "..");
const source = path.join(root, "assets", "ErikrafT_Wifi_Icon_Logo.svg");
const output = path.join(root, "desktop", "src-tauri", "icon-source.png");

await sharp(source, { density: 384 })
  .resize(1024, 1024, {
    fit: "contain",
    background: { r: 0, g: 0, b: 0, alpha: 0 }
  })
  .png()
  .toFile(output);

console.log("Generated temporary 1024x1024 source icon from the existing project SVG.");
