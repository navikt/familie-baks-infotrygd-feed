package no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte

import no.nav.familie.baks.infotrygd.feed.api.dto.ElementMetadata
import no.nav.familie.baks.infotrygd.feed.api.dto.FeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte.KontantstøtteFeed
import no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte.KontantstøtteType

fun konverterTilFeedMeldingDto(feedListe: List<KontantstøtteFeed>): FeedMeldingDto =
    FeedMeldingDto(
        tittel = "Kontantstøtte feed",
        inneholderFlereElementer = feedListe.size > 1,
        elementer = feedListe.map {
            KontantstøtteFeedElement(
                metadata = ElementMetadata(opprettetDato = it.opprettetDato),
                innhold =
                when (it.type) {
                    KontantstøtteType.KS_Vedtak -> {
                        InnholdVedtak(datoStartNyKS = it.datoStartNyKS, fnrStoenadsmottaker = it.fnrStønadsmottaker)
                    }
                    KontantstøtteType.KS_StartBeh -> {
                        InnholdStartBehandling(fnrStoenadsmottaker = it.fnrStønadsmottaker)
                    }
                },
                sekvensId = it.sekvensId.toInt(),
                type = it.type
            )
        }
    )
