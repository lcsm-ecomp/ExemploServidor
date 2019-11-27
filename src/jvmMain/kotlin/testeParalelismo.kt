package testeParalelo

import jdk.nashorn.internal.objects.Global
import kotlinx.coroutines.*

fun fib(n:Int):Int {
    if (n<2)
        return 1
    else
        return fib(n-1) + fib(n-2)
}
suspend fun p_fib(n:Int):Int {
    if (n<33)
        return fib(n)
    val c1 = GlobalScope.async { p_fib(n-1) }
    val c2 = GlobalScope.async { p_fib(n-2) }
    return c1.await() + c2.await()

}

fun main() {
    val start = System.currentTimeMillis()
    runBlocking {
        val v1 = p_fib(35)
        println("fib = $v1")
    }
    val end = System.currentTimeMillis()
    println("tempo gasto = ${(end-start)/1000.0}")
}