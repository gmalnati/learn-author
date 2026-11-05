//! I test della classe dei codici di stato: scrivi qui quelli che mancano. Le versioni difettose
//! cambiano solo src/, mai questa cartella.

use app::status::status_class;

#[test]
fn duecento_e_un_successo() {
    assert_eq!(status_class(200), Ok("successo"));
}
