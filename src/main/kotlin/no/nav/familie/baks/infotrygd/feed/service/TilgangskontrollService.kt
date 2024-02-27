package no.nav.familie.baks.infotrygd.feed.service

import no.nav.security.token.support.core.context.TokenValidationContextHolder
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TilgangskontrollService(
    private val tokenValidationContextHolder: TokenValidationContextHolder,
    @Value("\${TEAMFAMILIE_FORVALTNING_GROUP_ID}") private val forvalterGroupId: String,
) {
    fun sjekkTilgang() {
        val roles = tokenValidationContextHolder.getTokenValidationContext().anyValidClaims?.getAsList("roles") ?: emptyList()
        val groups = tokenValidationContextHolder.getTokenValidationContext().anyValidClaims?.getAsList("groups") ?: emptyList()

        if (!roles.contains(ACCESS_AS_APPLICATION_ROLE) && !groups.contains(forvalterGroupId)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "User har ikke tilgang til å kalle tjenesten!")
        }
    }

    companion object {
        const val ACCESS_AS_APPLICATION_ROLE = "access_as_application"
    }
}
