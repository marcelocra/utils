import { addClassPath, loadFile } from "nbb";
import { fileURLToPath } from "url";
import { dirname, resolve } from "path";

const __dirname = fileURLToPath(dirname(import.meta.url));

addClassPath(resolve(__dirname, "dev/marcelocra"));

let file = resolve(__dirname, "dev/marcelocra/core.cljs");

export const { something } = await loadFile(file);
