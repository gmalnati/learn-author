---
title: I codici di stato
summary: Scrivi una pagina React che spiega un codice di stato HTTP mentre lo digiti.
# Un'app React senza un'API: la verificano i test interni (Vitest) e i test di browser/, eseguiti
# in Chromium contro l'app compilata (ADR 027).
checks: [tests, browser]
duration: 25
solution: { after-attempts: 2 }
---

# I codici di stato

Scrivi una pagina che spiega i codici di stato HTTP. La pagina ha:

- il titolo `I codici di stato`;
- un campo di testo con l'etichetta `Codice`;
- sotto il campo, una frase che cambia mentre scrivi, in un elemento con il ruolo `status` (per
  esempio `<p role="status">`): così le tecnologie assistive la leggono quando cambia.

La frase la calcola la funzione `describeStatus` di `src/status.ts`, a partire dal testo del campo
senza gli spazi intorno:

| Testo nel campo | Frase |
|---|---|
| vuoto | `Scrivi un codice di tre cifre.` |
| `404` | `404 Not Found: errore del client.` |
| `418` | `418: errore del client.` |
| `abc`, `99` o `600` | `abc non è un codice di stato.` |

Un codice di stato ha tre cifre, da `100` a `599`, e la prima ne dà la classe: `1` informativo,
`2` successo, `3` redirezione, `4` errore del client, `5` errore del server. Il nome del codice
(`Not Found`) c'è solo per quelli di `REASONS`, la tabella che trovi già in `status.ts`; gli altri
hanno solo la classe.

Scrivi il codice nella cartella `starter/`: trovi già il progetto React, con i test interni che
descrivono il comportamento atteso. Se hai Node, dopo `npm install` puoi eseguirli con `npm test` e
aprire la pagina con `npm run dev`. Poi, nella cartella dell'esercizio:

```sh
learn check
```

## Come viene verificato

`learn check` esegue i test interni e compila l'app, poi la avvia e la apre in Chromium: i test
della cartella `browser/` scrivono nel campo come farebbe una persona e controllano ciò che la
pagina mostra. Trovano gli elementi per ruolo ed etichetta, come le tecnologie assistive: un campo
senza un'etichetta collegata, per loro, non c'è.
