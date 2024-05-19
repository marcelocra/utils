import { loadFile } from "nbb";

const funcs = await loadFile("./src/dev/marcelocra/lib.cljs");
const exports = { ...funcs };

export default exports;
