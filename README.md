# learn-author

`learn-author` è lo strumento di chi scrive i corsi di [Learn2K](https://learn2k.it): crea corsi, lezioni ed esercizi da un modello, controlla i sorgenti, mostra il corso nel browser come lo vedranno gli studenti e lo pubblica.

*English below.*

## Installazione

Scarica il file del tuo sistema dall'[ultima release](https://github.com/gmalnati/learn-author/releases/latest):

| Sistema | File |
|---|---|
| macOS, Apple Silicon | `learn-author-aarch64-apple-darwin` |
| macOS, Intel | `learn-author-x86_64-apple-darwin` |
| Linux, x86_64 | `learn-author-x86_64-unknown-linux-musl` |
| Linux, ARM | `learn-author-aarch64-unknown-linux-musl` |
| Windows, x86_64 | `learn-author-x86_64-pc-windows-msvc.exe` |
| Windows, ARM | `learn-author-aarch64-pc-windows-msvc.exe` |

Rinominalo in `learn-author` (`learn-author.exe` su Windows) e mettilo in una cartella del `PATH`. Per esempio, su un Mac con Apple Silicon:

```sh
curl -fLo learn-author https://github.com/gmalnati/learn-author/releases/latest/download/learn-author-aarch64-apple-darwin
chmod +x learn-author
sudo mv learn-author /usr/local/bin/
```

I file per macOS sono firmati e notarizzati da Apple. Quelli per Windows non sono firmati: la prima volta SmartScreen può chiedere una conferma.

## Il primo avvio

Il primo comando scarica, una volta sola, Node.js 24 da nodejs.org e il programma di questa versione di `learn-author`: tra 45 e 65 MB secondo il sistema, circa 160 MB su disco. Prima di usarli ne controlla le impronte SHA-256. Da quel momento `learn-author` funziona anche senza rete, tranne `publish`.

## Comandi

| Comando | Effetto |
|---|---|
| `learn-author new course <cartella>` | crea un corso da un modello; `new` crea anche moduli, lezioni, quiz, checkpoint, banche di domande, esercizi e simulatori |
| `learn-author validate [corso]` | controlla i sorgenti e segnala gli errori con file e riga |
| `learn-author preview [corso]` | mostra il corso nel browser su http://localhost:5175, aggiornato a ogni salvataggio |
| `learn-author build [corso]` | compila il corso, come lo riceve la piattaforma |
| `learn-author publish [corso]` | pubblica il corso come nuova versione su learn2k.it: serve un account di docente, e l'accesso avviene dal browser con un codice |
| `learn-author test-exercises [corso]` | verifica gli esercizi in Docker: la soluzione supera le verifiche, il punto di partenza no |
| `learn-author update` | installa l'ultima versione; `update --check` controlla soltanto |

`learn-author --help` e `learn-author <comando> --help` elencano tutte le opzioni. Il corso è la cartella corrente, se non ne indichi un'altra. I messaggi sono in italiano, oppure in inglese con `--lang en`.

## Proxy, copie dei file e altre impostazioni

| Variabile | Effetto |
|---|---|
| `HTTPS_PROXY` | il proxy da cui passa la rete |
| `LEARN_AUTHOR_NODE` | il percorso di un Node.js 24 o successivo già installato, al posto di quello scaricato. Serve sulle distribuzioni Linux con musl, come Alpine, dove il Node.js di nodejs.org non funziona |
| `LEARN_AUTHOR_NODE_MIRROR` | una copia di `https://nodejs.org/dist` |
| `LEARN_AUTHOR_DOWNLOAD` | una copia di `https://github.com/gmalnati/learn-author/releases/download` |
| `LEARN_AUTHOR_HOME` | la cartella dei file scaricati, al posto di quella predefinita: `~/Library/Application Support/dev.LearnLab.learn-author` su macOS, `~/.local/share/learn-author` su Linux, `%LOCALAPPDATA%\LearnLab\learn-author\data` su Windows |

Le versioni precedenti di Node.js e del programma si cancellano quando una nuova versione è installata. Per togliere `learn-author`, cancella il file e quella cartella.

## Licenza

`learn-author` è gratuito e si distribuisce con la [PolyForm Strict License 1.0.0](https://polyformproject.org/licenses/strict/1.0.0), con un permesso aggiuntivo di TonicMinds s.r.l. In breve (il testo che vale è in [LICENSE](LICENSE)):

- puoi usarlo per scopi non commerciali, per esempio per studio, nella scuola o in un ente senza scopo di lucro;
- puoi usarlo anche per lavoro, ma solo per preparare corsi e pubblicarli su un'istanza di Learn2K gestita da TonicMinds s.r.l. o da chi ha il suo permesso;
- non puoi modificarlo né ridistribuirlo: chi lo vuole lo scarica da qui.

I corsi che scrivi restano tuoi. I componenti di terzi inclusi mantengono le loro licenze, elencate in `THIRD-PARTY-NOTICES.txt` di ogni release.

## English

`learn-author` is the tool of the authors of [Learn2K](https://learn2k.it) courses: it creates courses from templates, checks their sources, previews them in the browser and publishes them.

Download the file for your system from the [latest release](https://github.com/gmalnati/learn-author/releases/latest), rename it to `learn-author` (`learn-author.exe` on Windows) and put it on your `PATH`. Its first run downloads Node.js 24 from nodejs.org and the program of its version (45–65 MB), and checks their SHA-256; after that it works offline, except `publish`. Run `learn-author --help --lang en` for the commands, and `learn-author update` for new versions. The variables above set a proxy, copies of the downloads, an installed Node.js 24 (needed on musl distributions such as Alpine) and the folder of the downloads.

`learn-author` is free under the PolyForm Strict License 1.0.0 with an additional permission of TonicMinds s.r.l. ([LICENSE](LICENSE)): noncommercial use, and commercial use only to prepare courses and publish them on a Learn2K instance run or authorized by TonicMinds s.r.l.; no changes and no redistribution. Your courses remain yours.
