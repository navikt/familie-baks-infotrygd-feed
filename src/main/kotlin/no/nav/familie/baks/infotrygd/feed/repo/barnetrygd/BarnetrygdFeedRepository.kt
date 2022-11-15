package no.nav.familie.baks.infotrygd.feed.repo.barnetrygd

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BarnetrygdFeedRepository : JpaRepository<BarnetrygdFeed, Long> {

    @Query(
        """ SELECT f FROM BarnetrygdFeed f 
                WHERE f.sekvensId > :sistLesteSekvensId AND f.duplikat = false 
                ORDER BY f.sekvensId asc """
    )
    fun finnMeldingerMedSekvensIdStørreEnn(pageable: Pageable, sistLesteSekvensId: Long): List<BarnetrygdFeed>

    @Query(
        """ SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false end FROM BarnetrygdFeed f 
                    WHERE f.type = :type AND f.fnrBarn = :fnrBarn """
    )
    fun erDuplikatFoedselsmelding(type: BarnetrygdType, fnrBarn: String): Boolean

    @Query(value = "SELECT f FROM BarnetrygdFeed f WHERE f.fnrStonadsmottaker = :fnr OR f.fnrBarn = :fnr")
    fun finnMeldingerForFnr(fnr: String): List<BarnetrygdFeed>
}
