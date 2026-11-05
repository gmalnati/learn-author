//! I test della classe dei codici di stato. I bug si nascondono ai confini degli intervalli, e i
//! test controllano il primo e l'ultimo codice di ogni classe.

use app::status::status_class;

#[test]
fn ogni_codice_ha_la_sua_classe_anche_ai_confini() {
    let cases = [
        (100, "informativo"),
        (199, "informativo"),
        (200, "successo"),
        (299, "successo"),
        (300, "redirezione"),
        (399, "redirezione"),
        (400, "errore del client"),
        (499, "errore del client"),
        (500, "errore del server"),
        (599, "errore del server"),
    ];
    for (code, expected) in cases {
        assert_eq!(status_class(code), Ok(expected), "codice {code}");
    }
}

#[test]
fn i_codici_fuori_dall_intervallo_non_sono_codici_di_stato() {
    assert!(status_class(99).is_err());
    assert!(status_class(600).is_err());
}
