package dev.learnlab.exercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Application

data class Saluto(
    val messaggio: String,
)

@RestController
class SalutoController {
    @GetMapping("/saluto")
    fun saluto(
        @RequestParam(required = false) nome: String?,
    ) = Saluto("Ciao, ${nome ?: "mondo"}!")
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
