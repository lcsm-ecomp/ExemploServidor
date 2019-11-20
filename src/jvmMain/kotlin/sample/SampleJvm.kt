package sample

import io.ktor.application.*
import io.ktor.features.origin
import io.ktor.html.*
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import java.io.*
import java.nio.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

var contadorAcessos = 0
var nomesDosUsuarios:MutableList<String> = mutableListOf()
var conversas = ""
fun main() {
    conversas = Files.readString(Paths.get("conversa.txt"))
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("/ola.txt") {
                println("recebi um pedido de dados")
                call.respond("ola para todos")
            }
            get("desenho.html") {
                call.respondText("""
                    <html>
                    <title>Ferramenta de Desenho</title>
                    <body>
                    <CENTER>
                    <H1>Desenhe no quadro abaixo</H1>
                    <canvas id="desenho" width="400" height="300" style="border-style:solid;">
                    </canvas>
                    <script src="/static/kotlin.js"></script>
                    <script src="/static/OlaServidor.js"></script>
                    </body>
                    </html>
                    
                """.trimIndent(),ContentType.Text.Html )
            }
            get("papo.html") {
                var nome = call.parameters.get("Nome")
                var disse = call.parameters.get("Disse")
                if (nome!=null && disse!=null) {
                    conversas += "<b>$nome</b> disse: $disse<p>"
                    Files.writeString(
                        Paths.get("conversa.txt"),
                        conversas)
                }
                call.respondText("""
                <html>
                
                <title>Bate papo de LPF</title>
                <body>
                Conversas: <p>
                $conversas
                <p>Fim<p>
                <form action="papo.html">
                Nome: <input name="Nome" type="text" value="$nome">
                Disse: <input name="Disse" type="text">
                <input type="submit" value="falar">
                </form>
                <a href="papo.html">Falar</a>
                </body>
                </html>
                """.trimIndent(), ContentType.Text.Html)
            }
            get("ola.html") {
                contadorAcessos++
                var atributes = call.request.origin.remoteHost
                var request = call.request.toString()
                var nome = call.parameters.get("Nome")
                if (nome==null) nome = "Ninguem"
                nomesDosUsuarios.add(nome)
                var texto = "Acessado por: "
                nomesDosUsuarios.forEach { u ->
                    texto += " " + u
                }
                call.respondText("""
                    <html>
                    <title>Uma pagina HTML</title>
                    <body>
                    Ola $nome, <p>
                    boa tarde<p>
                    Atibutos = $atributes<p>
                    Request = $request<p>
                    Esta é uma página HTML gerada pelo servidor<p>
                    Esta página foi acessada $contadorAcessos vezes por<br>
                    $texto
                    </body>
                    </html>
                """.trimIndent(), ContentType.Text.Html)

            }

            static("static") {
                files("dados_estaticos")
                files("build/js/packages/OlaServidor/kotlin")
            }
        }
    }.start(wait = true)
}