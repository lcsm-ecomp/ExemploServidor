import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*


fun main() {
    println("ola do cliente")
    var canvas = document.getElementById("desenho") as HTMLCanvasElement
    canvas.onmousemove = { evento ->
        if (evento.buttons.toInt()==1) {
            println("O mouse moveu ${evento.offsetX} ${evento.offsetY} ${evento.buttons}")
            var ctx = canvas.getContext("2d") as CanvasRenderingContext2D
            println("contexto = $ctx")
            ctx.fillRect(evento.offsetX, evento.offsetY, 10.0, 10.0)
            ctx.stroke()
        }
    }
}