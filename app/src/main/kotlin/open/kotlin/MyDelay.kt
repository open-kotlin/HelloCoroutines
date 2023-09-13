package open.kotlin

import kotlin.coroutines.*
import kotlin.concurrent.thread
import log4kt.log

suspend fun myDelay(millis: Long) = suspendCoroutine<Unit> { continuation ->
    if (millis <= 0) {
        continuation.resume(Unit)
        return@suspendCoroutine
    }
    thread {
        Thread.sleep(millis)
        continuation.resume(Unit)
    }
}

suspend fun myPending() = suspendCoroutine<Unit> { continuation ->
    thread { // 主线程会等待所有非守护线程一起退出
        while (true) {
            Thread.sleep(Long.MAX_VALUE)
        }
    }
}