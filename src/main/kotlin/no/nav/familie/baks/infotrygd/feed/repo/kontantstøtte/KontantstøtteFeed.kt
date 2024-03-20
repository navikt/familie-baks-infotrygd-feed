package no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType
import java.time.LocalDate
import java.time.LocalDateTime

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
    @Column(name = "fnr_stonadsmottaker", nullable = false)
    var fnrStønadsmottaker: String,
    @Column(name = "dato_start_ny_ks", nullable = false)
    var datoStartNyKS: LocalDate,
    @Column(name = "opprettet_dato", nullable = true)
    var opprettetDato: LocalDateTime,
) {
    override fun toString(): String =
        "KontantstøtteFeed(sekvensId=$sekvensId, opprettetDato=$opprettetDato type=$type, datoStartNyBa=$datoStartNyKS)"
}
