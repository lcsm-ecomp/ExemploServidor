import io.ktor.application.*
import io.ktor.features.origin
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.title

var numero = 0
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("ola.html") {
                numero++
                call.respondText("""
                    <html>
                    <title>Página dinâmica</title>
                    <body>
                    Esta é uma página HTML gerada dinamicamente<br>
                    Está página já foi gerada $numero de vezes
                    </body>
                    </html>
                """.trimIndent(), ContentType.Text.Html)
            }
            get("ola2.html") {
                numero++
                call.respondHtml {
                    title = "Página dinâmica"
                    body {
                      +("Esta é uma página HTML gerada dinamicamente")
                      br
                      +("esta página já foi gerada $numero de vezes")
                    }
                }
            }
            static("static") {
                files("dados_estaticos")
            }
        }
    }.start(wait = true)
}