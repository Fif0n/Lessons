package com.example.lessons.models

data class Hour(
    val hour: Int,
    val minute: Int,
) {
    fun hourFormatted(): String {
        if (hour >= 10) {
            return hour.toString()
        }

        return "0$hour"
    }

    fun minuteFormatted(): String {
        if (minute >= 10) {
            return minute.toString()
        }

        return "0$minute"
    }

    fun toFormattedString(): String = String.format("%02d:%02d", hour, minute)
}

data class TimeRange(
    val hourFrom: Hour,
    val hourTo: Hour,
) {
    fun hourRangeFormatted(): String {
        return "${hourFrom.hourFormatted()}:${hourFrom.minuteFormatted()} - ${hourTo.hourFormatted()}:${hourTo.minuteFormatted()}"
    }
}

data class Day(
    val dayNumber: Int,
    var hours: List<TimeRange>?
) {
    fun hoursFormatted(): String {
        if (hours.isNullOrEmpty()) {
            return "-"
        }

        var timeRange = ""

        hours!!.forEach {
            timeRange += "${it.hourRangeFormatted()}, "
        }

        return timeRange
    }

}

data class AvailableHours(
    val dayOfWeek: List<Day>?
)
