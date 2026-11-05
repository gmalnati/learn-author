---
title: La riga di richiesta
summary: Scrivi un programma che legge la prima riga di una richiesta HTTP e ne stampa le parti, o la rifiuta se non è valida.
# Un programma che legge dallo standard input e scrive sullo standard output, senza un servizio:
# lo verificano i casi di cases.yaml, gli stessi per ogni linguaggio (ADR 027).
checks: [cases]
duration: 25
solution: { after-attempts: 3 }
# Senza librerie web: la toolchain di base, più leggera da scaricare (ADR 027).
profiles: { kotlin: base, rust: base }
---

# La riga di richiesta

Ogni richiesta HTTP comincia con una riga di tre parti separate da uno spazio: il **metodo**, la
**destinazione** e la **versione** del protocollo. Per esempio:

```
GET /saluto?nome=Ada HTTP/1.1
```

Scrivi un programma che legge questa riga dallo standard input e ne stampa le parti, una per riga:

```
metodo: GET
percorso: /saluto
query: nome=Ada
versione: HTTP/1.1
```

Il percorso è la destinazione fino al `?`, la query è ciò che lo segue. La riga `query` c'è solo
quando la query non è vuota.

La riga termina con `\r\n`, come vuole HTTP, oppure solo con `\n`. La fine della riga non fa parte
della versione.

Una riga **non è valida** quando:

- non ha esattamente tre parti separate da un solo spazio;
- il metodo non è fatto solo di lettere maiuscole (`get` non è `GET`);
- la destinazione non comincia con `/`;
- la versione non è `HTTP/1.0` né `HTTP/1.1`.

In questi casi il programma non stampa niente sullo standard output, scrive `riga di richiesta non
valida` sullo standard error e termina con il codice di uscita 2.

In Kotlin `main` è in `src/main/kotlin/dev/learnlab/exercise/Main.kt` e la funzione da completare,
`parseRequestLine`, in `RequestLine.kt`; in Rust entrambe sono in `src/main.rs` (la funzione è
`parse_request_line`). `main` legge l'input e stampa il risultato: tu completa la funzione.

## Come viene verificato

Non ci sono test da scrivere né un servizio da avviare. `learn check` compila il programma e lo
avvia una volta per ogni caso di `cases.yaml`, lo stesso file per Kotlin e per Rust: per ogni caso
confronta lo standard output e il codice di uscita con quelli attesi e, se non coincidono, ti
mostra la differenza. `cases.yaml` è nella cartella dell'esercizio: leggerlo è il modo più rapido
per capire che cosa ci si aspetta.
