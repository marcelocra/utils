const defaultStrings = {
  appName: "My app",
  appDescription: "My app description",
};

class DefaultStrings {
  static locale = "en-us";

  strings: typeof defaultStrings;

  constructor() {
    this.strings = defaultStrings;
  }
}

class EnUsStrings extends DefaultStrings {}

class PtBrStrings extends DefaultStrings {
  static override locale = "pt-br";

  constructor() {
    super();

    this.strings.appDescription = "Descrição do meu app em português.";
  }
}

const strings = new Map<string, DefaultStrings>([
  [EnUsStrings.locale, new EnUsStrings()],
]);

/**
 * Named `s` to simplify usage.
 */
export let s = {
  ...strings.get(EnUsStrings.locale)!.strings,
  currentLocale: EnUsStrings.locale,
};

/**
 * Whenever we add strings for a new locale, we need to add it here.
 */
const supportedLocales = new Set([EnUsStrings.locale, PtBrStrings.locale]);

export function changeStringsLocale(locale: string) {
  if (!supportedLocales.has(locale) || locale === s.currentLocale) {
    return;
  }

  if (strings.has(locale)) {
    s.currentLocale = locale;
    return;
  }

  switch (locale) {
    case PtBrStrings.locale:
      strings.set(PtBrStrings.locale, new PtBrStrings());
      break;
    default:
      console.error("Should never get here!");
      return;
  }

  s = { ...strings.get(locale)!.strings, currentLocale: locale };
}

