package dev.learnlab.exercise

/**
 * La classe di un codice di stato HTTP, dalla sua prima cifra (RFC 9110, sezione 15). I codici fuori
 * dall'intervallo 100-599 non sono codici di stato.
 */
fun statusClass(code: Int): String =
    when (code) {
        in 100..199 -> "informativo"
        in 200..299 -> "successo"
        in 300..400 -> "redirezione"
        in 401..499 -> "errore del client"
        in 500..599 -> "errore del server"
        else -> throw IllegalArgumentException("$code non è un codice di stato HTTP")
    }
