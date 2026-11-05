---
title: Pronto per il cluster
summary: Correggi il Dockerfile, il workflow e i manifest Kubernetes del servizio di saluto finché gli strumenti e le regole del gruppo non trovano più problemi.
# Una consegna di soli file, senza codice da compilare: la cartella files/, senza toolchain. La
# verificano gli strumenti di static.yaml e le regole di policy/ (ADR 033).
checks: [static]
duration: 30
solution: { after-attempts: 2 }
---

# Pronto per il cluster

Il servizio di saluto è pronto, ma i file che lo portano in produzione no. Nella cartella `starter/`
trovi:

- `Dockerfile`, che mette il jar del servizio in un'immagine;
- `.github/workflows/ci.yml`, il workflow di GitHub Actions che costruisce l'immagine;
- `k8s/deployment.yaml` e `k8s/service.yaml`, i manifest che lo avviano in un cluster Kubernetes.

Correggili. Non serve costruire l'immagine né avere un cluster: `learn check` legge i file con gli
strumenti che si usano in un gruppo di lavoro e con le sue regole.

| Strumento | Che cosa controlla |
|---|---|
| hadolint | il Dockerfile: immagini senza versione, comandi scritti in modo fragile |
| actionlint | il workflow: sintassi, espressioni e, con shellcheck, gli script dei passi |
| kubeconform | i manifest, contro gli schemi di Kubernetes 1.36: un campo scritto male è un errore |
| conftest | le regole del gruppo, qui sotto, scritte in Rego nella cartella `policy/` |

| Regola | |
|---|---|
| D1 | Il servizio gira con un utente senza privilegi, non come root. |
| K1 | Ogni container ha un'immagine con una versione, non `latest`. |
| K2 | Ogni container dichiara richieste e limiti di CPU e memoria. |
| K3 | Ogni container ha una `readinessProbe` HTTP: il servizio risponde su `/saluto`, porta 8080. |
| K4 | Ogni Service seleziona i pod di un Deployment. |

Il `.dockerignore` del progetto tiene fuori dall'immagine il workflow e i manifest, come si conviene:
`learn check` li legge lo stesso.

## Come viene verificato

```sh
learn check
```

`learn check` esegue gli strumenti sui file di `starter/` e mostra, per ogni file e per ogni regola,
se il controllo è superato. Per un controllo fallito indica il file, la riga quando lo strumento la
conosce, il codice della regola (`DL3007`, `SC2086`) e il messaggio: i messaggi degli strumenti sono in
inglese, quelli delle regole del gruppo in italiano. L'esercizio è superato quando passano tutti.

Le opzioni dei file del progetto non contano: un `.hadolint.yaml` o un commento `# hadolint ignore`
non spengono una regola.
