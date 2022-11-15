package no.nav.familie.baks.infotrygd.feed

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["no.nav.familie.baks.infotrygd.feed"])
class DevLauncherPostgres

fun main(args: Array<String>) {
    val springApp = SpringApplication(DevLauncherPostgres::class.java)
    springApp.setAdditionalProfiles("postgres")
    springApp.run(*args)
}
