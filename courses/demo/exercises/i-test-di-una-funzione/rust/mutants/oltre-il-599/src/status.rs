//! La classe di un codice di stato HTTP, dalla sua prima cifra (RFC 9110, sezione 15). I codici
//! fuori dall'intervallo 100-599 non sono codici di stato.

pub fn status_class(code: u16) -> Result<&'static str, String> {
    match code {
        100..=199 => Ok("informativo"),
        200..=299 => Ok("successo"),
        300..=399 => Ok("redirezione"),
        400..=499 => Ok("errore del client"),
        500..=999 => Ok("errore del server"),
        _ => Err(format!("{code} non è un codice di stato HTTP")),
    }
}
