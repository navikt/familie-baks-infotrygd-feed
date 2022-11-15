package no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity(name = "KontantstøtteFeed")
@Table(name = "kontantstotte_feed")
data class KontantstøtteFeed(
    @Id
    @Column(name = "sekvens_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_seq_generator")
    @SequenceGenerator(name = "feed_seq_generator", sequenceName = "kontantstotte_feed_seq", allocationSize = 1)
    val sekvensId: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: KontantstøtteType,

    @Column(name = "fnr_stonadsmottaker", nullable = true)
    var fnrStonadsmottaker: String? = null,

    @Column(name = "dato_start_ny_ba", nullable = true)
    var datoStartNyBa: LocalDate? = null,

    @Column(name = "fnr_barn", nullable = true)
    var fnrBarn: String? = null,

    @Column(name = "er_duplikat", nullable = true)
    var duplikat: Boolean? = false,

    @Column(name = "opprettet_dato", nullable = true)
    var opprettetDato: LocalDateTime

) {
    override fun toString(): String =
        "KontantstøtteFeed(sekvensId=$sekvensId, opprettetDato=$opprettetDato type=$type, datoStartNyBa=$datoStartNyBa, duplikat=$duplikat"
}

@Suppress("ktlint:enum-entry-name-case")
enum class KontantstøtteType {
    KS_Vedtak_v1,
    KS_StartBeh
}
