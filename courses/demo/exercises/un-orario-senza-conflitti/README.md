---
title: Un orario senza conflitti
summary: Scrivi i due vincoli rigidi di un piccolo orario scolastico con Timefold, finché il validatore non trova più conflitti.
# Un servizio Spring Boot con una pagina JTE e htmx: lo verificano i test interni, le verifiche HTTP
# e i test nel browser, con axe per l'accessibilità (ADR 027, ADR 030).
checks: [tests, http, browser]
duration: 30
solution: { after-attempts: 2 }
profiles: { java: web }
---

# Un orario senza conflitti

Il servizio prepara l'orario di una scuola piccolissima: due docenti, Rossi e Bianchi, insegnano
nelle classi 3A e 3B, una lezione per classe, e la settimana ha due ore, `Lun 1` e `Lun 2`. Le
lezioni le sistema il risolutore di vincoli Timefold, e la pagina `/` le mostra in una griglia, con
il pulsante «Ricalcola» che chiede un nuovo calcolo senza ricaricare la pagina (htmx).

Il risolutore cerca l'orario con il punteggio migliore secondo i vincoli di `TimetableConstraints`.
Oggi i due vincoli non penalizzano niente: per il risolutore ogni orario è perfetto, e si ferma sul
primo che trova, con tutte le lezioni nella stessa ora. Se ne accorge `TimetableValidator`, che
controlla l'orario senza usare il risolutore, e la pagina mostra «4 conflitti».

Il tuo compito è scrivere i due vincoli rigidi:

- `teacherConflict`: un docente non insegna in due classi nella stessa ora;
- `classConflict`: una classe non ha due lezioni nella stessa ora.

Ogni coppia di lezioni che viola un vincolo vale una penalità rigida (`HardSoftScore.ONE_HARD`).
`forEachUniquePair` e `Joiners.equal` di Timefold fanno quasi tutto il lavoro. Il resto del servizio
è già scritto: non serve cambiarlo.

Trovi il progetto nella cartella `starter/`, con i test interni che descrivono i vincoli. Poi, nella
cartella dell'esercizio:

```sh
learn check
```

## Come viene verificato

- I **test interni** controllano ogni vincolo con `ConstraintVerifier` di Timefold, il validatore e
  l'API.
- Le **verifiche HTTP** di `verify/` chiedono l'orario all'API (`GET /api/timetable`): zero
  conflitti, quattro lezioni, punteggio `0hard/0soft`.
- I **test nel browser** di `browser/` aprono la pagina in Chromium, cercano la griglia e lo stato
  «Nessun conflitto.», premono «Ricalcola» e controllano la pagina con axe.

Il punteggio da solo non basta: con i vincoli vuoti è `0hard/0soft` anche quando l'orario è
sbagliato. Per questo le verifiche guardano i conflitti contati dal validatore.
