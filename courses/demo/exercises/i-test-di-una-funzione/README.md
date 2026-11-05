---
title: I test di una funzione
summary: Scrivi i test della funzione che dà la classe di un codice di stato HTTP, fino a scoprire tutte le versioni con un bug.
kind: tests
# Senza un servizio: solo i test dello studente e le versioni difettose, niente verifiche HTTP (ADR 027).
checks: [tests, mutants]
duration: 20
solution: { after-attempts: 2 }
# Senza librerie web: la toolchain di base, più leggera da scaricare (ADR 027).
profiles: { kotlin: base, rust: base, java: base }
---

# I test di una funzione

La funzione `statusClass` (Kotlin, in `StatusClass.kt`), `StatusClass.of` (Java, in
`StatusClass.java`) o `status_class` (Rust, in `src/status.rs`) riceve un codice di stato HTTP e ne
restituisce la classe:

| Codici | Classe |
|---|---|
| 100-199 | `informativo` |
| 200-299 | `successo` |
| 300-399 | `redirezione` |
| 400-499 | `errore del client` |
| 500-599 | `errore del server` |

Un codice fuori da questi intervalli non è un codice di stato: in Kotlin e in Java la funzione lancia
`IllegalArgumentException`, in Rust restituisce un `Err`.

La funzione è già scritta e funziona: il tuo compito è **scrivere i test**. Trovi un test d'esempio,
in Kotlin in `src/test/kotlin`, in Java in `src/test/java`, in Rust in `tests/`. Scrivi lì gli altri.

`learn check` esegue i tuoi test sul codice corretto, dove devono passare, e poi su **tre versioni con
un bug**, una per volta: su ognuna almeno un test deve fallire. I bug di questo tipo si nascondono ai
confini degli intervalli. Le versioni difettose sono nella cartella `mutants/`: guardarle prima toglie
senso all'esercizio.

A differenza dell'esercizio precedente non c'è un servizio da avviare: l'esercizio dichiara solo le
verifiche `tests` e `mutants`, e `learn check` non esegue verifiche HTTP.
