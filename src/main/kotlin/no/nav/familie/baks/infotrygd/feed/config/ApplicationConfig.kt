package no.nav.familie.baks.infotrygd.feed.config

import no.nav.familie.log.filter.LogFilter
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootConfiguration
@ConfigurationPropertiesScan
@EnableJpaRepositories(ApplicationConfig.PAKKENAVN)
@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
@ComponentScan(ApplicationConfig.PAKKENAVN)
class ApplicationConfig {
    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        logger.info("Registering LogFilter filter")
        return FilterRegistrationBean<LogFilter>().apply {
            filter = LogFilter()
            order = 1
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ApplicationConfig::class.java)
        const val PAKKENAVN = "no.nav.familie.baks.infotrygd.feed"
    }
}
