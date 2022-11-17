package no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte

import java.time.LocalDate
import java.time.LocalDateTime

data class VedtakDto(val datoStartNyKS: LocalDate, val fnrStoenadsmottaker: String)

data class StartBehandlingDto(val fnrStoenadsmottaker: String)

data class FeedOpprettetDto(val opprettetDato: LocalDateTime, val datoStartNyKS: LocalDate?)
