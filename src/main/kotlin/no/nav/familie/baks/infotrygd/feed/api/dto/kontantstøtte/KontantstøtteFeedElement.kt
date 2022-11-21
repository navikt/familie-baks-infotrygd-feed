package no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte

import no.nav.familie.baks.infotrygd.feed.api.dto.ElementMetadata
import no.nav.familie.baks.infotrygd.feed.api.dto.FeedElement
import no.nav.familie.baks.infotrygd.feed.api.dto.Innhold
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType

data class KontantstøtteFeedElement(
    val innhold: Innhold,
    val metadata: ElementMetadata,
    val sekvensId: Int,
    val type: KontantstøtteType
) : FeedElement
