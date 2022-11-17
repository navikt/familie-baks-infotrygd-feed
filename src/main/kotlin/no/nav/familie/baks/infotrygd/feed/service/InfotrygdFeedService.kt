package no.nav.familie.baks.infotrygd.feed.service

import no.nav.familie.baks.infotrygd.feed.api.dto.FeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.konverterTilFeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.konverterTilFeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.repo.barnetrygd.BarnetrygdFeedRepository
import no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte.KontantstøtteFeedRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

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
}
