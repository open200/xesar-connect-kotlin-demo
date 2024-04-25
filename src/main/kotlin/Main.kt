import com.open200.xesar.connect.Config
import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.extension.*
import com.open200.xesar.connect.filters.TopicFilter
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.AccessProtocolEvent
import com.open200.xesar.connect.messages.query.EventType
import com.open200.xesar.connect.messages.query.GroupOfEvent
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.*
import kotlin.io.path.Path

private val log = KotlinLogging.logger {}

fun main() {

    // add a security provider
    Security.addProvider(BouncyCastleProvider())

    runBlocking {

        // path to the zip file containing the certificates
        val pathToZip = Path("mqtt-cert.zip")

        // connect to the MQTT broker and login to xesar
        // set 'logoutOnClose' to 'false' to avoid triggering a logout and certificate expiration after each execution when using the zip file for authentication
        val xesar = XesarConnect.connectAndLoginAsync(Config.configureFromZip(pathToZip)).await()

        // Subscribe to topics all topics (or the topics you are interested in)
        xesar.subscribeAsync(Topics(Topics.ALL_TOPICS)).await()

        // send command to create a person
        val personId = UUID.randomUUID()
        xesar.createPersonAsync(
            firstName = "Ford",
            lastName = "Prefect",
            identifier = "fprefect",
            externalId = "fprefect",
            personId = personId
        ).await()

        // query all persons
        val persons = xesar.queryPersons().data
        log.info { "Received person list: $persons" }


        // query one person by id
        val person = xesar.queryPersonById(personId)

        person?.let {
            // send command to delete a person
            xesar.deletePersonAsync(person.externalId).await()
        }

        // fetching persons incrementally in smaller,more manageable chunks
        val chunkSize = 15
        xesar.queryStreamPerson(Query.Params(pageLimit =chunkSize)).collect{
            log.info { "person: ${it.firstName}, ${it.lastName}" }
        }

        // subscribe to the access event and listen to battery warnings. You can use the provided enums for the events from the library
        val batteryEmptyTopic = Topics.Event.accessProtocolEventTopic(GroupOfEvent.EvvaComponent, EventType.BATTERY_EMPTY)
        xesar.on(TopicFilter(batteryEmptyTopic)) {
            val event = AccessProtocolEvent.decode(it.message)
            log.info { "Received battery warning from installation point identifier: ${event.installationPointIdentifier}" }
        }

        // suspend the coroutine to keep the connection to xesar open
        xesar.delay()
    }
}
