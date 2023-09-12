package open.kotlin

import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
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

// 现在我们知道suspend函数的调用是：suspend_func(in1, in2, ..., continuation: Continuation<T>): T or COROUTINE_SUSPENDED flag
fun <T> runBlocking2(process: suspend () -> T): T? {
    var isOk = false
    var value: T? = null
    val func = process as Function1<Continuation<T>, T>
    val continuation = object : Continuation<T> {
        override fun resumeWith(result: Result<T>) {
            log.d(result)
            if (result.isSuccess) {
                value = result.getOrNull()
            }
            isOk = true
        }
        override val context = EmptyCoroutineContext
    }
    val flag = func.invoke(continuation)
    return when (flag) {
        COROUTINE_SUSPENDED -> {
            while (true) {
                if (isOk) break
                Thread.sleep(1)
            }
            value
        }
        else -> flag
    }
}

fun testRunBlocking() {
    val value = runBlocking {
        log.d("执行")
        printTid() // thread id: 1 (main)
        // delay(0) // 不会被暂停
        delay(2000) // thread id: 10 (kotlinx.coroutines.DefaultExecutor)
        printTid()
        myDelay(2000) // thread id: 11 (Thread-0)
        printTid()
        log.d("好了")
        getPromise("World")
    }
    log.d("value = $value")
    Thread.sleep(5000)
}

suspend fun job(doDelay: Boolean = true): Int {
    open.kotlin.log.d("开始执行协程")
    if (doDelay)
        delay(4000)
    open.kotlin.log.d("执行协程完毕") // thread id: 10 (kotlinx.coroutines.DefaultExecutor)
    return 888
}

fun testRunBlocking2() {
    val value = runBlocking2(::job)
    log.d("value = $value")
    Thread.sleep(5000)
}