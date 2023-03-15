package ru.ac.uniyar.web.handlers

import org.http4k.core.Request
import ru.ac.uniyar.web.filters.dateFromLensList
import ru.ac.uniyar.web.filters.dateToLensList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

const val minYear = 0
const val minMonth = 1
const val minDay = 1
const val minHour = 0
const val minMinute = 0
const val minSecond = 0

const val maxYear = 9999
const val maxMonth = 12
const val maxDay = 31
const val maxHour = 23
const val maxMinute = 59
const val maxSecond = 59

fun makeDateFrom(request: Request): LocalDateTime {
    return try {
        if (dateFromLensList(request) == null)
            LocalDateTime.of(minYear, minMonth, minDay, minHour, minMinute, minSecond)
        else
            LocalDateTime.of(
                LocalDate.parse(dateFromLensList(request)!!),
                LocalTime.of(minHour, minMinute, minSecond)
            )
    } catch (_: DateTimeParseException) {
        LocalDateTime.of(minYear, minMonth, minDay, minHour, minMinute, minSecond)
    }
}

fun makeDateTo(request: Request): LocalDateTime {
    return try {
        if (dateToLensList(request) == null)
            LocalDateTime.of(maxYear, maxMonth, maxDay, maxHour, maxMinute, maxSecond)
        else
            LocalDateTime.of(
                LocalDate.parse(dateToLensList(request)!!),
                LocalTime.of(maxHour, maxMinute, maxSecond)
            )
    } catch (_: DateTimeParseException) {
        LocalDateTime.of(maxYear, maxMonth, maxDay, maxHour, maxMinute, maxSecond)
    }
}
