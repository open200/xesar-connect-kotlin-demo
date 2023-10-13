import com.open200.xesar.connect.Config
import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.filters.AllTopicsFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import kotlin.io.path.Path
import kotlin.time.Duration.Companion.days

private val log = KotlinLogging.logger {}

fun main() {

    Security.addProvider(BouncyCastleProvider())

    runBlocking {
        launch {
            val pathToZip = Path("/home/martin/Downloads/mqtt-cert.zip")
            val personCreatedTopic = "xs3/1/ces/PersonCreated"

            XesarConnect.connectAndLoginAsync(Config.configureFromZip(pathToZip)).await()
                .use {api ->
                    // we are now connected to the MQTT broker and already logged in

                    // Subscribe to topics we are interested
                    api.subscribeAsync(Topics(personCreatedTopic), 2).await()

                    // Send commands and wait for the corresponding events
                    // TODO add sending a command when library supports this feature

                    api.on(AllTopicsFilter()) {
                        log.debug("Received message {} -> {}", it.topic, it.message)
                    }

                    // Create listeners to react on emitted events
                    api.on({ topic, _ -> topic.startsWith(personCreatedTopic) }) {
                        log.info { "Message received on topic ${it.topic}" }
                    }

                    delay(10.days)
                }
        }
    }
}
