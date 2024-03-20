package no.nav.familie.baks.infotrygd.feed.service

import no.nav.familie.baks.infotrygd.feed.api.dto.FeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.konverterTilFeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.konverterTilFeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.repo.FeedLogg
import no.nav.familie.baks.infotrygd.feed.repo.FeedLoggRepository
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
import java.util.UUID

@Service
class InfotrygdFeedService(
    private val barnetrygdFeedRepository: BarnetrygdFeedRepository,
    private val kontantstøtteFeedRepository: KontantstøtteFeedRepository,
    private val feedLoggRepository: FeedLoggRepository,
) {
    fun hentBarnetrygdMeldingerFraFeed(
        sistLestSekvensId: Long,
        maxSize: Int = 100,
    ): FeedMeldingDto =
        konverterTilFeedMeldingDto(
            barnetrygdFeedRepository.finnMeldingerMedSekvensIdStørreEnn(
                pageable = PageRequest.of(0, maxSize),
                sistLesteSekvensId = sistLestSekvensId,
            ),
        )

    fun hentKontantStøtteMeldingerFraFeed(
        sistLestSekvensId: Long,
        maxSize: Int = 100,
    ): FeedMeldingDto =
        konverterTilFeedMeldingDto(
            kontantstøtteFeedRepository.finnMeldingerMedSekvensIdStørreEnn(
                pageable = PageRequest.of(0, maxSize),
                sistLesteSekvensId = sistLestSekvensId,
            ),
        )

    @Transactional
    fun opprettBarnetrygdFeed(
        key: UUID,
        type: BarnetrygdType,
        fnrBarn: String? = null,
        fnrStonadsmottaker: String? = null,
        datoStartNyBA: LocalDate? = null,
    ) {
        val gjeldendeFnr =
            fnrBarn ?: fnrStonadsmottaker
                ?: error("Både fnrBarn og fnrStonadsmottaker kan ikke være null")

        if (feedLoggRepository.existsByLoggIdAndTypeAndGjeldendeFnr(loggId = key, type = type.name, gjeldendeFnr = gjeldendeFnr)) {
            return
        }
        val erDuplikat =
            type.takeIf { it == BarnetrygdType.BA_Foedsel_v1 }
                ?.let { barnetrygdFeedRepository.erDuplikatFoedselsmelding(type, fnrBarn!!) }
                ?: false

        barnetrygdFeedRepository.save(
            BarnetrygdFeed(
                type = type,
                fnrBarn = fnrBarn,
                fnrStønadsmottaker = fnrStonadsmottaker,
                datoStartNyBa = datoStartNyBA,
                duplikat = erDuplikat,
                opprettetDato = LocalDateTime.now(),
            ),
        )
        loggFeed(key, type.name, gjeldendeFnr)
    }

    @Transactional
    fun opprettKontantstøtteFeed(
        key: UUID,
        type: KontantstøtteType,
        fnrStonadsmottaker: String,
        datoStartNyKS: LocalDate = LocalDate.now(),
    ) {
        if (feedLoggRepository.existsByLoggIdAndTypeAndGjeldendeFnr(loggId = key, type = type.name, gjeldendeFnr = fnrStonadsmottaker)) {
            return
        }

        kontantstøtteFeedRepository.save(
            KontantstøtteFeed(
                type = type,
                fnrStønadsmottaker = fnrStonadsmottaker,
                datoStartNyKS = datoStartNyKS,
                opprettetDato = LocalDateTime.now(),
            ),
        )
        loggFeed(key, type.name, fnrStonadsmottaker)
    }

    private fun loggFeed(
        loggId: UUID,
        type: String,
        gjeldendeFnr: String,
    ) {
        feedLoggRepository.save(FeedLogg(loggId = loggId, type = type, gjeldendeFnr = gjeldendeFnr))
    }
}
