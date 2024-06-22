export function error(msg) {
  console.error(msg);
}

/**
 * Prints stuff to the console to help debugging.
 *
 * @param {any} msg Whatever you want to print debug.
 * @param {{print: boolean}} opts Debug options.
 * @returns Nothing.
 */
export function debug(msg, opts) {
  if (!opts || !opts.print) {
    return;
  }

  console.debug(msg);
}
