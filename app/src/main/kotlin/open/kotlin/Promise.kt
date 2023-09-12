package open.kotlin

import kotlin.coroutines.*
import log4kt.log

/**
 * Continuation 可以看作是一种 Promise，resume、resumeWith、resumeWithException等方法提供了 resolve 和 reject
 */
suspend fun getPromise(world: String? = null): String =
        suspendCoroutine<String> { continuation: Continuation<String> ->
            if (world == null) {
                continuation.resumeWithException(RuntimeException("Damn it!"))
            } else {
                // continuation.resume("Hello, " + world)
                continuation.resumeWith(Result.success("Hello, " + world))
            }
        }

fun promise(): suspend () -> Any {
    return suspend {
        val hi = getPromise("World")
        log.d("hi = $hi")
        try {
            getPromise()
        } catch (e: Throwable) {
            log.e("getPromise(null) =", e)
        }
    }
}
