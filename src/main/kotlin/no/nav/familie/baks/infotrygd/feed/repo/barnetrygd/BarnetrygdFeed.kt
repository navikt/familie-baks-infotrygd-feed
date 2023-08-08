package no.nav.familie.baks.infotrygd.feed.repo.barnetrygd

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "BarnetrygdFeed")
@Table(name = "barnetrygd_feed")
data class BarnetrygdFeed(
    @Id
    @Column(name = "sekvens_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_seq_generator")
    @SequenceGenerator(name = "feed_seq_generator", sequenceName = "barnetrygd_feed_seq", allocationSize = 1)
    val sekvensId: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: BarnetrygdType,

    @Column(name = "fnr_stonadsmottaker", nullable = true)
    var fnrStønadsmottaker: String? = null,

    @Column(name = "dato_start_ny_ba", nullable = true)
    var datoStartNyBa: LocalDate? = null,

    @Column(name = "fnr_barn", nullable = true)
    var fnrBarn: String? = null,

    @Column(name = "er_duplikat", nullable = true)
    var duplikat: Boolean? = false,

    @Column(name = "opprettet_dato", nullable = true)
    var opprettetDato: LocalDateTime,

) {
    override fun toString(): String =
        "BarnetrygdFeed(sekvensId=$sekvensId, opprettetDato=$opprettetDato type=$type, datoStartNyBa=$datoStartNyBa, duplikat=$duplikat)"
}
