# Utils - they really are!

This is my atempt at a command line program where I can create all my program
and code generators.

Right now this program requires `babashka` and the core script lives in
`src/core.clj`.

I'll be splitting that script in a number of modules, to improve code quality,
but that file will probably always the the entry point.

Right now it already generates some stuff, with defaults that I care about:

- `shadow-cljs.edn`
- `.gitignore` for a shadow-cljs project
- `.prettierrc.json`
- `bb.edn`

You can find the next stuff I'm planning on working on in the next section.

## Up next

- [ ] full `shadow-cljs` project, with Reagent, TailwindCSS and DaisyUI
      (Tailwind plugin)
- [ ] snippet program, to help insert code snippets in any editor. Should work
      like VSCode snippets, but powered by a cli, to work in any editor (like an
      lsp). Would allow me to have the same snippet system in Neovim, Sublime,
      etc
- [ ] migrate all `archive` code and script references to the snippet program
