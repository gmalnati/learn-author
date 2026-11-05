---
title: Un endpoint di saluto
summary: Implementa GET /saluto, che risponde con un messaggio JSON personalizzato.
# Il tempo stimato per svolgerlo, che il portale mostra accanto alla durata della lezione.
duration: 30
# La soluzione si vede dopo due verifiche con learn check, o appena una è superata (EXE-08).
solution: { after-attempts: 2 }
---

# Un endpoint di saluto

Implementa un servizio HTTP in ascolto sulla porta 8080 con un solo endpoint:

- `GET /saluto?nome=Ada` risponde `200` con il corpo `{"messaggio": "Ciao, Ada!"}`;
- senza il parametro `nome` risponde `{"messaggio": "Ciao, mondo!"}`;
- ogni altro percorso risponde `404`.

Scrivi il codice nella cartella `starter/` del linguaggio che hai scelto: trovi già il progetto,
con i test interni che descrivono il comportamento atteso. Poi, nella cartella dell'esercizio:

```sh
learn check
```

La verifica compila il codice, esegue i test interni, avvia il servizio e controlla le risposte
con le richieste HTTP della cartella `verify/`, le stesse per tutti i linguaggi.
