import { zip } from "./zip.ts";

import type { Keys, Values } from "./types.ts";

/**
 *
 * @param params the params
 * @returns {Map<string, string|number>}
 */
export function hashTable(params: { keys: Keys; values: Values }) {
  const zipped = zip(params.keys, params.values);
  if (zipped instanceof Error) {
    return new Map();
  }

  return new Map(zipped);
}
