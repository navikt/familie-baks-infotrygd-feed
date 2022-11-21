package no.nav.familie.baks.infotrygd.feed.consumer

import no.nav.familie.baks.infotrygd.feed.config.KafkaConfig
import no.nav.familie.baks.infotrygd.feed.service.InfotrygdFeedService
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdFeedDto
import no.nav.familie.kontrakter.ba.infotrygd.feed.BarnetrygdType
import no.nav.familie.kontrakter.ba.infotrygd.feed.FødselsDto
import no.nav.familie.kontrakter.ba.infotrygd.feed.StartBehandlingDto
import no.nav.familie.kontrakter.ba.infotrygd.feed.VedtakDto
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteFeedDto
import no.nav.familie.kontrakter.ks.infotrygd.feed.KontantstøtteType
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
@ConditionalOnProperty(
    value = ["funksjonsbrytere.kafka.consumer.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class FeedConsumer(private val infotrygdFeedService: InfotrygdFeedService) {

    private var barnetrygdLatch: CountDownLatch = CountDownLatch(1)
    private var kontantstøtteLatch: CountDownLatch = CountDownLatch(1)

    @KafkaListener(
        id = "familie-ba-sak-feed",
        topics = [KafkaConfig.BARNETRYGD_FEED_TOPIC],
        containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    fun listenBarnetrygdFeed(consumerRecord: ConsumerRecord<String, String>, ack: Acknowledgment) {
        val data: String = consumerRecord.value()
        val key: String = consumerRecord.key()

        logger.info("Feed hendelse for Barnetrygd er mottatt i kafka $consumerRecord for behandlingId $key")
        secureLogger.info("Feed hendelse for Barnetrygd er mottatt i kafka $consumerRecord for behandlingId $key")

        val request: BarnetrygdFeedDto = objectMapper.readValue(data, BarnetrygdFeedDto::class.java)
        when (request.type) {
            BarnetrygdType.BA_Vedtak_v1 -> {
                val vedtakDto = request as VedtakDto
                infotrygdFeedService.opprettBarnetrygdFeed(
                    type = request.type,
                    datoStartNyBA = vedtakDto.datoStartNyBa,
                    fnrStonadsmottaker = vedtakDto.fnrStoenadsmottaker
                )
            }
            BarnetrygdType.BA_Foedsel_v1 -> {
                val fødselsDto = request as FødselsDto
                infotrygdFeedService.opprettBarnetrygdFeed(type = request.type, fnrBarn = fødselsDto.fnrBarn)
            }
            BarnetrygdType.BA_StartBeh -> {
                val startBehandlingDto = request as StartBehandlingDto
                infotrygdFeedService.opprettBarnetrygdFeed(
                    type = request.type,
                    fnrStonadsmottaker = startBehandlingDto.fnrStoenadsmottaker
                )
            }
        }

        barnetrygdLatch.countDown()
        ack.acknowledge()
    }

    @KafkaListener(
        id = "familie-ks-sak-feed",
        topics = [KafkaConfig.KONTANTSTØTTE_FEED_TOPIC],
        containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    fun listenKontantstøtteFeed(consumerRecord: ConsumerRecord<String, String>, ack: Acknowledgment) {
        val data: String = consumerRecord.value()
        val key: String = consumerRecord.key()

        logger.info("Feed hendelse for \"Kontantstøtte\" er mottatt i kafka $consumerRecord for behandlingId $key")
        secureLogger.info("Feed hendelse for \"Kontantstøtte\" er mottatt i kafka $consumerRecord for behandlingId $key")

        val request: KontantstøtteFeedDto = objectMapper.readValue(data, KontantstøtteFeedDto::class.java)
        when (request.type) {
            KontantstøtteType.KS_Vedtak -> {
                val vedtakDto = request as no.nav.familie.kontrakter.ks.infotrygd.feed.VedtakDto
                infotrygdFeedService.opprettKontantstøtteFeed(
                    type = request.type,
                    datoStartNyKS = vedtakDto.datoStartNyKS,
                    fnrStonadsmottaker = vedtakDto.fnrStoenadsmottaker
                )
            }
            KontantstøtteType.KS_StartBeh -> {
                val startBehandlingDto = request as no.nav.familie.kontrakter.ks.infotrygd.feed.StartBehandlingDto
                infotrygdFeedService.opprettKontantstøtteFeed(
                    type = request.type,
                    fnrStonadsmottaker = startBehandlingDto.fnrStoenadsmottaker
                )
            }
        }

        kontantstøtteLatch.countDown()
        ack.acknowledge()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val secureLogger = LoggerFactory.getLogger("secureLogger")
    }
}
