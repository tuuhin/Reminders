package com.eva.reminders.utils

fun String.getFirstSentence(): String {
    val periodIndex = this.indexOf(".")
    if (periodIndex == -1)
        return this.trim()
    return this.substring(0, periodIndex + 1).trim()
}