package dev.learnlab.exercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Application

@RestController
class SalutoController {
    // Da fare: GET /saluto?nome=Ada risponde {"messaggio": "Ciao, Ada!"}; senza nome, "Ciao, mondo!".
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
