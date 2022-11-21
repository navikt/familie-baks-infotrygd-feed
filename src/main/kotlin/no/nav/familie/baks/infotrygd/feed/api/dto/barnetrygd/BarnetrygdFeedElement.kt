package no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd

import no.nav.familie.baks.infotrygd.feed.api.dto.ElementMetadata
import no.nav.familie.baks.infotrygd.feed.api.dto.FeedElement
import no.nav.familie.baks.infotrygd.feed.api.dto.Innhold
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType

data class BarnetrygdFeedElement(
    val innhold: Innhold,
    val metadata: ElementMetadata,
    val sekvensId: Int,
    val type: BarnetrygdType
) : FeedElement
