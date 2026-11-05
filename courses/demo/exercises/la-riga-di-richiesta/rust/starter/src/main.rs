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

/// The parts of `text`, one request line with its end (`\r\n` in HTTP, or just `\n`), or `None`
/// when it is not a valid request line.
fn parse_request_line(text: &str) -> Option<RequestLine<'_>> {
    // TODO: the end of the line, the query and the rules of the README.
    let line = text.strip_suffix('\n').unwrap_or(text);
    let mut parts = line.split(' ');
    Some(RequestLine {
        method: parts.next().unwrap_or(""),
        path: parts.next().unwrap_or(""),
        query: None,
        version: parts.next().unwrap_or(""),
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
