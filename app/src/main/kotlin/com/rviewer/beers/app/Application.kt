package com.rviewer.beers.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.rviewer.beers"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}