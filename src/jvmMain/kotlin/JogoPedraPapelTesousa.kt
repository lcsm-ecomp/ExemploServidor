package PedraPapelTesoura
import io.ktor.application.*
import io.ktor.features.origin
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.title
import kotlin.random.Random


// -1 : Nao entrou 0 : Não Jogou 1 : Pedra 2 : Papel 3 : Tesoura

data class Jogada(val codigo:String, var jogadaA:Int, var jogadaB:Int)

var jogadas = mutableMapOf<String, Jogada>()
fun novaJogada():Jogada {
    println("Nova jogada")
    var codigo = Random.nextInt(10,100000).toString();
    while (jogadas.containsKey(codigo))
        codigo = Random.nextInt(10,100000).toString();
    val j = Jogada(codigo,-1,-1)
    jogadas.put(codigo,j)
    return j
}


var numero = 0
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("ppt.html") {
                var jogo = call.parameters.get("jogo")
                var jogadaAtual : Jogada
                var jogador = call.parameters.get("jogador")
                if (jogador==null || jogo==null) {
                    jogador = "a"
                    jogadaAtual = novaJogada()
                    jogo = jogadaAtual.codigo
                    call.respondRedirect("ppt.html?jogador=$jogador&jogo=$jogo")
                    return@get
                }
                if (jogadas[jogo]==null) {
                    call.respondHtml {
                        body {
                            +"Partida inválida"
                        }
                    }
                }
                jogadaAtual = jogadas[jogo] as Jogada
                if (jogadaAtual.jogadaB==-1 && jogador=="b") {
                     jogadaAtual.jogadaB=0
                }
                if (jogadaAtual.jogadaB==-1 && jogador=="a") {
                    call.respondText(
                        """
                    <html>
                    <title>Pedra Papel e Tesoura</title>
                    <body>
                    Chame outro jogador para competir contigo pelo link:<br>
                    <a href="ppt.html?jogo=$jogo&jogador=b">Aqui</a><br>
                    </body>
                    </html>
                    
                """.trimIndent(), ContentType.Text.Html)

                }  else {
                    var jogada = call.parameters.get("jogada")
                    if (jogada!=null) {
                        if (jogador=="a")
                            jogadaAtual.jogadaA = Integer.parseInt(jogada)
                        else
                            jogadaAtual.jogadaB = Integer.parseInt(jogada)
                    }
                    call.respondText(
                        """
                        <html>
                        <title>Pedra Papel e Tesoura</title>
                        <body>
                        <center><h1>Você é o jogador $jogador</h1></center>
                        O jogador A jogou: ${jogadaAtual.jogadaA}<br>
                        O jogador B jogou: ${jogadaAtual.jogadaB}<p>
                        <a href="ppt.html?jogador=$jogador&jogo=$jogo&jogada=1">Jogar Pedra</a><br>
                        Jogar Papel<br>
                        Jogar Tesoura
                        
                        </body>
                        </html>
                        
                    """.trimIndent(), ContentType.Text.Html
                    )
                }

            }
            static("static") {
                files("dados_estaticos")
                files("build/js/packages/ServidorWeb/kotlin/")
            }
        }
    }.start(wait = true)
}