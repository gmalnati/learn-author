//! Le regole del saluto, separate dal livello HTTP.

/// Il nome più lungo che il servizio saluta.
pub const LUNGHEZZA_MASSIMA: usize = 30;

/// Il messaggio di saluto per `nome`, senza gli spazi intorno; senza nome, o con un nome vuoto,
/// saluta il mondo. `None` quando il nome supera [`LUNGHEZZA_MASSIMA`] caratteri.
pub fn messaggio(nome: Option<&str>) -> Option<String> {
    let pulito = nome.map(str::trim);
    if pulito.is_some_and(|nome| nome.chars().count() > LUNGHEZZA_MASSIMA) {
        return None;
    }
    Some(format!("Ciao, {}!", pulito.unwrap_or("mondo")))
}
