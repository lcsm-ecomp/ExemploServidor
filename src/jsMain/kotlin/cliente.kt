import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.*


fun digaOla() {
    println("ola do cliente")

}

@JsName("modifica")
fun modifica() {
    val area = document.getElementById("area1") as HTMLDivElement
    area.innerHTML = """
        este texto não estava no arquivo HTML<br>
        mas foi inserido pelo programa em kotlin
    """.trimIndent()
}