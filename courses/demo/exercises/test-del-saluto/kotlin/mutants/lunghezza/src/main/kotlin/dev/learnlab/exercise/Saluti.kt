package dev.learnlab.exercise

/** Il nome più lungo che il servizio saluta. */
const val LUNGHEZZA_MASSIMA = 30

/**
 * Il messaggio di saluto per [nome], senza gli spazi intorno; senza nome, o con un nome vuoto,
 * saluta il mondo. Null quando il nome supera [LUNGHEZZA_MASSIMA] caratteri.
 */
fun messaggio(nome: String?): String? {
    val pulito = nome?.trim().orEmpty()
    if (pulito.length > LUNGHEZZA_MASSIMA + 1) return null
    return "Ciao, ${pulito.ifEmpty { "mondo" }}!"
}
