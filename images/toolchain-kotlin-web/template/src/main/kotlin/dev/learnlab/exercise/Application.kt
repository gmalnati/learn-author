package dev.learnlab.exercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Application

@RestController
class PingController {
    @GetMapping("/ping")
    fun ping() = mapOf("status" to "ok")
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
