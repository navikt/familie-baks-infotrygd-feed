package no.nav.familie.baks.infotrygd.feed

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["no.nav.familie.baks.infotrygd.feed"])
class DevLauncher

fun main(args: Array<String>) {
    val springApp = SpringApplication(DevLauncher::class.java)
    springApp.setAdditionalProfiles("dev")
    springApp.run(*args)
}
