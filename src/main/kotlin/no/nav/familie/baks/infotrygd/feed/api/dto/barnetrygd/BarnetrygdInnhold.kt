package no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd

import no.nav.familie.baks.infotrygd.feed.api.dto.Innhold
import java.time.LocalDate

data class InnholdVedtak(val datoStartNyBA: LocalDate, val fnrStoenadsmottaker: String) : Innhold

data class InnholdFødsel(val fnrBarn: String) : Innhold
