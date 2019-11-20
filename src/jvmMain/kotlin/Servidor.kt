import io.ktor.application.*
import io.ktor.features.origin
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("ola.html") {
                //PROGRAMA
            }
            static("static") {
                files("dados_estaticos")
            }
        }
    }.start(wait = true)
}