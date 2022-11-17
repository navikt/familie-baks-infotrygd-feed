package no.nav.familie.baks.infotrygd.feed.api.dto

data class FeedMeldingDto(
    val elementer: List<FeedElement>,
    val inneholderFlereElementer: Boolean,
    val tittel: String
)
