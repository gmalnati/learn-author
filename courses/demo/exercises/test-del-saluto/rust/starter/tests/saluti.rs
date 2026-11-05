//! I test del servizio di saluto: scrivi qui quelli che mancano, uno per regola del README. Le
//! versioni difettose cambiano solo src/, mai questa cartella.

use app::saluti::messaggio;

#[test]
fn saluta_per_nome() {
    assert_eq!(messaggio(Some("Ada")).as_deref(), Some("Ciao, Ada!"));
}
