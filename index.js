#!/usr/bin/env node

import { addClassPath, loadFile } from "nbb";
import { fileURLToPath } from "url";
import { dirname, resolve } from "path";

const __dirname = fileURLToPath(dirname(import.meta.url));

addClassPath(resolve(__dirname, "src"));

const funcs = await loadFile(resolve(__dirname, "src/dev/marcelocra/core.cljs"));
const exports = { ...funcs };
export default exports;
