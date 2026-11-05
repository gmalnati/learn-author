//! Un test per regola del README: ognuno scopre almeno una versione difettosa.

use app::saluti::{LUNGHEZZA_MASSIMA, messaggio};
use axum::{body::Body, http::Request};
use http_body_util::BodyExt;
use serde_json::{Value, json};
use tower::ServiceExt;

#[test]
fn saluta_per_nome() {
    assert_eq!(messaggio(Some("Ada")).as_deref(), Some("Ciao, Ada!"));
}

#[test]
fn senza_nome_o_con_un_nome_vuoto_saluta_il_mondo() {
    for nome in [None, Some(""), Some("   ")] {
        assert_eq!(messaggio(nome).as_deref(), Some("Ciao, mondo!"), "nome: {nome:?}");
    }
}

#[test]
fn toglie_gli_spazi_intorno_al_nome() {
    assert_eq!(messaggio(Some("  Ada ")).as_deref(), Some("Ciao, Ada!"));
}

#[test]
fn saluta_un_nome_lungo_fino_al_limite_e_rifiuta_quelli_piu_lunghi() {
    assert!(messaggio(Some(&"a".repeat(LUNGHEZZA_MASSIMA))).is_some());
    assert_eq!(messaggio(Some(&"a".repeat(LUNGHEZZA_MASSIMA + 1))), None);
}

async fn get(uri: &str) -> (u16, Vec<u8>) {
    let response = app::app().oneshot(Request::get(uri).body(Body::empty()).unwrap()).await.unwrap();
    let status = response.status().as_u16();
    (status, response.into_body().collect().await.unwrap().to_bytes().to_vec())
}

#[tokio::test]
async fn risponde_con_il_messaggio_in_json() {
    let (status, body) = get("/saluto?nome=Ada").await;
    assert_eq!(status, 200);
    assert_eq!(serde_json::from_slice::<Value>(&body).unwrap(), json!({ "messaggio": "Ciao, Ada!" }));
}

#[tokio::test]
async fn rifiuta_un_nome_troppo_lungo_con_400() {
    let (status, _) = get(&format!("/saluto?nome={}", "a".repeat(LUNGHEZZA_MASSIMA + 1))).await;
    assert_eq!(status, 400);
}
