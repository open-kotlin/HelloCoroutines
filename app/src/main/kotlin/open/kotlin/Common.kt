package open.kotlin

import log4kt.log

fun getTid(): Long = Thread.currentThread().id
fun getTname(): String = Thread.currentThread().name

inline fun printTid() {
    log.d("thread id: ${getTid()} (${getTname()})")
}