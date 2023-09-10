package open.kotlin

import kotlin.coroutines.*
import kotlinx.coroutines.delay
import log4kt.log

fun <T> runBlocking(process: suspend () -> T): T? {
    var isOk = false
    var value: T? = null
    val coroutine = process.createCoroutine(
        object : Continuation<T> {
            override fun resumeWith(result: Result<T>) {
                log.d(result)
                if (result.isSuccess) {
                    value = result.getOrNull()
                }
                isOk = true
            }
            override val context = EmptyCoroutineContext
        }
    )
    coroutine.resume(Unit)
    // return value // 如果不涉及暂停, resume()会在resumeWith执行后才返回, delay(0)可以得到返回值
    while (true) {
        if (isOk) return value
        Thread.sleep(1)
    }
}

fun testRunBlocking() {
    val value = runBlocking {
        log.d("执行")
        // delay(0) // 不会被暂停
        delay(2000)
        log.d("好了")
        getPromise("World")
    }
    log.d("value = $value")
    Thread.sleep(5000)
}