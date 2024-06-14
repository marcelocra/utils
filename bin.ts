#!/usr/bin/env bun

import { $ } from "zx";
import { expect, test } from "bun:test";
import path from "path";

const DEFAULT_LOGFILE_PATH = path.resolve(Bun.env.HOME, ".sync-repo.log.txt");

function helloWorld() {
  return "hello";
}

if (Bun.env.NODE_ENV === "test") {
  test(helloWorld.name, () => {
    expect(helloWorld()).toBe("hello");
  });
}
