use axum::{Json, Router, extract::Query, routing::get};
use serde::{Deserialize, Serialize};

#[derive(Deserialize)]
struct Parametri {
    nome: Option<String>,
}

#[derive(Serialize)]
struct Saluto {
    messaggio: String,
}

async fn saluto(Query(parametri): Query<Parametri>) -> Json<Saluto> {
    let nome = parametri.nome.as_deref().unwrap_or("mondo");
    Json(Saluto { messaggio: format!("Ciao, {nome}!") })
}

fn app() -> Router {
    Router::new().route("/saluto", get(saluto))
}

#[tokio::main]
async fn main() {
    let listener = tokio::net::TcpListener::bind("0.0.0.0:8080").await.unwrap();
    axum::serve(listener, app()).await.unwrap();
}

#[cfg(test)]
mod tests {
    use super::*;
    use axum::{body::Body, http::Request};
    use http_body_util::BodyExt;
    use serde_json::{Value, json};
    use tower::ServiceExt;

    async fn messaggio(uri: &str) -> Value {
        let response = app().oneshot(Request::get(uri).body(Body::empty()).unwrap()).await.unwrap();
        assert_eq!(response.status(), 200);
        let body = response.into_body().collect().await.unwrap().to_bytes();
        serde_json::from_slice::<Value>(&body).unwrap()
    }

    #[tokio::test]
    async fn saluta_per_nome() {
        assert_eq!(messaggio("/saluto?nome=Ada").await, json!({ "messaggio": "Ciao, Ada!" }));
    }

    #[tokio::test]
    async fn senza_nome_saluta_il_mondo() {
        assert_eq!(messaggio("/saluto").await, json!({ "messaggio": "Ciao, mondo!" }));
    }
}
