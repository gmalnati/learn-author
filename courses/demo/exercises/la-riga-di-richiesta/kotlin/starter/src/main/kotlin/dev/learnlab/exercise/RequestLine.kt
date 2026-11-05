package dev.learnlab.exercise

/** The parts of the first line of an HTTP request, e.g. `GET /saluto?nome=Ada HTTP/1.1`. */
data class RequestLine(
    val method: String,
    val path: String,
    /** What follows `?` in the target; null when there is nothing. */
    val query: String?,
    val version: String,
)

/**
 * The parts of [text], one request line with its end (`\r\n` in HTTP, or just `\n`), or null when
 * it is not a valid request line.
 */
fun parseRequestLine(text: String): RequestLine? {
    // TODO: the end of the line, the query and the rules of the README.
    val parts = text.removeSuffix("\n").split(" ")
    return RequestLine(
        method = parts[0],
        path = parts.getOrElse(1) { "" },
        query = null,
        version = parts.getOrElse(2) { "" },
    )
}
