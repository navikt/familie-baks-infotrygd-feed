package no.nav.familie.baks.infotrygd.feed.repo

import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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
