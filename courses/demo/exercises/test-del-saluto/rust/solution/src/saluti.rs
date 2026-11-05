//! Le regole del saluto, separate dal livello HTTP.

/// Il nome più lungo che il servizio saluta.
pub const LUNGHEZZA_MASSIMA: usize = 30;

/// Il messaggio di saluto per `nome`, senza gli spazi intorno; senza nome, o con un nome vuoto,
/// saluta il mondo. `None` quando il nome supera [`LUNGHEZZA_MASSIMA`] caratteri.
pub fn messaggio(nome: Option<&str>) -> Option<String> {
    let pulito = nome.unwrap_or("").trim();
    if pulito.chars().count() > LUNGHEZZA_MASSIMA {
        return None;
    }
    let nome = if pulito.is_empty() { "mondo" } else { pulito };
    Some(format!("Ciao, {nome}!"))
}
