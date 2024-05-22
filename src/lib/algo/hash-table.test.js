import { test } from "node:test";
import { strict as assert } from "node:assert";
import { hashTable } from "./hash-table.js";

test("the correct map is built", () => {
  assert.deepEqual(
    hashTable({ keys: ["a", "b"], values: [1, 2] }),
    new Map([
      ["a", 1],
      ["b", 2],
    ])
  );
});
