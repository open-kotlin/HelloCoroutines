package open.kotlin

import log4kt.*

fun getTid(): Long = Thread.currentThread().id
fun getTname(): String = Thread.currentThread().name

inline fun printTid() {
    log4kt.log.d("thread id: ${getTid()} (${getTname()})")
}

val defaultLoggerImpl = LoggerImpl()
@JvmField
val log = Logger("") { level: Level, tag: String, msg: String ->
    println("${defaultLoggerImpl.getTime()} - $tag - [$level] - {${getTid()}:${getTname()}} - $msg")
}