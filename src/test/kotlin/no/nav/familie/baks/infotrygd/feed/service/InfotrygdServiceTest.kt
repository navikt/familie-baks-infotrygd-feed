package no.nav.familie.baks.infotrygd.feed.service

import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.BarnetrygdFeedElement
import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.InnholdFødsel
import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.InnholdStartBehandling
import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.InnholdVedtak
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.KontantstøtteFeedElement
import no.nav.familie.baks.infotrygd.feed.config.DbContainerInitializer
import no.nav.familie.baks.infotrygd.feed.repo.barnetrygd.BarnetrygdFeedRepository
import no.nav.familie.baks.infotrygd.feed.repo.kontantstøtte.KontantstøtteFeedRepository
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.InnholdStartBehandling as KontantstøtteInnholdStartBehandling
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.InnholdVedtak as KontantstøtteInnholdVedtak

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [DbContainerInitializer::class])
@ActiveProfiles("integrasjonstest")
@Tag("integration")
class InfotrygdServiceTest {

    @Autowired
    private lateinit var infotrygdFeedService: InfotrygdFeedService

    @Autowired
    private lateinit var barnetrygdFeedRepository: BarnetrygdFeedRepository

    @Autowired
    private lateinit var kontantstøtteFeedRepository: KontantstøtteFeedRepository

    @AfterEach
    fun tearDown() {
        barnetrygdFeedRepository.deleteAll()
        kontantstøtteFeedRepository.deleteAll()
    }

    @Test
    fun `opprettFeed opprett fødsels feed for barnetrygd`() {
        val fnrBarn = "12345678911"
        assertDoesNotThrow { infotrygdFeedService.opprettBarnetrygdFeed(type = BarnetrygdType.BA_Foedsel_v1, fnrBarn = fnrBarn) }

        val feedMelding = infotrygdFeedService.hentBarnetrygdMeldingerFraFeed(0)
        assertEquals("Barnetrygd feed", feedMelding.tittel)
        assertFalse { feedMelding.inneholderFlereElementer }

        val feed = feedMelding.elementer.first() as BarnetrygdFeedElement
        assertEquals(BarnetrygdType.BA_Foedsel_v1, feed.type)
        assertNotNull(feed.metadata.opprettetDato)
        assertEquals(fnrBarn, (feed.innhold as InnholdFødsel).fnrBarn)
        assertTrue { barnetrygdFeedRepository.finnMeldingerForFnr(fnrBarn).any { it.duplikat == false } }
    }

    @Test
    fun `opprettFeed opprett duplikat fødsels feed for barnetrygd`() {
        val fnrBarn = "12345678912"
        infotrygdFeedService.opprettBarnetrygdFeed(type = BarnetrygdType.BA_Foedsel_v1, fnrBarn = fnrBarn)
        infotrygdFeedService.opprettBarnetrygdFeed(type = BarnetrygdType.BA_Foedsel_v1, fnrBarn = fnrBarn)

        assertTrue { barnetrygdFeedRepository.finnMeldingerForFnr(fnrBarn).any { it.duplikat == false } }
        assertTrue { barnetrygdFeedRepository.finnMeldingerForFnr(fnrBarn).any { it.duplikat == true } }
    }

    @Test
    fun `opprettFeed opprett vedtak feed for barnetrygd`() {
        val fnrStønadsmottaker = "12345678911"
        val datoStartNyBA = LocalDate.now()
        assertDoesNotThrow {
            infotrygdFeedService.opprettBarnetrygdFeed(
                type = BarnetrygdType.BA_Vedtak_v1,
                datoStartNyBA = datoStartNyBA,
                fnrStonadsmottaker = fnrStønadsmottaker
            )
        }

        val feedMelding = infotrygdFeedService.hentBarnetrygdMeldingerFraFeed(0)
        assertEquals("Barnetrygd feed", feedMelding.tittel)
        assertFalse { feedMelding.inneholderFlereElementer }

        val feed = feedMelding.elementer.first() as BarnetrygdFeedElement
        assertEquals(BarnetrygdType.BA_Vedtak_v1, feed.type)
        assertNotNull(feed.metadata.opprettetDato)
        assertEquals(fnrStønadsmottaker, (feed.innhold as InnholdVedtak).fnrStoenadsmottaker)
        assertEquals(datoStartNyBA, (feed.innhold as InnholdVedtak).datoStartNyBA)
    }

    @Test
    fun `opprettFeed opprett start behandling feed for barnetrygd`() {
        val fnrStønadsmottaker = "12345678911"
        assertDoesNotThrow {
            infotrygdFeedService.opprettBarnetrygdFeed(
                type = BarnetrygdType.BA_StartBeh,
                fnrStonadsmottaker = fnrStønadsmottaker
            )
        }

        val feedMelding = infotrygdFeedService.hentBarnetrygdMeldingerFraFeed(0)
        assertEquals("Barnetrygd feed", feedMelding.tittel)
        assertFalse { feedMelding.inneholderFlereElementer }

        val feed = feedMelding.elementer.first() as BarnetrygdFeedElement
        assertEquals(BarnetrygdType.BA_StartBeh, feed.type)
        assertNotNull(feed.metadata.opprettetDato)
        assertEquals(fnrStønadsmottaker, (feed.innhold as InnholdStartBehandling).fnrStoenadsmottaker)
    }

    @Test
    fun `opprettFeed opprett vedtak feed for kontantstøtte`() {
        val fnrStønadsmottaker = "12345678911"
        val datoStartNyKS = LocalDate.now()
        assertDoesNotThrow {
            infotrygdFeedService.opprettKontantstøtteFeed(
                type = KontantstøtteType.KS_Vedtak,
                datoStartNyKS = datoStartNyKS,
                fnrStonadsmottaker = fnrStønadsmottaker
            )
        }

        val feedMelding = infotrygdFeedService.hentKontantStøtteMeldingerFraFeed(0)
        assertEquals("Kontantstøtte feed", feedMelding.tittel)
        assertFalse { feedMelding.inneholderFlereElementer }

        val feed = feedMelding.elementer.first() as KontantstøtteFeedElement
        assertEquals(KontantstøtteType.KS_Vedtak, feed.type)
        assertNotNull(feed.metadata.opprettetDato)
        assertEquals(fnrStønadsmottaker, (feed.innhold as KontantstøtteInnholdVedtak).fnrStoenadsmottaker)
        assertEquals(datoStartNyKS, (feed.innhold as KontantstøtteInnholdVedtak).datoStartNyKS)
    }

    @Test
    fun `opprettFeed opprett start behandling feed for kontantstøtte`() {
        val fnrStønadsmottaker = "12345678911"
        assertDoesNotThrow {
            infotrygdFeedService.opprettKontantstøtteFeed(
                type = KontantstøtteType.KS_StartBeh,
                fnrStonadsmottaker = fnrStønadsmottaker
            )
        }

        val feedMelding = infotrygdFeedService.hentKontantStøtteMeldingerFraFeed(0)
        assertEquals("Kontantstøtte feed", feedMelding.tittel)
        assertFalse { feedMelding.inneholderFlereElementer }

        val feed = feedMelding.elementer.first() as KontantstøtteFeedElement
        assertEquals(KontantstøtteType.KS_StartBeh, feed.type)
        assertNotNull(feed.metadata.opprettetDato)
        assertEquals(fnrStønadsmottaker, (feed.innhold as KontantstøtteInnholdStartBehandling).fnrStoenadsmottaker)
        assertEquals(LocalDate.now(), kontantstøtteFeedRepository.findByIdOrNull(feed.sekvensId.toLong())?.datoStartNyKS)
    }
}
