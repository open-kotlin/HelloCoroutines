package open.kotlin

import kotlin.coroutines.*
import log4kt.log

suspend fun getPromise2(world: String? = null): String {
    printTid()
    return getPromise(world)
}

/**
 * 演示API:
 * ```
 * fun <T> (suspend () -> T).createCoroutine(completion: Continuation<T>): Continuation<Unit>
 * ```
 */
fun createCoroutine() {
    printTid()
    val suspendLambda = suspend {
        log.d("suspendLambda开始执行")
        printTid()
        val hi = getPromise2("World")
        log.d("hi = $hi")
        try {
            getPromise2()
        } catch (e: Throwable) {
            log.e("getPromise(null) =", e)
        }
    }
    val task = suspendLambda.createCoroutine(object : Continuation<Any> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Any>) {
            Thread.sleep(10000)
            log.d("result: $result")
            printTid()
        }
    })
    Thread.sleep(5000)
    log.d("开始恢复协程")
    task.resume(Unit) // 将会被堵塞10秒!
    log.d("ok")
}

fun startCoroutine() {
    val suspendLambda = suspend {
        log.d("suspendLambda开始执行")
        printTid()
        myDelay(5000)
        printTid()
    }
    log.d("启动协程")
    suspendLambda.startCoroutine(object : Continuation<Any> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Any>) {
            log.d("协程处理完毕，执行回调")
            log.d("result: $result")
            printTid()
        }
    })
    log.d("return")
}