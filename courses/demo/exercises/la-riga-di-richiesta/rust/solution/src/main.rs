//! Reads the first line of an HTTP request from the standard input and writes its parts.

use std::io::{self, Read};
use std::process::ExitCode;

/// The parts of the first line of an HTTP request, e.g. `GET /saluto?nome=Ada HTTP/1.1`.
struct RequestLine<'a> {
    method: &'a str,
    path: &'a str,
    /// What follows `?` in the target; `None` when there is nothing.
    query: Option<&'a str>,
    version: &'a str,
}

const VERSIONS: [&str; 2] = ["HTTP/1.0", "HTTP/1.1"];

/// The parts of `text`, one request line with its end (`\r\n` in HTTP, or just `\n`), or `None`
/// when it is not a valid request line.
fn parse_request_line(text: &str) -> Option<RequestLine<'_>> {
    let line = text.strip_suffix('\n').unwrap_or(text);
    let line = line.strip_suffix('\r').unwrap_or(line);
    // Two spaces in a row give an empty part, and four parts.
    let parts: Vec<&str> = line.split(' ').collect();
    let [method, target, version] = parts[..] else {
        return None;
    };
    let valid_method = !method.is_empty() && method.bytes().all(|byte| byte.is_ascii_uppercase());
    if !valid_method || !target.starts_with('/') || !VERSIONS.contains(&version) {
        return None;
    }
    let (path, query) = match target.split_once('?') {
        Some((path, query)) => (path, Some(query).filter(|query| !query.is_empty())),
        None => (target, None),
    };
    Some(RequestLine {
        method,
        path,
        query,
        version,
    })
}

fn main() -> ExitCode {
    let mut text = String::new();
    let request = match io::stdin().read_to_string(&mut text) {
        Ok(_) => parse_request_line(&text),
        Err(_) => None,
    };
    let Some(request) = request else {
        eprintln!("riga di richiesta non valida");
        return ExitCode::from(2);
    };
    println!("metodo: {}", request.method);
    println!("percorso: {}", request.path);
    if let Some(query) = request.query {
        println!("query: {query}");
    }
    println!("versione: {}", request.version);
    ExitCode::SUCCESS
}
