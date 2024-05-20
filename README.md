# Utils

> Be more productive by automating tasks and avoiding code rewrites.

## Goal

The goal is for this package to be both a command line program (cli) and a
utility library.

**IMPORTANT:** Currently, 19may2024, we are not there yet!

## to do now, per version

v0.1.3

- [ ] finish porting code from `core.clj` to cljs
- [ ] rename `js` files to `mjs`, to make it more clear
- [ ] create algorithm study section
- [ ] add tests
  - might be possible to add cljs tests in the same file as the code is,
    importing it later in a test file so that the testing framework recognize it
- [x] add `author` and `repository` fields in the `package.json` file

## ideas

- [ ] full `shadow-cljs` project, with Reagent, TailwindCSS and DaisyUI
      (Tailwind plugin)
- [ ] snippet program, to help insert code snippets in any editor. Should work
      like VSCode snippets, but powered by a cli, to work in any editor (like an
      lsp). Would allow me to have the same snippet system in Neovim, Sublime,
      etc
- [ ] migrate all `archive` code and script references to the snippet program

---

<details>
<summary>Thinking about the installation, for later.</summary>

## Install

### cli

To use this package as a task automation cli, running like an isolated binary,
run:

```sh
pnpm add --global mcra-utils

```

I also alias it to `u`. If you like the alias, run

```sh
mcra-utils --create-alias
```

to add it both to `~/.bashrc` and `~/.zshrc`. Source those or reload your
terminal to use the alias, as usual.

### JavaScript utility library

To use this package as a JavaScript utility library, add it to your project
dependencies:

```sh
# In your project folder, run:
pnpm add mcra-utils
```

</details>
