package no.nav.familie.baks.infotrygd.feed.repo

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FeedLoggRepository : JpaRepository<FeedLogg, UUID> {

    fun existsByLoggIdAndTypeAndGjeldendeFnr(loggId: UUID, type: String, gjeldendeFnr: String): Boolean
}
