# Corso demo

Un breve corso sul protocollo HTTP che usa **ogni componente didattico** di Learn2K. È il riferimento da copiare quando scrivi un corso: ogni elemento è descritto nella [guida al formato dei corsi](../../docs/guides/formato-dei-corsi.md), gli esercizi nella [guida agli esercizi](../../docs/guides/esercizi.md).

## Provarlo

Con `learn-author` installato, dalla cartella che contiene `courses/demo`:

```sh
learn-author preview courses/demo    # il corso nel browser, su http://localhost:5175
learn-author validate courses/demo   # i controlli che fa la piattaforma
```

L'anteprima mostra le lezioni come le vedono gli studenti, con i quiz corretti in locale e le soluzioni visibili all'autore. Per verificare gli esercizi serve Docker: `learn-author test-exercises courses/demo`.

## Dove trovare un esempio

| Lezione | Componenti |
|---|---|
| [Benvenuto nel corso](modules/01-introduzione/01-benvenuto.mdx) | `<Callout>`, `<Image>` |
| [Il protocollo HTTP](modules/01-introduzione/02-protocollo-http.mdx) | `<CodeTabs>`, `<Diagram>`, `<Playground>`, `<Video>`, `<Simulator>`, `<Quiz>` con domande `single`, `multiple` e `true-false` |
| [Prepara l'ambiente](modules/02-primo-servizio/01-ambiente-locale.mdx) | `<Callout>`, `<CodeTabs>` |
| [Un endpoint di saluto](modules/02-primo-servizio/02-endpoint-di-saluto.mdx) | `<Video>`, `<Checkpoint>`, `<Exercise>`, `<Quiz>` con domande `fill` (`<Blank>`), `order` e `find-error` e un `<Draw>` |

- **Banca di domande:** [`banks/codici-di-stato.mdx`](banks/codici-di-stato.mdx), da cui estrae il `<Draw>` dell'ultima lezione.
- **Simulatori:** [`simulators/`](simulators), nella lezione «Il protocollo HTTP». Usano l'SDK dei simulatori e si completano solo quando la pagina lo comunica: `ciclo-di-vita-richiesta` all'ultima fase della richiesta, `router-http` quando lo studente ha ottenuto una risposta di una rotta, un `404` e un `405`.
- **Esercizi:** [`exercises/`](exercises), uno per tipo di verifica:

| Esercizio | Verifiche | Linguaggi |
|---|---|---|
| [`hello-http`](exercises/hello-http) | test interni e verifiche HTTP | Kotlin, Rust |
| [`test-del-saluto`](exercises/test-del-saluto) | i test dello studente sulle versioni difettose, verifiche HTTP | Kotlin, Rust |
| [`i-test-di-una-funzione`](exercises/i-test-di-una-funzione) | i test dello studente sulle versioni difettose di una funzione, senza servizio | Kotlin, Rust, Java |
| [`la-riga-di-richiesta`](exercises/la-riga-di-richiesta) | casi di input e output | Kotlin, Rust |
| [`i-codici-di-stato`](exercises/i-codici-di-stato) | test interni e test nel browser | React (Node) |
| [`un-orario-senza-conflitti`](exercises/un-orario-senza-conflitti) | test interni, verifiche HTTP e test nel browser | Java (Spring Boot) |
| [`pronto-per-il-cluster`](exercises/pronto-per-il-cluster) | controlli statici di Dockerfile, workflow e manifest Kubernetes | soli file |

## Copiarlo

Puoi copiare e adattare qualunque parte di questo corso nei corsi che prepari per Learn2K: lo consente la licenza di `learn-author` ([LICENSE](https://github.com/gmalnati/learn-author/blob/main/LICENSE), «Additional Permission for the Examples»). Lo stesso vale per i progetti delle immagini degli esercizi, in [`images/`](../../images).

## Nel repository della piattaforma

Il corso è anche la fixture dei test di `packages/content-compiler`: ogni nuovo componente va aggiunto qui con un esempio d'uso.

```sh
make demo-course   # valida e compila il corso in courses/demo/dist
make preview       # anteprima con ricarica a caldo su http://localhost:5175
```
