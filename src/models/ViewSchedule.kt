package com.kiko.models

import TimeSlot
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.concurrent.fixedRateTimer

class ViewSchedule {
    private val createDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atStartOfDay()
    private var schedule = Array(11 * 3 * 7) { Reservation() }

    init {
        fixedRateTimer(
            initialDelay = createDate.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            period = 1000 * 60 * 60 * 24 * 7
        ) {
            schedule = Array(11 * 3 * 7) { Reservation() }
        }
    }

    fun get(timeSlot: TimeSlot): Reservation? {
        val current = Calendar.getInstance().timeInMillis
        val diff = current - createDate.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

        return schedule[timeSlot.day * 33 + timeSlot.slot].takeIf { diff < 1000 * 3600 * 24 }
    }
}
