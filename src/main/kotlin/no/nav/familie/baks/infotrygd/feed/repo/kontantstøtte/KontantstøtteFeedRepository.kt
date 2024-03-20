package no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface KontantstøtteFeedRepository : JpaRepository<KontantstøtteFeed, Long> {
    @Query(
        """ SELECT f FROM KontantstøtteFeed f 
                WHERE f.sekvensId > :sistLesteSekvensId ORDER BY f.sekvensId asc """,
    )
    fun finnMeldingerMedSekvensIdStørreEnn(
        pageable: Pageable,
        sistLesteSekvensId: Long,
    ): List<KontantstøtteFeed>
}
