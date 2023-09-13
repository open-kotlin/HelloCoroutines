package open.kotlin

import kotlin.coroutines.*
import kotlin.concurrent.thread
import kotlinx.coroutines.*

// https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-global-scope/
fun testGlobalScope() {
    // GlobalScope启动的协程不会使进程保持活动状态。它们就像守护线程，当所有非守护线程都执行完毕后结束运行。
    val job = GlobalScope.launch {
        log.d("协程开始")
        delay(2000)
        log.d("协程结束")
    }
    // runBlocking { job.join() }
    // 如果是守护线程，那么协程不会被执行完JVM就退出了
    thread(isDaemon = true) {
        Thread.sleep(5000)
    }
    log.d("主线程退出")
}

// https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html
fun testCoroutineScope() {
    runBlocking {
        // 并发分解工作
        coroutineScope {
            val a /* StandaloneCoroutine */ = launch(context = Dispatchers.IO) {
                log.d("1")
                delay(5000)
                log.d("1 done")
            }
            val b /* DeferredCoroutine */= async(Dispatchers.IO) {
                log.d("2")
                delay(10000)
                log.d("2 done")
                "async return value"
            }
            log.d("switching context")
            val c: Unit = withContext(Dispatchers.Default) {
                log.d("3")
                delay(1000)
                log.d("3 done")
                // b.cancel() // DeferredCoroutine会在执行结构化并发范式失败时取消父作业（或外层作用域）
                a.cancel() // 1 done不会被打印
                log.d("b.await() = ${b.await()}")
                Unit
            }
            log.d("coroutineScope done")
        }
        log.d("all done")
    }
}

fun scope() {
    // testGlobalScope()
    testCoroutineScope()
}