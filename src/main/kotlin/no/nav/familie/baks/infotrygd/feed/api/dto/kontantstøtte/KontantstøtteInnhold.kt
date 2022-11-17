package no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte

import no.nav.familie.baks.infotrygd.feed.api.dto.Innhold
import java.time.LocalDate

data class InnholdVedtak(val datoStartNyKS: LocalDate, val fnrStoenadsmottaker: String) : Innhold

data class InnholdStartBehandling(val fnrStoenadsmottaker: String) : Innhold
