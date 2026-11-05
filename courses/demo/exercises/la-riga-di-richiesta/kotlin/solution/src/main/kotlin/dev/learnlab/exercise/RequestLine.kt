package dev.learnlab.exercise

/** The parts of the first line of an HTTP request, e.g. `GET /saluto?nome=Ada HTTP/1.1`. */
data class RequestLine(
    val method: String,
    val path: String,
    /** What follows `?` in the target; null when there is nothing. */
    val query: String?,
    val version: String,
)

private val METHOD = Regex("[A-Z]+")
private val VERSIONS = setOf("HTTP/1.0", "HTTP/1.1")

/**
 * The parts of [text], one request line with its end (`\r\n` in HTTP, or just `\n`), or null when
 * it is not a valid request line.
 */
fun parseRequestLine(text: String): RequestLine? {
    val line = text.removeSuffix("\n").removeSuffix("\r")
    // Two spaces in a row give an empty part, and four parts.
    val parts = line.split(" ")
    if (parts.size != 3) return null
    val (method, target, version) = parts
    if (!METHOD.matches(method) || !target.startsWith("/") || version !in VERSIONS) return null
    return RequestLine(
        method = method,
        path = target.substringBefore('?'),
        query = target.substringAfter('?', "").ifEmpty { null },
        version = version,
    )
}
