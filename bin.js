#!/usr/bin/env node

import { addClassPath, loadFile } from "nbb";
import { fileURLToPath } from "url";
import { dirname, resolve } from "path";

const __dirname = fileURLToPath(dirname(import.meta.url));

addClassPath(resolve(__dirname, "src"));
addClassPath(resolve(__dirname, "node_modules/mcra-utils"));

await loadFile(resolve(__dirname, "src/dev/marcelocra/bin.cljs"));
