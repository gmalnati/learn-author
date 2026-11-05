package dev.learnlab.exercise

import kotlin.system.exitProcess

/** Reads a request line from the standard input and writes its parts, or refuses it. */
fun main() {
    val request = parseRequestLine(System.`in`.bufferedReader().readText())
    if (request == null) {
        System.err.println("riga di richiesta non valida")
        exitProcess(2)
    }
    println("metodo: ${request.method}")
    println("percorso: ${request.path}")
    request.query?.let { println("query: $it") }
    println("versione: ${request.version}")
}
