package club.deneb.client.utils

import java.time.Duration
import java.time.Instant

class Timer {

    var time: Long
    fun resetTimeSkipTo(ms: Int) {
        time = System.currentTimeMillis() + ms
    }

    fun passed(ms: Double): Boolean {
        return System.currentTimeMillis() - time >= ms
    }

    fun passedSec(second: Double): Boolean {
        return System.currentTimeMillis() - time >= second * 1000
    }

    fun passedTick(tick: Double): Boolean {
        return System.currentTimeMillis() - time >= tick * 50
    }

    fun passedMin(minutes: Double): Boolean {
        return System.currentTimeMillis() - time >= minutes * 1000 * 60
    }

    fun reset(): Timer {
        time = System.currentTimeMillis()
        return this
    }

    fun hasPassed(): Long {
        return System.currentTimeMillis() - time
    }

    companion object {
        fun now(): Instant {
            return Instant.now()
        }

        fun getSecondsPassed(start: Instant?, current: Instant?): Long {
            return Duration.between(start, current).seconds
        }

        fun haveSecondsPassed(start: Instant?, current: Instant?, seconds: Long): Boolean {
            return getSecondsPassed(start, current) >= seconds
        }

        fun getMilliSecondsPassed(start: Instant?, current: Instant?): Long {
            return Duration.between(start, current).toMillis()
        }

        fun haveMilliSecondsPassed(start: Instant?, current: Instant?, milliseconds: Long): Boolean {
            return getMilliSecondsPassed(start, current) >= milliseconds
        }
    }
    init {
        time = -1
    }
}