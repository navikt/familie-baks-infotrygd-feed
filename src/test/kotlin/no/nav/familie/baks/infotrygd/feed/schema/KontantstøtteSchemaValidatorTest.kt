package no.nav.familie.baks.infotrygd.feed.schema

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonMetaSchema
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.NonValidationKeyword
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidatorTypeCode
import no.nav.familie.baks.infotrygd.feed.api.dto.ElementMetadata
import no.nav.familie.baks.infotrygd.feed.api.dto.FeedMeldingDto
import no.nav.familie.baks.infotrygd.feed.api.dto.barnetrygd.InnholdVedtak
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.InnholdStartBehandling
import no.nav.familie.baks.infotrygd.feed.api.dto.kontantstøtte.KontantstøtteFeedElement
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalDateTime

class KontantstøtteSchemaValidatorTest {
    @Test
    fun `Dto for start behandling validerer mot schema`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForStartBehandling())
        val feilListe = schema.validate(node)
        assertTrue(feilListe.isEmpty())
    }

    @Test
    fun `Dto for vedtak validerer mot schema`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForVedtak())
        val feilListe = schema.validate(node)
        assertTrue(feilListe.isEmpty())
    }

    @Test
    fun `Dto for vedtak validerer ikke dersom fnrStoenadsmottaker har feil format`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForVedtak("123456"))
        val feilListe = schema.validate(node)
        assertEquals(1, feilListe.size)
    }

    private fun testDtoForVedtak(fnrStoenadsmottaker: String = "12345678910"): FeedMeldingDto =
        FeedMeldingDto(
            tittel = "Feed schema validator test",
            inneholderFlereElementer = false,
            elementer =
                listOf(
                    KontantstøtteFeedElement(
                        innhold = InnholdVedtak(datoStartNyBA = LocalDate.now(), fnrStoenadsmottaker = fnrStoenadsmottaker),
                        metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                        sekvensId = 42,
                        type = KontantstøtteType.KS_Vedtak,
                    ),
                ),
        )

    private fun testDtoForStartBehandling(fnrStoenadsmottaker: String = "12345678910"): FeedMeldingDto =
        FeedMeldingDto(
            tittel = "Feed schema validator test",
            inneholderFlereElementer = false,
            elementer =
                listOf(
                    KontantstøtteFeedElement(
                        innhold = InnholdStartBehandling(fnrStoenadsmottaker = fnrStoenadsmottaker),
                        metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                        sekvensId = 42,
                        type = KontantstøtteType.KS_StartBeh,
                    ),
                ),
        )

    private val schema: JsonSchema
        get() {
            val schemaNode = objectMapper.readTree(hentFeedSchema())

            val uri = "https://json-schema.org/draft-04/schema"
            val id = "\$id"
            val myJsonMetaSchema =
                JsonMetaSchema
                    .Builder(uri)
                    .idKeyword(id)
                    .keywords(ValidatorTypeCode.getKeywords(SpecVersion.VersionFlag.V4))
                    .keywords(
                        listOf(
                            NonValidationKeyword("\$schema"),
                            NonValidationKeyword("\$id"),
                            NonValidationKeyword("examples"),
                        ),
                    ).build()

            return JsonSchemaFactory
                .Builder()
                .defaultMetaSchemaIri(uri)
                .metaSchema(myJsonMetaSchema)
                .build()
                .getSchema(schemaNode)
        }

    private fun hentFeedSchema(): String {
        val inputStream = this::class.java.classLoader.getResourceAsStream("schema/kontantstøtte-feed-schema.json")
        return String(inputStream!!.readAllBytes(), Charset.forName("UTF-8"))
    }
}
