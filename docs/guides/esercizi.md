# Esercizi

Guida per i docenti: come si scrive un esercizio che gli studenti verificano in locale con `learn check`. La struttura e i comandi sono gli stessi in ogni corso. Le decisioni di progetto sono nell'ADR 010 e, per le verifiche dichiarate, nell'ADR 027.

## In breve

1. `learn-author new exercise "Un endpoint di saluto"` crea la cartella dell'esercizio.
2. Copia in `starter/` e `solution/` il progetto di partenza della toolchain. Il comando precedente stampa i comandi `docker run … learn-init` da usare.
3. Scrivi la consegna nel `README.md`, la soluzione in `solution/` e, per un servizio, le verifiche HTTP in `verify/`, per un programma i casi di input e output in `cases.yaml`, per un'app nel browser i test Playwright in `browser/`, per dei file (un Dockerfile, dei manifest, un workflow) gli strumenti in `static.yaml` e le regole in `policy/`. Le verifiche dell'esercizio sono quelle di `checks` (`--checks` di `new exercise`).
4. Lascia nello starter i test interni e il codice da completare.
5. `learn-author test-exercises` controlla che la soluzione superi le verifiche e lo starter no.
6. Nella lezione, `<Exercise id="…" />` mostra la scheda con i comandi per lo studente.

## Struttura

```
exercises/hello-http/
├── README.md                consegna, con frontmatter title, summary, checks, duration e solution
├── AGENTS.md                facoltativo: istruzioni per l'agente AI dello studente
├── verify/                  verifiche HTTP (checks: http), le stesse per tutti i linguaggi
│   ├── saluto.hurl
│   └── percorsi.hurl
├── cases.yaml               oppure casi di input e output di un programma (checks: cases)
├── browser/                 oppure test nel browser di un'app (checks: browser)
├── static.yaml              controlli statici dei file (checks: static), con le regole di policy/*.rego
├── repeat.yaml              facoltativo, in Java web: le date su cui ripetere i test interni
├── kotlin/
│   ├── Dockerfile           immagine toolchain dell'edizione (generato)
│   ├── compose.yaml         servizio app e, con http, servizio check (generato)
│   ├── starter/             il progetto che lo studente completa
│   └── solution/            la soluzione di riferimento: mai nell'archivio dello starter
└── rust/
    └── …
```

- Nel frontmatter del README `title` e `summary` sono obbligatori. `duration`, facoltativo, è il tempo stimato in minuti per svolgere l'esercizio: il portale lo mostra accanto alla durata della lezione, che non lo comprende. `checks`, `kind` e `solution` sono descritti più avanti.
- Ogni cartella diversa da `verify/`, `browser/` e `policy/` è un **linguaggio**, con il nome della sua immagine toolchain: `kotlin` usa `learn-toolchain-kotlin`, `rust` usa `learn-toolchain-rust`, `java` usa `learn-toolchain-java`, o i loro profili `-web`, e `node` usa `learn-toolchain-node` (EXE-06, vedi «Il progetto»). `files` è la cartella di una consegna di soli file, senza toolchain: la leggono solo i controlli statici (vedi «I controlli statici»).
- `Dockerfile` e `compose.yaml` dipendono solo dalle verifiche dell'esercizio e li genera `learn-author`. Non vanno modificati: tutto quello che conta sta nell'immagine toolchain.
- Lo studente scarica il README, `checks.json` (le verifiche, per `learn`), `verify/`, `cases.yaml` o `browser/`, `static.yaml` e `policy/`, `repeat.yaml` e la cartella del suo linguaggio **senza** `solution/`. Restano fuori anche gli output di build locali (`build/`, `target/`, `.gradle/`, …).
- Il compilatore controlla che ci siano `Dockerfile`, `compose.yaml`, `starter/` e `solution/` (regola `exercise/missing-file`) e che eseguano le verifiche dichiarate (vedi sotto). Un esercizio senza linguaggi produce un avviso.

## Le verifiche (`checks`)

Il campo `checks` del README dice come si verifica l'esercizio (ADR 027):

| Verifica | Controlla | L'autore scrive |
|---|---|---|
| `tests` | i test interni del progetto | i test, nello starter e nella soluzione |
| `http` | il servizio, dall'esterno | `verify/*.hurl` |
| `mutants` | che i test dello studente scoprano le versioni difettose del codice | `mutants.yaml` e `mutants/` (vedi «Esercizi sui test») |
| `cases` | l'output di un programma per un input dato | `cases.yaml` (vedi «I casi di input e output») |
| `static` | i file del progetto, letti senza costruirli né avviarli: Dockerfile, workflow, manifest | `static.yaml` e `policy/*.rego` (vedi «I controlli statici») |
| `browser` | l'app nel browser, usata come farebbe una persona | `browser/*.spec.ts` (vedi «I test nel browser») |

```yaml
---
title: La somma di una lista
summary: Scrivi la funzione che somma i numeri di una lista.
checks: [tests]
---
```

