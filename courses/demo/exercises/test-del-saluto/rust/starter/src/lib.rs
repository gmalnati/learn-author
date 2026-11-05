//! Il servizio di saluto: le regole sono in [`saluti`], qui c'è solo il livello HTTP. È una
//! libreria perché i test in `tests/` possano usarla.

pub mod saluti;

use axum::{Json, Router, extract::Query, http::StatusCode, routing::get};
use serde::{Deserialize, Serialize};

#[derive(Deserialize)]
struct Parametri {
    nome: Option<String>,
}

#[derive(Serialize)]
struct Saluto {
    messaggio: String,
}

async fn saluto(Query(parametri): Query<Parametri>) -> Result<Json<Saluto>, StatusCode> {
    saluti::messaggio(parametri.nome.as_deref())
        .map(|messaggio| Json(Saluto { messaggio }))
        .ok_or(StatusCode::BAD_REQUEST)
}

pub fn app() -> Router {
    Router::new().route("/saluto", get(saluto))
}
