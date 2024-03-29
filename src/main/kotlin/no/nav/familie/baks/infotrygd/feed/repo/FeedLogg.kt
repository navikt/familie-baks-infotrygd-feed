package no.nav.familie.baks.infotrygd.feed.repo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.util.UUID

@Entity(name = "FeedLogg")
@Table(name = "feed_logg")
class FeedLogg(
    @Id
    @Column(name = "logg_id")
    val loggId: UUID,
    @Column(name = "type", nullable = false)
    val type: String,
    @Column(name = "gjeldende_fnr", nullable = false)
    val gjeldendeFnr: String,
    @Column(name = "opprettet_dato", nullable = false)
    val opprettetDato: LocalDateTime = LocalDateTime.now(),
) {
    init {
        if (BarnetrygdType.values().none { it.name == type } && KontantstøtteType.values().none { it.name == type }) {
            throw IllegalArgumentException("Ukjent type $type")
        }
    }
}