- Senza `checks` un esercizio vale `[tests, http]` e uno con `kind: tests` `[tests, http, mutants]`: gli esercizi scritti prima di `checks` non cambiano.
- Solo `http` fa dell'esercizio un **servizio**; con `browser` l'app compilata è servita sulla stessa porta, per i test nel browser. Senza `http`, `browser` né `cases`, il `Dockerfile` esegue soltanto i test interni (`learn-test`), `compose.yaml` non ha i servizi `check` e `browser` e non servono né `verify/` né la porta 8080: basta il codice con i suoi test, come negli esercizi di un primer.
- `mutants` vale solo con `kind: tests` e richiede `tests`; un database (`--database`) richiede `http`, perché lo usa il servizio. `cases` non si combina con `http`, `browser` né con `kind: tests`, e `browser` non vale con `kind: tests`. `static` si combina con tutte: gira in uno stadio suo della build, sui file del progetto. La toolchain entra nel `Dockerfile` solo se una verifica la usa: con `checks: [static]` lo studente scarica solo il verificatore.
- `learn-author new exercise "Titolo" --checks tests` genera `Dockerfile` e `compose.yaml` per quelle verifiche (con `cases`, anche un `cases.yaml` d'esempio). Il compilatore controlla che i file eseguano tutte e sole le verifiche dichiarate: `exercise/check-not-run` e `exercise/check-not-declared` per `learn-test` e `learn-check-cases` nel `Dockerfile` e i servizi `check` e `browser` in `compose.yaml`, `exercise/missing-verify` e `exercise/unused-verify` per `verify/`, `exercise/missing-cases` e `exercise/unused-cases` per `cases.yaml`, `exercise/missing-browser-tests` e `exercise/unused-browser-tests` per `browser/`; per `static` vedi «I controlli statici». Per cambiare le verifiche di un esercizio esistente, cambia `checks` nel README e rigenera i file con `learn-author update-exercises`.
- Con `repeat.yaml`, in Java con il profilo web, i test interni della verifica `tests` girano una volta per ogni data, in un ordine casuale (vedi «I test ripetuti»).
- Un esercizio senza `http` richiede `learn` 0.4.0 o successiva, uno con `cases` la 0.5.0, uno con `browser` la 0.7.0, uno con `static` la 0.8.0: l'API rifiuta gli esiti delle versioni precedenti, che cercherebbero un servizio o non conterebbero i casi, i test nel browser o i controlli statici.
- Nel corso demo, `hello-http` e `test-del-saluto` non dichiarano `checks` e valgono come prima; `i-test-di-una-funzione` dichiara `checks: [tests, mutants]`: i test dello studente su una funzione, senza un servizio da avviare; `la-riga-di-richiesta` dichiara `checks: [cases]`: un programma che legge la prima riga di una richiesta HTTP; `i-codici-di-stato` dichiara `checks: [tests, browser]`: una pagina React, in `node/`. `i-test-di-una-funzione` ha anche la cartella `java/`, e `un-orario-senza-conflitti` dichiara `checks: [tests, http, browser]`: un servizio Spring Boot con una pagina JTE e htmx e un orario calcolato con Timefold, in `java/` con il profilo web; `pronto-per-il-cluster` dichiara `checks: [static]`: un Dockerfile, un workflow e i manifest Kubernetes di un servizio, in `files/`.

## La soluzione (EXE-08)

Il campo `solution` del frontmatter del README decide se e quando lo studente può vedere la soluzione di riferimento:

```yaml
---
title: Un endpoint di saluto
summary: Implementa GET /saluto, che risponde con un messaggio JSON.
solution: { after-attempts: 2 }
---
```

| Valore | Lo studente vede la soluzione |
|---|---|
| `never` (predefinito, anche senza il campo) | mai: la soluzione non entra nemmeno nel bundle |
| `always` | da subito |
| `after-pass` | dopo una verifica superata con `learn check` |
| `{ after-attempts: N }` | dopo N verifiche con `learn check`, in qualunque linguaggio, oppure appena una è superata |

Quando la soluzione si può sbloccare, `learn-author build` aggiunge al bundle, per ogni linguaggio:

- `exercises/<id>/<linguaggio>-solution.json`: i file di `solution/` nuovi o diversi da quelli di `starter/`, evidenziati come il codice delle lezioni, che il portale mostra nella scheda dell'esercizio. I file binari o più grandi di 100 kB non sono mostrati ma restano nell'archivio;
- `exercises/<id>/<linguaggio>-solution.zip`: il README, `verify/`, `Dockerfile`, `compose.yaml` e l'intera cartella `solution/`, da scaricare.

Sono nel bundle, ma l'API li consegna solo agli studenti che hanno sbloccato la soluzione (ADR 021). La politica è parte della versione del corso: cambiarla richiede una nuova pubblicazione.

Nel portale la scheda dell'esercizio spiega la regola (per esempio «Verifiche che mancano: 1») e, una volta sbloccata, mostra il pulsante **Vedi la soluzione**: i file cambiati, un linguaggio per scheda, e il link all'archivio completo. Nell'anteprima di `learn-author` l'autore vede sempre la soluzione, con la regola che vale per gli studenti.

## Il progetto

Lo starter e la soluzione partono dal progetto del template della toolchain, copiato da `learn-init`: le cartelle `template/` di [`images/`](../../images), una per toolchain. Ogni linguaggio ha due toolchain, i **profili** (ADR 027), che `profiles` sceglie in `course.yaml` per tutto il corso e nel README per un esercizio (`profiles: { kotlin: web, rust: web, java: web }`; senza, `base`):

| Profilo | Immagine | Progetto | Per |
|---|---|---|---|
| `base` | `learn-toolchain-kotlin` | Kotlin con Gradle, plugin `application` (`fun main` in `Main.kt`), JUnit 5 | programmi e funzioni, i primer |
| `base` | `learn-toolchain-rust` | pacchetto `app` con serde e serde_json | programmi e funzioni, i primer |
| `web` | `learn-toolchain-kotlin-web` | Spring Boot 4 | servizi web |
| `web` | `learn-toolchain-rust-web` | Axum 0.8, sqlx, SeaORM, … | servizi web |
| `base` | `learn-toolchain-node` | app React 19 in TypeScript con Vite, Vitest e Testing Library | app nel browser |
| `base` | `learn-toolchain-java` | Java 25 con Gradle, plugin `application` (`main` in `Main.java`), JUnit 5 e AssertJ | programmi e funzioni |
| `web` | `learn-toolchain-java-web` | Spring Boot 4 con pagine JTE e htmx; facoltativi SQLite, PostgreSQL, Flyway, Timefold, ArchUnit e gli strumenti di qualità (JaCoCo, Checkstyle, SpotBugs, PIT) | app web locali, il corso sugli agenti, quello sull'integrazione continua |

Le immagini `-web` sono costruite sopra la base: chi segue un primer e poi un corso web scarica i livelli della base una volta sola. Il primer Rust scarica circa 300 MB invece di 800, quello Kotlin 400 invece di 500. Con la verifica `http` il progetto è un servizio HTTP in ascolto sulla **porta 8080**; senza, basta il codice con i suoi test. I test interni sono JUnit 5 (`src/test/kotlin`) e `#[test]` eseguiti con cargo-nextest. `learn-run` passa al programma gli argomenti che riceve.

**Le dipendenze sono quelle dell'immagine**, con le stesse versioni: l'immagine le contiene già e gli esercizi si compilano senza rete (`network: none` nel `compose.yaml`, EXE-09). Una dipendenza che l'immagine non contiene fa fallire la compilazione con un errore di risoluzione offline. Se serve davvero, va aggiunta in una nuova versione dell'immagine. Il compilatore controlla che il `Dockerfile` usi la toolchain del profilo (regola `exercise/wrong-toolchain`).

- **Kotlin, base.** `build.gradle.kts` e `settings.gradle.kts` del template, senza aggiunte.
- **Kotlin, web.** Il template contiene Spring MVC, la validazione e Jackson. Nel `build.gradle.kts` dell'esercizio puoi aggiungere, senza versione, le dipendenze facoltative di [`images/toolchain-kotlin-web/cache/build.gradle.kts`](../../images/toolchain-kotlin-web/cache/build.gradle.kts):

  | Per | Dipendenze |
  |---|---|
  | JPA e PostgreSQL | plugin `kotlin("plugin.jpa") version "2.3.21"`; `spring-boot-starter-data-jpa` (o `spring-boot-starter-jdbc`), `runtimeOnly` `org.postgresql:postgresql`; nei test `spring-boot-starter-data-jpa-test` e `testRuntimeOnly` `com.h2database:h2` |
  | Migrazioni | `spring-boot-starter-flyway`, `org.flywaydb:flyway-database-postgresql` |
  | Sicurezza e JWT | `spring-boot-starter-security`, `spring-boot-starter-oauth2-resource-server`; nei test `spring-boot-starter-security-test` |
  | Coroutine e Actuator | `org.jetbrains.kotlinx:kotlinx-coroutines-core`, `kotlinx-coroutines-reactor`, `spring-boot-starter-actuator` |
  | Test double e property-based testing | nei test `io.mockk:mockk:1.14.11`, `net.jqwik:jqwik:1.10.1` (con la versione: non sono nel BOM di Spring Boot) |
  | Monolite modulare | `implementation(platform("org.springframework.modulith:spring-modulith-bom:2.1.1"))`, poi `spring-modulith-starter-core`, `spring-modulith-starter-jdbc` (registro delle pubblicazioni degli eventi); nei test `spring-modulith-starter-test` |
  | Documento OpenAPI | `org.springdoc:springdoc-openapi-starter-webmvc-api:3.1.1` |

- **Node.** `package.json` e `package-lock.json` sono **sempre** quelli del template: React e React DOM 19.3, React Router 8.5, TypeScript 6.0, Vite 8.3, Vitest 5.0 con jsdom e Testing Library (`react`, `jest-dom`, `user-event`). Le dipendenze stanno nell'immagine, in `/opt/learn/template/node_modules`, e `learn-test` e `learn-build` le collegano al progetto con un link simbolico: la build non installa niente. I test interni sono i file `*.test.ts(x)` di `src/`, eseguiti da Vitest in jsdom; `learn-build` controlla i tipi (`tsc -b`) e compila l'app con Vite, e `learn-run` serve `dist/` sulla porta 8080, rimandando a `index.html` gli indirizzi senza estensione, come un'app a pagina singola in produzione. Con Node installato, lo studente può anche lavorare in locale: `npm install`, poi `npm test` e `npm run dev`.
- **Java** (ADR 030, ADR 031). La base è costruita sopra quella di Kotlin e ne riusa JDK, Gradle e comandi `learn-*`. Nel profilo web il `build.gradle.kts` del template ha Spring MVC, la validazione, JTE 3.2 con lo starter per Spring Boot 4 (i template in `src/main/jte`, compilati con il codice: un errore in un template ferma la build) e htmx 2.0 servito dal jar (`/webjars/htmx.org/2.0.11/dist/htmx.min.js`). `learn-run` avvia il servizio con `--enable-native-access=ALL-UNNAMED`, che serve al driver di SQLite con Java 25. Con Timefold 2 i campi annotati privati vogliono un setter pubblico (per esempio quello del punteggio), e un risolutore negli esercizi ha un seme fisso e un tempo massimo, così le verifiche sono ripetibili. Nel `build.gradle.kts` dell'esercizio puoi aggiungere le dipendenze e i plugin di [`images/toolchain-java-web/cache/build.gradle.kts`](../../images/toolchain-java-web/cache/build.gradle.kts), con le versioni scritte lì; le righe segnate con *2026.4* richiedono `learn-toolchain-java-web:2026.4` nel `Dockerfile`, che gli esercizi nuovi nominano già (uno esistente si aggiorna con `learn-author update-exercises`):

  | Per | Dipendenze e plugin |
  |---|---|
  | SQLite | `spring-boot-starter-jdbc`, `spring-boot-starter-flyway`, `runtimeOnly` `org.xerial:sqlite-jdbc` |
  | PostgreSQL (*2026.4*) | `spring-boot-starter-jdbc`, `spring-boot-starter-flyway`, `org.flywaydb:flyway-database-postgresql`, `runtimeOnly` `org.postgresql:postgresql`; nei test `spring-boot-starter-jdbc-test` e `testRuntimeOnly` `com.h2database:h2` (vedi «Esercizi con un database») |
  | Orari | `ai.timefold.solver:timefold-solver-core:2.7.1` (anche `ConstraintVerifier` per i test) |
  | Regole sulla struttura | nei test `com.tngtech.archunit:archunit-junit5:1.5.1` |
  | Copertura (*2026.4*) | plugin `jacoco` di Gradle, JaCoCo `0.8.15` (la versione di Gradle 9.8.1) |
  | Stile (*2026.4*) | plugin `checkstyle` di Gradle con `toolVersion = "14.3.0"`, le regole in `config/checkstyle/checkstyle.xml` |
  | Bug comuni (*2026.4*) | `id("com.github.spotbugs") version "6.5.13"`, SpotBugs `4.10.5`; `compileOnly` `com.github.spotbugs:spotbugs-annotations:4.10.5` per `@SuppressFBWarnings` |
  | Versioni difettose con PIT (*2026.4*) | `id("info.solidsoft.pitest") version "1.19.0"` con `pitestVersion = "1.30.1"` e `junit5PluginVersion = "1.2.3"` |

  Gli strumenti di qualità scaricano i loro artefatti solo quando girano, quindi l'immagine ha solo le versioni della tabella: senza `toolVersion` Checkstyle cercherebbe la 10.24.0 e PIT la 1.22.1, che non ci sono, e la build fallisce con un errore di risoluzione offline. Un blocco completo, provato senza rete, da aggiungere al `build.gradle.kts` del template:

  ```kotlin
  plugins {
      java
      jacoco
      checkstyle
      id("org.springframework.boot") version "4.1.1"
      id("gg.jte.gradle") version "3.2.4"
      id("com.github.spotbugs") version "6.5.13"
      id("info.solidsoft.pitest") version "1.19.0"
  }

  // … repositories, java, dependencies, jte e tasks del template …

  // Copertura: almeno l'80% delle righe di ogni classe, anche di una nuova.
  jacoco {
      toolVersion = "0.8.15"
  }
  // Le classi che JTE genera dai template non sono codice dell'esercizio.
  val ownClasses = sourceSets.main.get().output.classesDirs.asFileTree.matching {
      exclude("gg/jte/generated/**")
  }
  tasks.jacocoTestReport {
      classDirectories.setFrom(ownClasses)
  }
  tasks.jacocoTestCoverageVerification {
      classDirectories.setFrom(ownClasses)
      violationRules {
          rule {
              element = "CLASS"
              excludes = listOf("dev.learnlab.exercise.Application")
              limit {
                  counter = "LINE"
                  minimum = "0.80".toBigDecimal()
              }
          }
      }
  }
  tasks.check {
      dependsOn(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
  }

  // Stile: le regole di config/checkstyle/checkstyle.xml, nessun avviso ammesso.
  checkstyle {
      toolVersion = "14.3.0"
      maxWarnings = 0
  }
  tasks.checkstyleMain {
      exclude("gg/jte/generated/**")
  }

  // Bug comuni, con un report HTML.
  spotbugs {
      toolVersion = "4.10.5"
  }
  tasks.spotbugsMain {
      reports.create("html") {
          required = true
      }
  }

  // Versioni difettose delle regole: tutte scoperte.
  pitest {
      pitestVersion = "1.30.1"
      junit5PluginVersion = "1.2.3"
      targetClasses = setOf("dev.learnlab.exercise.loans.LoanPolicy")
      targetTests = setOf("dev.learnlab.exercise.loans.LoanPolicyTest")
      mutationThreshold = 100
      timestampedReports = false
  }
  // Dopo i task che lavorano in background, che nasconderebbero il riepilogo di PIT nella console.
  tasks.pitest {
      mustRunAfter(tasks.spotbugsMain, tasks.spotbugsTest, tasks.jacocoTestReport)
  }
  ```

  - Il codice che JTE genera dai template sta in `build/generated-sources/jte`: Checkstyle lo controllerebbe e JaCoCo lo conterebbe nella copertura, per questo il blocco lo esclude.
  - SpotBugs segnala come mai letti i campi pubblici che un framework legge per riflessione, come le entità di Timefold: `@SuppressFBWarnings(value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", justification = "…")`.
  - `learn-test` esegue `gradle test`, non `gradle check`: in `learn check` gli strumenti girano solo se l'esercizio li collega ai test, per esempio con `tasks.test { finalizedBy(tasks.checkstyleMain, tasks.spotbugsMain, tasks.jacocoTestCoverageVerification) }`. Dalla *2026.4* un controllo fallito fa allora fallire la verifica `tests` con un test chiamato come il task (`gradle > :checkstyleMain`) e la causa data da Gradle, e in un esercizio sui test ferma le versioni difettose (vedi «Esercizi sui test»).
- **Rust.** `Cargo.toml` e `Cargo.lock` sono **sempre** quelli del template della toolchain del profilo: nella base serde e serde_json, nel profilo web tutte le librerie dei servizi: Axum (con le macro), Tokio, tokio-stream e futures-util, serde, sqlx (PostgreSQL), SeaORM, jsonwebtoken, tower-http, utoipa e utoipa-axum (OpenAPI), hmac, sha2 e base64, uuid, chrono, thiserror, tracing, validator; nei test http-body-util, mockall e proptest. Un esercizio sui test che usa `tests/` ha bisogno anche di `src/lib.rs` (vedi «Esercizi sui test»). Non modificarli: con `--locked`, un `Cargo.lock` diverso da quello dell'immagine non si compila senza rete.

## Esercizi con un database

`learn-author new exercise "Titolo" --database` aggiunge al `compose.yaml` un servizio `db` con PostgreSQL 17, nuovo e vuoto a ogni verifica. Il servizio dello studente parte quando il database è pronto e lo raggiunge con queste variabili d'ambiente:

| Variabile | Valore | Letta da |
|---|---|---|
| `SPRING_DATASOURCE_URL`, `_USERNAME`, `_PASSWORD` | `jdbc:postgresql://db:5432/app`, `app`, `app` | Spring Boot, senza configurazione |
| `DATABASE_URL` | `postgres://app:app@db:5432/app` | sqlx (`PgPool::connect(&std::env::var("DATABASE_URL")?)`) |

- Lo schema lo crea il servizio all'avvio, con le migrazioni: Flyway (`src/main/resources/db/migration`) in Kotlin, `sqlx::migrate!()` (cartella `migrations/`) in Rust.
- La build non ha né rete né database: i test interni usano H2 in Kotlin e in Java e implementazioni in memoria in Rust. Le verifiche HTTP girano sul database vero. H2 non accetta tutto ciò che accetta PostgreSQL: per esempio `INSERT … RETURNING` no, quindi in Java la chiave generata si legge con un `KeyHolder` di `JdbcClient`, che vale per entrambi.
- In Rust usa le funzioni di sqlx (`sqlx::query_as::<_, Riga>(…)`), non le macro `query!`, che durante la compilazione cercano il database.

## L'ambiente del servizio

Nel container delle verifiche un servizio a volte deve girare in modo diverso da come parte sul computer di chi lo usa: un'app locale, per esempio, ascolta solo su `127.0.0.1`, ma nel container deve ascoltare su tutte le interfacce, altrimenti il servizio `check` non la raggiunge. Il valore predefinito resta quello sicuro, nel codice, e il container lo cambia da fuori con le variabili d'ambiente di `environment` nel README:

```yaml
---
title: Il server locale sicuro
summary: …
environment:
  SERVER_ADDRESS: 0.0.0.0          # Spring Boot: server.address
  SERVER_PORT: '8080'
  CAMPANELLA_SERVER_TOKEN: token-delle-verifiche-di-learn-check
---
```

- Le variabili vanno al servizio `app` di `compose.yaml`, con `learn check` e con `learn run`, in ogni linguaggio dell'esercizio. La build, e quindi i test interni, non le vede.
- Valgono solo per un servizio (`http` o `browser`); i nomi sono in maiuscolo e non possono ripetere quelli del database. Dopo una modifica, `learn-author update-exercises` rigenera `compose.yaml`.
- Un valore che lo studente legge nel README, come un token noto alle verifiche, non è un segreto: serve solo nel container.

## Le verifiche HTTP

Ogni file `verify/*.hurl` è una serie di richieste [Hurl](https://hurl.dev) con le asserzioni sulle risposte. `{{base_url}}` è l'indirizzo del servizio dello studente:

```hurl
# GET /saluto risponde con un saluto personalizzato.
GET {{base_url}}/saluto?nome=Ada
HTTP 200
[Asserts]
header "Content-Type" contains "application/json"
jsonpath "$.messaggio" == "Ciao, Ada!"
```

- Le verifiche sono le stesse per tutti i linguaggi: descrivono il comportamento del servizio, non il codice.
- Ogni richiesta conta come una verifica. Se una fallisce, le successive vengono eseguite lo stesso. Lo studente vede il file e la riga di ogni asserzione fallita.
- I commenti (`#`) si leggono nei messaggi di errore: scrivili per lo studente.
- I file `verify/*.hurl` vengono eseguiti **in parallelo** sullo stesso servizio (`hurl --test`), mentre le richieste di un file vanno in ordine. Le verifiche che dipendono da dati salvati da richieste precedenti (creazione, lettura, cancellazione) devono stare nello stesso file, come in `courses/web-lato-server/exercises/catalogo-eventi/verify/catalogo.hurl`.

## I casi di input e output

Con `checks: [cases]` lo studente scrive un **programma** che legge dallo standard input e scrive sullo standard output, senza un servizio né un framework di test. `cases.yaml`, nella cartella dell'esercizio, elenca i casi: lo stesso file vale per tutti i linguaggi.

```yaml
cases:
  - name: un percorso con la query
    input: "GET /saluto?nome=Ada HTTP/1.1\n"
    output: |
      metodo: GET
      percorso: /saluto
      query: nome=Ada
      versione: HTTP/1.1
  - name: un metodo in minuscolo non è valido
    input: "get / HTTP/1.1\n"
    output: ""
    exit: 2
```

| Campo | Significato |
|---|---|
| `name` | il nome del caso, che lo studente legge quando fallisce; unico nel file |
| `input` | lo standard input del programma; vuoto se manca |
| `output` | ciò che il programma deve scrivere sullo standard output |
| `exit` | il codice di uscita atteso, da 0 a 255; 0 se manca |
| `compare` | `trimmed`, il predefinito, ignora gli spazi a fine riga (`\r` compreso) e le righe vuote finali; `exact` confronta ogni carattere |
| `timeout` | in cima al file, i secondi a disposizione di ogni caso, da 1 a 60; 10 se manca |

- Al massimo 50 casi. Il compilatore controlla il file con `cases.schema.json` (regola `schema/invalid`), lo stesso formato che legge il programma di verifica.
- La build dell'immagine compila il programma (`learn-build`) e lo avvia con `learn-run` una volta per ogni caso, senza rete. `learn-check-cases`, copiato dalla sua immagine (`ghcr.io/gmalnati/learn-check-cases`), confronta prima il codice di uscita e poi l'output, e scrive il report in `/reports/cases/report.json`. Lo standard error non conta, ma le sue ultime righe compaiono allo studente quando un caso fallisce: un'eccezione si vede.
- Un caso che supera il `timeout` viene fermato e fallisce; un programma che non parte è un errore.
- `args` dà al programma gli argomenti della riga di comando (al massimo 20): servono le toolchain `2026.3` o successive, il cui `learn-run` li passa.
- Con la toolchain di base: in Kotlin il programma è la funzione `main` di `src/main/kotlin/dev/learnlab/exercise/Main.kt` (il `build.gradle.kts` del template la avvia), in Rust `src/main.rs`. Con `checks: [tests, cases]` lo starter ha anche i suoi test interni.
- `cases.yaml` è nell'archivio dello studente: i casi fanno parte della consegna, e il README può invitare a leggerli.
- `learn check` mostra, per ogni caso fallito, il motivo e la prima riga diversa tra l'output atteso e quello ottenuto.

Il corso demo ne ha uno completo: [`courses/demo/exercises/la-riga-di-richiesta`](../../courses/demo/exercises/la-riga-di-richiesta).

## I test nel browser

Con `checks: [browser]` (di solito `[tests, browser]`) l'esercizio è un'app per il browser, e la verificano dei test [Playwright](https://playwright.dev) nella cartella `browser/` dell'esercizio, gli stessi per ogni linguaggio. Ogni file `*.spec.ts` è un test: apre la pagina, la usa come farebbe una persona e controlla che cosa mostra.

```ts
// browser/codici-di-stato.spec.ts
import { expect, test } from '@playwright/test';

test.describe('la pagina dei codici di stato', () => {
  test('spiega il codice mentre lo scrivi', async ({ page }) => {
    await page.goto('/');
    await page.getByLabel('Codice').fill('418');
    await expect(page.getByRole('status')).toHaveText('418: errore del client.');
  });
});
```

- `page.goto('/')` apre la pagina principale dell'app dello studente. I test girano in **Chromium**, l'unico browser dell'immagine, con la lingua `it-IT` e il fuso orario `Europe/Rome`, uno alla volta, senza ripetizioni: 30 secondi al massimo per un test, 10 per aprire una pagina, 5 per un'azione (`fill`, `click`) o un'attesa di `expect`. Così un elemento che manca fa fallire il test in pochi secondi, con ciò che Playwright stava aspettando.
- Trova gli elementi per **ruolo ed etichetta** (`getByRole`, `getByLabel`), come le tecnologie assistive, invece che per classe CSS o struttura: i test non dipendono da come lo studente scrive il markup, e un campo senza etichetta non passa.
- Dalla versione 2026.4 di `learn-check-browser` un test può controllare anche le regole di accessibilità con axe: `import AxeBuilder from '@axe-core/playwright'`, poi `expect((await new AxeBuilder({ page }).analyze()).violations).toEqual([])`. Il corso demo lo fa in `un-orario-senza-conflitti`.
- Ogni test è un controllo del report, con il nome dei blocchi `describe` e del test (`la pagina dei codici di stato › spiega il codice mentre lo scrivi`): scrivi nomi che lo studente capisca quando falliscono. Un test saltato (`test.skip`) non conta.
- `learn-author new exercise "Titolo" --languages node --checks tests,browser` crea `browser/<id>.spec.ts` d'esempio. Il compilatore controlla che `browser/` abbia almeno un `.spec.ts` quando l'esercizio dichiara `browser` e nessuno quando non lo dichiara.
- `compose.yaml` costruisce l'app (test interni e `learn-build`), la avvia con `learn-run` e poi esegue il servizio `browser`, con l'immagine `ghcr.io/gmalnati/learn-check-browser`: Playwright e Chromium, con la cartella `browser/` montata in sola lettura. Il verificatore aspetta che l'app risponda sulla porta 8080 e scrive il report in `/reports/browser/report.json`. Chromium apre in HTTPS ogni indirizzo `app` (`.app` è un dominio di primo livello con HSTS precaricato): per questo l'app ha anche il nome `app-under-test`, che è quello che usa il verificatore.
- `browser/` è nell'archivio dello studente: i test fanno parte della consegna, come le verifiche HTTP.
- Lo studente scarica circa 110 MB compressi per la toolchain e 340 per il verificatore, che hanno in comune i livelli di Node.

Il corso demo ne ha uno completo: [`courses/demo/exercises/i-codici-di-stato`](../../courses/demo/exercises/i-codici-di-stato).

## I controlli statici

Con `checks: [static]` l'esercizio si verifica leggendo i **file** del progetto, senza costruirli né avviarli: un Dockerfile, un workflow di GitHub Actions, dei manifest Kubernetes, un file Compose (ADR 033). `static.yaml`, alla radice dell'esercizio, dice quali strumenti leggono quali file; le regole del gruppo, in Rego, stanno in `policy/`.

```yaml
# static.yaml
tools:
  - tool: hadolint
    files: [Dockerfile]
  - tool: actionlint
    files: [.github/workflows/ci.yml]
  - tool: kubeconform
    files: ['k8s/*.yaml']
  - tool: conftest
    files: ['k8s/*.yaml']
    namespace: kubernetes
    rules:
      K1: Ogni container ha un'immagine con una versione, non latest.
```

| Strumento | Controlla | Opzioni |
|---|---|---|
| `hadolint` 2.15.1 | un Dockerfile: le regole `DL` e, negli script dei `RUN`, quelle `SC` di ShellCheck | `threshold`, il livello minimo che fa fallire (`error`, `warning`, il predefinito, `info`, `style`); `ignore`, codici da ignorare (`DL3008`); `trusted-registries` |
| `actionlint` 1.7.12 | un workflow di GitHub Actions: sintassi, espressioni, azioni e, con shellcheck 0.11.0, gli script dei passi | `ignore`, espressioni regolari sui messaggi da ignorare |
| `kubeconform` 0.8.0 | manifest Kubernetes contro gli schemi di Kubernetes 1.36.6, con i campi sconosciuti come errori | `skip`, i tipi senza schema da non controllare (le risorse personalizzate, come `IngressRoute`) |
| `conftest` 0.71.1 | qualunque file che conftest legge (YAML, JSON, Dockerfile, `.dockerignore`, TOML, HCL, …) con le regole `deny` di `policy/` | `namespace`, il `package` delle regole (`main` se manca); `rules`, obbligatorio; `parser`, quando il nome del file non basta (`dockerfile` per un file che non si chiama `Dockerfile`); `combine`, le regole vedono tutti i file insieme |

- `files` sono percorsi relativi allo starter e alla soluzione, con `/`: `*` e `?` valgono dentro una parte del percorso, `**` per più parti, e un nome che comincia con il punto non è speciale (`*/workflows/*.yml` trova `.github/workflows/ci.yml`). Al massimo 20 strumenti, 50 percorsi per strumento e 50 regole.
- **Le regole hanno un id.** Ogni messaggio di `deny` comincia con l'id di una regola di `rules` e i due punti, e `rules` dà a ogni id il testo che lo studente legge:

  ```rego
  package kubernetes

  import rego.v1

  deny contains sprintf("K1: il container %s usa %s, senza una versione", [c.name, c.image]) if {
  	input.kind == "Deployment"
  	some c in input.spec.template.spec.containers
  	endswith(c.image, ":latest")
  }
  ```

  `learn check` mostra ogni regola, superata o fallita, con il testo di `rules` e, quando fallisce, i messaggi senza l'id. Un messaggio con un id che `rules` non ha fallisce un controllo a parte. Le regole sono in Rego v1 (OPA 1.x, con `if` e `contains`); `conftest verify` e i file `*_test.rego` servono all'autore e non entrano nei controlli.
- conftest legge YAML 1.1: la chiave `on:` di un workflow diventa `"true"`. Una regola sui workflow legge gli eventi con `object.get(input, "on", object.get(input, "true", {}))`.
- Con `combine: true` l'input delle regole è l'elenco dei file, `{path, contents}`, e un file con più documenti separati da `---` ha l'elenco dei documenti: serve alle regole che confrontano più file, come un Service e il Deployment che seleziona. Senza, conftest valuta ogni documento da solo.
- **Le opzioni del progetto non contano.** Un `.hadolint.yaml`, un commento `# hadolint ignore=…`, la configurazione di actionlint o un `.shellcheckrc` dello studente non spengono una regola: valgono solo quelle di `static.yaml`. Restano i commenti `# shellcheck disable=…` dentro gli script dei passi: se contano, una regola Rego li vieta.
- **Un controllo per strumento e per file**, con il nome `hadolint › Dockerfile`, e **uno per regola**, con il nome `K1 Ogni container…`. Un percorso che non trova file fallisce («il file non c'è») e fa fallire anche le regole che lo leggono. Per un controllo fallito `learn check` mostra i problemi uno per riga, `file:riga:colonna: codice messaggio`: i messaggi degli strumenti sono in inglese, come li vede chi li lancia a mano.
- La consegna senza linguaggio sta in `files/`: `learn-author new exercise "Titolo" --languages files --checks static` crea `static.yaml`, una regola d'esempio in `policy/main.rego`, `Dockerfile`, `compose.yaml` e `Dockerfile.dockerignore`, senza un template da copiare. Con una sola cartella lo studente scarica l'esercizio con `learn get <corso> <esercizio>`, senza `--language`. Quando i file stanno in un progetto con il suo linguaggio, come il workflow di Scaffale, la cartella resta quella del linguaggio (`java/`), anche con la sola verifica `static`.
- Il `Dockerfile` generato esegue `learn-check-static` (immagine `ghcr.io/gmalnati/learn-check-static`) in uno stadio suo: copia `static.yaml`, `policy/` e il progetto, e lascia il report in `/reports/static/report.json`. Con altre verifiche lo stadio della toolchain ne copia solo il report, e i due stadi si costruiscono in parallelo. Il verificatore non ha rete e non esegue il codice dello studente.
- **`Dockerfile.dockerignore`**, accanto al `Dockerfile` e generato con lui, prende il posto del `.dockerignore` del progetto, che in un esercizio su Docker è dello studente e potrebbe togliere proprio i file da controllare: esclude solo `.git` e ciò che scrivono le build (`build`, `.gradle`, `.kotlin`, `target`, `node_modules`, `dist`). Un percorso di `files` non può cadere lì.
- Il compilatore controlla `static.yaml` con `static.schema.json` (`schema/invalid`, con la riga) e: `exercise/missing-static` ed `exercise/unused-static`; `exercise/missing-policy` ed `exercise/unused-policy` (le regole servono a conftest, e solo a lui); `exercise/static-namespace` (ogni `namespace` è il `package` di un file di `policy/`: altrimenti conftest non troverebbe regole e ognuna passerebbe); `exercise/static-rule-not-in-policy` (ogni id compare come `"<id>:` in quel `package`); `exercise/static-file-missing` (ogni percorso trova un file nella soluzione); `exercise/check-not-run` ed `exercise/check-not-declared` per `learn-check-static` nel `Dockerfile`; `exercise/missing-file` per `Dockerfile.dockerignore`; `exercise/files-without-toolchain` per una verifica che chiede una toolchain nella cartella `files/`. Dopo una modifica di `static.yaml` (per esempio la prima regola di conftest), rigenera i file con `learn-author update-exercises`.
- `static.yaml` e `policy/` sono nell'archivio dello studente: le regole fanno parte della consegna, e il README può presentarle in una tabella.
- Lo studente scarica circa 60 MB compressi per il verificatore, e niente altro se l'esercizio ha la sola verifica `static`. Un check dura pochi secondi.

Il corso demo ne ha uno completo: [`courses/demo/exercises/pronto-per-il-cluster`](../../courses/demo/exercises/pronto-per-il-cluster).

## I test ripetuti

Un test che dipende dalla data o dall'ordine dei test passa quasi sempre: fallisce l'ultimo giorno del mese, o quando un altro test non è girato prima di lui. `learn check` esegue i test una volta, alla data del computer dello studente, e non lo scopre. Con `repeat.yaml` alla radice dell'esercizio i test interni girano **una volta per ogni data** dichiarata, e ogni volta in un **ordine casuale** delle classi e dei metodi (ADR 032):

```yaml
dates: [2026-10-31T23:59:59, 2028-02-29T12:00:00, 2026-03-29T02:30:00]
timezone: Europe/Rome
required: [ReminderApiTest, ReservationApiTest]
```

| Campo | Significato |
|---|---|
| `dates` | da 1 a 6 date e ore locali diverse, `AAAA-MM-GGThh:mm:ss`, dal 2000 al 2099: un'esecuzione per ognuna |
| `timezone` | obbligatorio: il fuso orario IANA (`Europe/Rome`, `UTC`) in cui si leggono le date e girano le JVM dei test |
| `required` | facoltativo, al massimo 20 classi di test, con il nome semplice o completo: devono girare in ogni esecuzione senza test saltati, altrimenti l'esecuzione fallisce |
| `seed` | facoltativo: il seme dell'ordine della prima esecuzione, le altre usano i numeri successivi; senza, i semi sono casuali |

- Vale solo per **Java con il profilo web** (`learn-toolchain-java-web` dalla `2026.5`) e per la verifica `tests`: non con `kind: tests`. Si combina con `http`, `browser` e `cases`, che girano all'ora vera dopo i test. `learn-author new exercise "Titolo" --languages java --checks tests --repeat` crea un `repeat.yaml` d'esempio e un `Dockerfile` che esegue `learn-test-repeat` al posto di `learn-test`; per un esercizio esistente, scrivi `repeat.yaml` e rigenera i file con `learn-author update-exercises`.
- La data la vedono **solo le JVM dei test**, con libfaketime: `LocalDate.now()`, `Instant.now()` e gli altri orologi di Java partono dalla data dell'esecuzione e vanno mille volte più piano, così `23:59:59` resta lo stesso giorno per tutti i test. Gradle, la compilazione e il servizio delle verifiche HTTP vedono l'ora vera. Un'ora che non esiste per l'ora legale va avanti (il 29 marzo 2026 alle 02:30 diventa le 03:30); di un'ora ripetuta vale la prima.
- L'ordine è quello degli ordinatori casuali di JUnit Jupiter, con un seme per esecuzione. Con `forkEvery = 1` ogni classe ha la sua JVM e cambia solo l'ordine dei metodi; un `@TestMethodOrder` esplicito tiene il suo ordine.
- Il nome di ogni test nei report dice l'esecuzione, così lo studente vede su quale data e con quale seme è fallito: `it.valleverde.scaffale.loan.ReminderApiTest > listsTheLoansDueTomorrow() [31/10/2026 23:59:59, seme 18560]: java.time.DateTimeException: …`. Una classe di `required` cancellata, rinominata o con un test `@Disabled` fallisce in ogni esecuzione: `ReminderApiTest > richiesta in ogni esecuzione [29/02/2028 12:00:00, seme 15062]: 1 test saltato (@Disabled?), …`.
- I semi casuali cambiano a ogni build del codice: un secondo `learn check` senza modifiche riusa l'immagine e ripete l'esito. Se lo starter fallisce solo per l'ordine, fissa `seed`, così `learn-author test-exercises` dà sempre lo stesso esito.
- Ogni data ripete tutti i test: con Scaffale (Spring Boot, 32 test, `forkEvery = 1`) un'esecuzione dura circa 35 secondi e tre date circa 110. Scegli poche date, ognuna con un motivo: l'ultimo secondo di un mese, un 29 febbraio, il cambio dell'ora legale.
- `repeat.yaml` è nell'archivio dello studente: le date fanno parte della consegna, e il README può spiegarle. Il compilatore controlla il file con `repeat.schema.json` (regola `schema/invalid`, con la riga) e che il `Dockerfile` esegua `learn-test-repeat` solo quando c'è (`exercise/repeat-not-run`, `exercise/missing-repeat`); con un altro profilo o linguaggio segnala `exercise/repeat-toolchain`, senza `tests` `exercise/repeat-without-tests`, in un esercizio sui test `exercise/repeat-with-mutants`.

## Cache di compilazione

Il `Dockerfile` generato monta le cache di compilazione della toolchain (ADR 027): ciò che la build compila resta tra una verifica e l'altra, e dalla seconda si ricompila solo quello che è cambiato. Su un esercizio grande il secondo `learn check` passa da 22 a 10 secondi in Rust e da 26 a 22 in Kotlin.

- **Rust:** una cache per versione della toolchain, comune a tutti gli esercizi, che parte dalle dipendenze già compilate dell'immagine. Cargo decide che cosa ricompilare dalle date dei file: per non usare mai un binario vecchio, la build dà a ogni file l'ora corrente.
- **Kotlin:** lo stato del progetto (`build/`, `.gradle/`, `.kotlin/`), uno per progetto Compose. `compose.yaml` passa il nome del progetto come `CACHE_ID`; `learn check` ne usa uno per esercizio e linguaggio. La home di Gradle resta nell'immagine: in una cache rallentava le build in parallelo.
- **Kotlin e Java, i test:** dalle toolchain `2026.4` (`2026.6` per il profilo web Java) `learn-test` esegue `gradle test --rerun`: i test girano anche quando Gradle li crede aggiornati. Senza, un test che legge un file che non è un ingresso di Gradle (un workflow, un Dockerfile) dava l'esito del check precedente quando cambiava solo quel file (ADR 033). Costa la durata dei test solo in quel caso. Con le toolchain precedenti, dichiara quei file fra gli ingressi del task: `tasks.test { inputs.files(fileTree(".github/workflows")) }`.
- **Node:** nessuna cache: test interni e build di un esercizio durano pochi secondi.
- Le cache stanno nel builder di Docker, non nelle immagini: `docker buildx du` mostra quanto occupano, `learn clean` le cancella (la verifica successiva ricompila tutto).
- `learn-author test-exercises` dà a soluzione e starter di un esercizio le stesse cache; con `--jobs`, le build in parallelo ne hanno una ciascuna.
- Gli esercizi scritti prima delle cache si aggiornano con `learn-author update-exercises [corso]`, che rigenera `Dockerfile` e `compose.yaml` di ogni esercizio dalle verifiche del README, tiene il database e scrive solo i file che cambiano.

## Esito di una verifica

`learn check` (e `learn-author test-exercises`) costruisce l'immagine dell'esercizio, che compila il codice ed esegue i **test interni**, in un esercizio sui test le **versioni difettose**, con `cases` il programma su ogni **caso di input e output** e, con `static`, i **controlli statici** dei file. Con `http` avvia poi il servizio ed esegue le **verifiche HTTP**, con `browser` serve l'app ed esegue i **test nel browser**. L'esercizio è superato quando:

- il codice si compila;
- ogni verifica dichiarata ha almeno un controllo e nessuno fallisce: almeno un test interno, una richiesta HTTP, una versione difettosa, un caso, un controllo statico, un test nel browser;
- fanno eccezione i test interni, che possono mancare se l'esercizio dichiara anche altre verifiche: lo starter di un servizio può non averne.

L'esito (test e verifiche superati e falliti, durata, versioni delle immagini, numero del tentativo) arriva alla piattaforma (EXE-04). L'esercizio risulta completato quando una verifica è superata (ADR 008).

Prima di verificare un servizio, lo studente può avviarlo con `learn run`: costruisce la stessa immagine (test interni compresi, anche se falliscono) e pubblica la porta 8080 del servizio `app` su `127.0.0.1` (`--port` per cambiarla), così le lezioni possono chiedere di provarlo con `curl`. Il servizio `check` non parte e nessun esito viene inviato. Se `compose.yaml` ha altri servizi da cui `app` dipende (es. un database), partono anche quelli.

## Immagini e versioni

Le immagini toolchain e di verifica sono pubblicate su `ghcr.io/gmalnati/learn-<nome>:<versione>` per amd64 e arm64, come pacchetti pubblici: si scaricano senza accesso a GitHub (ADR 016). Ogni immagine ha la sua versione (es. `2026.2`), e il `Dockerfile` e il `compose.yaml` di ogni esercizio nominano le versioni esatte delle immagini che usano (ADR 027).

- Una versione pubblicata **non cambia mai** (EXE-05): un esercizio verificato oggi si comporta allo stesso modo l'anno prossimo.
- Un'immagine cambiata ha una versione nuova, pubblicata accanto alle precedenti. Le altre immagini restano come sono, e chi le ha già scaricate non scarica niente.
- L'**edizione** è l'insieme di versioni che `learn-author new exercise` scrive negli esercizi nuovi: le ultime pubblicate quando è uscita la versione di `learn-author` che usi. Un esercizio esistente tiene le versioni dei suoi file: per adottarne una nuova rigenera `Dockerfile` e `compose.yaml` con `learn-author update-exercises` e, se serve, aggiorna i progetti di starter e soluzione.
- In un altro registry: `LEARN_REGISTRY=registry.example.org/learnlab` vale sia per `learn check` sia per `docker compose`.

`AGENTS.md`, se c'è, finisce alla radice dell'esercizio scaricato, accanto al README, e nell'archivio della soluzione: è il file di istruzioni che gli agenti AI leggono all'avvio (Codex dalla radice del repository Git alla cartella in cui parte, Claude Code quando il progetto non ha un `CLAUDE.md`). Lo usa il corso *Sviluppare software con gli agenti AI* per dare all'agente dello studente le regole del progetto; non entra nelle verifiche.

## Esercizi presi da un altro corso

Un corso tradotto usa gli esercizi del corso originale invece di copiarli (ADR 029). Nel suo `course.yaml` dichiara `exercisesFrom: ../<corso>` e in `exercises/<id>/` tiene solo i testi tradotti:

```
courses/server-side-web/exercises/rotte-e-parametri/
└── README.md        title e summary tradotti, poi la consegna
```

```markdown
---
title: Routes and parameters
summary: Implement three endpoints with parameters in the path and in the query.
---

# Routes and parameters
…
```

- Il frontmatter ha solo `title` e `summary`; `kind`, `checks`, `duration`, `solution` e `profiles` vengono dal README originale. Un esercizio sui test ha anche il suo `mutants.yaml` tradotto, con le stesse versioni difettose (regola `exercise/mutants-translation`), e un esercizio con `AGENTS.md` la sua traduzione: senza, l'archivio non ne ha.
- Altri file o cartelle sono un errore (`exercise/shared-file`), e ogni cartella deve corrispondere a un esercizio del corso d'origine (`exercise/not-shared`). Gli id restano quelli del corso d'origine.
- L'archivio dello studente ha il README tradotto, l'`AGENTS.md` della traduzione e, per un esercizio sui test, le descrizioni tradotte; il resto è identico a quello del corso d'origine. `cases.yaml` non si traduce.
- `learn-author test-exercises` e `update-exercises` saltano questi esercizi: si verificano e si rigenerano nel corso d'origine, e la CI li verifica una volta sola. Una modifica al README originale va riportata nella traduzione.

## Esercizi sui test

In un esercizio con `kind: tests` lo studente non scrive il servizio ma **i suoi test** (ADR 024). Il servizio è già scritto e funziona; l'autore prepara alcune **versioni difettose** del codice, ognuna con un bug. `learn check` esegue i test dello studente sul codice corretto, dove devono passare, e poi su ogni versione difettosa, dove almeno un test deve fallire. L'esercizio è superato quando le scopre tutte e passano le altre verifiche dichiarate. Senza `http` (`checks: [tests, mutants]`) il codice da testare non deve essere un servizio: può essere una libreria.

```sh
learn-author new exercise "I test del saluto" --tests
```

```
exercises/test-del-saluto/
├── README.md                kind: tests
├── mutants.yaml             le versioni difettose, con il bug di ognuna
├── verify/                  verifiche HTTP: il servizio deve restare quello corretto
└── kotlin/
    ├── Dockerfile           generato: esegue anche learn-test-mutants
    ├── compose.yaml         generato: passa mutants/ alla build
    ├── starter/             il codice corretto e un test d'esempio
    ├── solution/            lo stesso codice e i test dell'autore
    └── mutants/
        ├── nome-vuoto/      i file che sostituiscono quelli corretti, con lo stesso percorso
        │   └── src/main/kotlin/dev/learnlab/exercise/Saluti.kt
        └── …
```

```yaml
# mutants.yaml: la descrizione compare allo studente quando i suoi test non scoprono la versione.
nome-vuoto: 'Un nome vuoto o di soli spazi non diventa «mondo»: il saluto è «Ciao, !».'
spazi: Non toglie gli spazi intorno al nome.
lunghezza: Accetta anche un nome di 31 caratteri, ma il limite è 30.
```

- Ogni versione difettosa ha la sua cartella in **ogni** linguaggio, con almeno un file. Al più 10 versioni: ognuna riesegue i test, e la verifica si allunga.
- Una versione sostituisce solo codice che esiste sia in `starter/` sia in `solution/`, mai i test: in Kotlin i test stanno in `src/test/`, in Rust in `tests/`. Per questo in Rust il servizio è una libreria (`src/lib.rs`, con le regole in un modulo a parte) e `src/main.rs` lo avvia.
- Metti le regole da testare in un file separato dal livello HTTP: le versioni difettose cambiano quel file e restano piccole.
- Scrivi bug realistici, ognuno scoperto da almeno un test ragionevole: un caso limite, una condizione invertita, un valore predefinito dimenticato. Il README elenca le regole del servizio, così lo studente sa che cosa testare.
- Le versioni difettose sono nell'archivio dello studente, perché la verifica avviene senza rete. Il README spiega che guardarle toglie senso all'esercizio.
- `learn-author test-exercises` controlla che i test della soluzione scoprano tutte le versioni difettose e che lo starter non superi l'esercizio. Una versione che non compila è un errore dell'autore.

Il corso demo ne ha uno completo: [`courses/demo/exercises/test-del-saluto`](../../courses/demo/exercises/test-del-saluto).

### Una barriera di qualità (Java, profilo web)

Con `learn-toolchain-java-web` dalla `2026.4` (ADR 031) lo studente può consegnare, invece dei test, le **regole e le soglie** che fermano il codice sbagliato: stile con Checkstyle, bug comuni con SpotBugs, copertura con JaCoCo. L'esercizio è `kind: tests` con `checks: [tests, mutants]`, e le versioni difettose sono codice che le regole devono fermare: un ciclo senza graffe, un valore ignorato, un metodo senza test.

- Le regole girano in `learn check` solo se il `build.gradle.kts` le collega ai test: `tasks.test { finalizedBy(tasks.checkstyleMain, tasks.spotbugsMain, tasks.jacocoTestCoverageVerification) }`. Il blocco di configurazione degli strumenti è in «Il progetto».
- Una versione difettosa è scoperta quando un test fallisce oppure quando i test passano ma la build fallisce dopo di loro, fermata da una regola. Una versione che non compila resta un errore dell'autore, come negli altri esercizi sui test.
- Sul codice corretto la build deve passare tutta, regole comprese: se una regola lo ferma, la verifica `tests` fallisce con un test chiamato come il task (per esempio `gradle > :jacocoTestCoverageVerification`) e la causa data da Gradle, e le versioni difettose non vengono provate.
- Lo starter ha gli strumenti senza regole collegate, o con soglie troppo basse: le versioni difettose passano, e lo studente scrive le regole che le fermano senza fermare il codice corretto. `learn-author test-exercises` lo controlla come per gli altri esercizi sui test.

## Verificare gli esercizi (EXE-07)

```sh
learn-author test-exercises                 # tutti gli esercizi del corso
learn-author test-exercises --exercise hello-http
learn-author test-exercises --jobs 3        # tre verifiche alla volta
```

Per ogni linguaggio, la soluzione deve superare le verifiche e lo starter **no**: uno starter che passa già non insegna nulla. Il comando esce con codice 1 se un esito è diverso da quello atteso. Va eseguito nella CI del repository del corso, come fa la CI di Learn2K per i suoi corsi.

Lo starter, di solito, si costruisce e non supera le verifiche: uno starter che non si costruisce è un errore dell'autore. A volte però l'esercizio è proprio farlo costruire nel container: una build che funziona solo sul computer di chi l'ha scritta, con una versione mobile o una libreria installata a mano, si ferma nel container senza rete. Il README lo dichiara con `starter: does-not-build` (senza, vale `builds`): `learn-author test-exercises` si aspetta allora che lo starter non si costruisca e segnala quello che si costruisce. Lo studente vede l'errore della build con `learn check`, come su una macchina pulita.

Una verifica lascia ferma gran parte della CPU: mentre il servizio parte o Hurl aspetta una risposta, il computer non lavora. Con `--jobs N` ne girano N alla volta, ognuna nel suo progetto Docker Compose, e gli esiti compaiono man mano che finiscono. Ogni verifica in più chiede memoria a Docker, circa 2 GB per un esercizio Kotlin: la CI di Learn2K ne esegue tre alla volta.

Per una verifica a mano di un servizio con Docker Compose, dalla cartella del linguaggio:

```sh
SRC=solution docker compose up --build --exit-code-from check check
docker compose down
```

Senza `http`, `SRC=solution docker compose build app` esegue i test e i casi durante la build e ne mostra l'output; l'esito, letto dai report, lo dà `learn-author test-exercises`.
