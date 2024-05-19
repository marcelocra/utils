#!/usr/bin/env node

import { loadFile } from "nbb";

const { main } = await loadFile("./src/dev/marcelocra/bin.cljs");

main();
