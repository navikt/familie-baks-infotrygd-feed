package no.nav.familie.baks.infotrygd.feed

import no.nav.familie.baks.infotrygd.feed.config.ApplicationConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["no.nav.familie.baks.infotrygd.feed"])
class Launcher

fun main(args: Array<String>) {
    val app = SpringApplication(ApplicationConfig::class.java)
    app.run(*args)
}
