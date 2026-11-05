package dev.learnlab.exercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Application

data class Saluto(
    val messaggio: String,
)

/** Il livello HTTP del servizio: le regole del saluto sono in Saluti.kt. */
@RestController
class SalutoController {
    @GetMapping("/saluto")
    fun saluto(
        @RequestParam(required = false) nome: String?,
    ): ResponseEntity<Saluto> = messaggio(nome)?.let { ResponseEntity.ok(Saluto(it)) } ?: ResponseEntity.badRequest().build()
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
