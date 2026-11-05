---
title: I test del saluto
summary: Scrivi i test del servizio di saluto, fino a scoprire tutte le versioni con un bug.
kind: tests
# La soluzione (i test dell'autore) si vede dopo due verifiche, o appena una è superata (EXE-08).
solution: { after-attempts: 2 }
---

# I test del saluto

Questa volta il servizio è già scritto e funziona: il tuo compito è **scrivere i test**. Il
servizio risponde a `GET /saluto` con queste regole:

1. `?nome=Ada` risponde `{"messaggio": "Ciao, Ada!"}`;
2. senza `nome`, o con un nome vuoto o di soli spazi, saluta il mondo: `Ciao, mondo!`;
3. toglie gli spazi intorno al nome: `?nome=%20Ada%20` saluta `Ada`;
4. un nome di più di 30 caratteri è rifiutato con `400`.

Le regole sono in `Saluti.kt` (Kotlin) e in `src/saluti.rs` (Rust). Trovi già un test d'esempio:
in Kotlin in `src/test/kotlin`, in Rust in `tests/`. Scrivi lì gli altri.

`learn check` esegue i tuoi test sul codice corretto, dove devono passare, e poi su **tre versioni
con un bug**, una per volta: su ognuna almeno un test deve fallire. Se una versione «sopravvive»,
il riepilogo ti dice quale bug i tuoi test non hanno visto. Le versioni difettose sono nella
cartella `mutants/`: guardarle prima toglie senso all'esercizio.

Le verifiche HTTP di `verify/` controllano che il servizio sia rimasto quello corretto: non
cambiare il codice, scrivi solo i test.
