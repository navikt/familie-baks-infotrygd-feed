package no.nav.familie.baks.infotrygd.feed.config

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.Executor

@Component
class KafkaErrorHandler : CommonContainerStoppingErrorHandler() {

    val logger: Logger = LoggerFactory.getLogger(KafkaErrorHandler::class.java)
    val secureLogger: Logger = LoggerFactory.getLogger("secureLogger")

    private val executor: Executor

    override fun handleRemaining(
        e: Exception,
        records: MutableList<ConsumerRecord<*, *>>,
        consumer: Consumer<*, *>,
        container: MessageListenerContainer,
    ) {
        Thread.sleep(1000)

        if (records.isEmpty()) {
            logger.error("Feil ved konsumering av melding. Ingen records. ${consumer.subscription()}", e)
            scheduleRestart(e, records, consumer, container, "Ukjent topic")
        } else {
            records.first().run {
                logger.error(
                    "Feil ved konsumering av melding fra ${this.topic()}. id ${this.key()}, " +
                        "offset: ${this.offset()}, partition: ${this.partition()}",
                )
                secureLogger.error("${this.topic()} - Problemer med prosessering av $records", e)
                scheduleRestart(e, records, consumer, container, this.topic())
            }
        }
    }

    private fun scheduleRestart(
        e: Exception,
        records: List<ConsumerRecord<*, *>>,
        consumer: Consumer<*, *>,
        container: MessageListenerContainer,
        topic: String,
    ) {
        executor.execute {
            try {
                Thread.sleep(SHORT)
                logger.warn("Starter kafka container for $topic")
                container.start()
            } catch (exception: Exception) {
                logger.error("Feil oppstod ved venting og oppstart av kafka container", exception)
            }
        }
        logger.warn("Stopper kafka container for $topic i ${Duration.ofMillis(SHORT)}")
        super.handleRemaining(e, records, consumer, container)
    }

    companion object {

        private val SHORT = Duration.ofSeconds(10).toMillis()
    }

    init {
        this.executor = SimpleAsyncTaskExecutor()
    }
}
