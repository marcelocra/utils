import shelljs from "shelljs";
import { debug, error } from "./bin-helpers.js";

export function freeRam() {
  const memfree = shelljs.cat("/proc/meminfo")?.stdout?.match("MemFree.*");
  debug(memfree, { print: false });

  if (!memfree) {
    error("Couldn't find the results.");
    process.exit(1);
  }

  console.log(memfree[0].replace(/MemFree:[^0-9]+/, ""));
}
