package no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd

import no.nav.familie.baks.infotrygd.feed.api.dto.ElementMetadata
import no.nav.familie.baks.infotrygd.feed.api.dto.FeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.repo.barnetrygd.BarnetrygdFeed
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType

fun konverterTilFeedMeldingDto(feedListe: List<BarnetrygdFeed>): FeedMeldingDto =
    FeedMeldingDto(
        tittel = "Barnetrygd feed",
        inneholderFlereElementer = feedListe.size > 1,
        elementer =
            feedListe.map {
                BarnetrygdFeedElement(
                    metadata = ElementMetadata(opprettetDato = it.opprettetDato),
                    innhold =
                        when (it.type) {
                            BarnetrygdType.BA_Vedtak_v1 -> {
                                InnholdVedtak(
                                    datoStartNyBA = checkNotNull(it.datoStartNyBa),
                                    fnrStoenadsmottaker = checkNotNull(it.fnrStønadsmottaker),
                                )
                            }

                            BarnetrygdType.BA_StartBeh -> {
                                InnholdStartBehandling(fnrStoenadsmottaker = checkNotNull(it.fnrStønadsmottaker))
                            }

                            else -> {
                                InnholdFødsel(fnrBarn = checkNotNull(it.fnrBarn))
                            }
                        },
                    sekvensId = it.sekvensId.toInt(),
                    type = it.type,
                )
            },
    )
