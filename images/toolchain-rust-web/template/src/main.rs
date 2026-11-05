use axum::{Json, Router, routing::get};
use serde_json::{Value, json};

async fn ping() -> Json<Value> {
    Json(json!({ "status": "ok" }))
}

fn app() -> Router {
    Router::new().route("/ping", get(ping))
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
    use tower::ServiceExt;

    #[tokio::test]
    async fn answers_ping() {
        let response = app().oneshot(Request::get("/ping").body(Body::empty()).unwrap()).await.unwrap();
        let body = response.into_body().collect().await.unwrap().to_bytes();
        assert_eq!(serde_json::from_slice::<Value>(&body).unwrap(), json!({ "status": "ok" }));
    }
}
