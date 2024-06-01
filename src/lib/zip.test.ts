import { test } from "node:test";
import { strict as assert } from "node:assert";

import { zip } from "./zip.ts";

test("merge two arrays into an array of pairs", () => {
  assert.deepEqual(zip(["a", "b", "c"], [1, 2, 3]), [
    ["a", 1],
    ["b", 2],
    ["c", 3],
  ]);
});
