package no.nav.familie.baks.infotrygd.feed.service

import no.nav.familie.baks.infotrygd.feed.api.dto.FeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.konverterTilFeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.konverterTilFeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.repo.barnetrygd.BarnetrygdFeed
import no.nav.familie.baks.infotrygd.feed.repo.barnetrygd.BarnetrygdFeedRepository
import no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte.KontantstøtteFeed
import no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte.KontantstøtteFeedRepository
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class InfotrygdFeedService(
    private val barnetrygdFeedRepository: BarnetrygdFeedRepository,
    private val kontantstøtteFeedRepository: KontantstøtteFeedRepository
) {

    fun hentBarnetrygdMeldingerFraFeed(sistLestSekvensId: Long, maxSize: Int = 100): FeedMeldingDto =
        konverterTilFeedMeldingDto(
            barnetrygdFeedRepository.finnMeldingerMedSekvensIdStørreEnn(
                pageable = PageRequest.of(0, maxSize),
                sistLesteSekvensId = sistLestSekvensId
            )
        )

    fun hentKontantStøtteMeldingerFraFeed(sistLestSekvensId: Long, maxSize: Int = 100): FeedMeldingDto =
        konverterTilFeedMeldingDto(
            kontantstøtteFeedRepository.finnMeldingerMedSekvensIdStørreEnn(
                pageable = PageRequest.of(0, maxSize),
                sistLesteSekvensId = sistLestSekvensId
            )
        )

    @Transactional
    fun opprettBarnetrygdFeed(
        type: BarnetrygdType,
        fnrBarn: String? = null,
        fnrStonadsmottaker: String? = null,
        datoStartNyBA: LocalDate? = null
    ) {
        val erDuplikat = type.takeIf { it == BarnetrygdType.BA_Foedsel_v1 }
            ?.let { barnetrygdFeedRepository.erDuplikatFoedselsmelding(type, fnrBarn!!) }
            ?: false

        barnetrygdFeedRepository.save(
            BarnetrygdFeed(
                type = type,
                fnrBarn = fnrBarn,
                fnrStønadsmottaker = fnrStonadsmottaker,
                datoStartNyBa = datoStartNyBA,
                duplikat = erDuplikat,
                opprettetDato = LocalDateTime.now()
            )
        )
    }

    @Transactional
    fun opprettKontantstøtteFeed(
        type: KontantstøtteType,
        fnrStonadsmottaker: String,
        datoStartNyKS: LocalDate = LocalDate.now()
    ) = kontantstøtteFeedRepository.save(
        KontantstøtteFeed(
            type = type,
            fnrStønadsmottaker = fnrStonadsmottaker,
            datoStartNyKS = datoStartNyKS,
            opprettetDato = LocalDateTime.now()
        )
    )
}
