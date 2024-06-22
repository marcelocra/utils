#!/usr/bin/env bun
/**
 * Helps with useful commands.
 */

import { readdirSync } from "fs";
import { resolve } from "path";
import { format, parseArgs } from "util";
import React, { useState, useEffect } from "react";
import { render, Text, Box, Static } from "ink";

import { debug, error } from "./bin-helpers.js";
import { freeRam } from "./bin-cmd-free-ram.js";

// Parse command line arguments.
const { values, positionals } = parseArgs({
  args: process.argv,
  options: {
    cmd: {
      type: "string",
    },
  },
  strict: true,
  allowPositionals: true,
});
debug({ values, positionals }, { print: false });

const cmds = {
  freeRam: {
    name: "free-ram",
    doc: "Prints the amount of free ram memory",
  },
  smt: {
    name: "other",
    doc: "hey",
  },
};

if (!values.cmd) {
  render(
    <Box gap={1} flexDirection="column">
      <Text>Usage: mcra-utils --cmd [command]</Text>
      <Text>Valid commands:</Text>
      {Object.entries(cmds).map(([cmdId, cmd]) => {
        // error(format("[%s]\t\t%s", cmd.name.padEnd(10, " "), cmd.doc));
        return <Text key={cmdId}>{cmd.name}</Text>;
      })}
    </Box>,
  );
}

// Get path to templates directory.
const TEMPLATES_PATH = resolve(import.meta.dirname, "templates");
debug(TEMPLATES_PATH, { print: false });

switch (values.cmd) {
  case cmds.freeRam.name:
    freeRam();
    break;

  default:
    break;
}
