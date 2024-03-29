package com.eva.reminders.domain.enums

import com.eva.reminders.R

enum class TaskColorEnum(val color: Int) {
    TRANSPARENT(R.color.transparent),
    RED(R.color.red_300),
    ORANGE(R.color.orange_300),
    AMBER(R.color.amber_300),
    YELLOW(R.color.yellow_300),
    LIME(R.color.lime_300),
    GREEN(R.color.green_300),
    EMERALD(R.color.emerald_300),
    TEAL(R.color.teal_300),
    CYAN(R.color.cyan_300),
    SKY(R.color.sky_300),
    BLUE(R.color.blue_300),
    INDIGO(R.color.indigo_300),
    VIOLET(R.color.violet_300),
    PURPLE(R.color.purple_300),
    FUCHSIA(R.color.fuchsia_300),
    PINK(R.color.pink_300),
    ROSE(R.color.rose_30);

    fun toText(): String =
        when (this) {
            TRANSPARENT -> ""
            RED -> "Red"
            ORANGE -> "Orange"
            AMBER -> "Amber"
            YELLOW -> "Yellow"
            LIME -> "Lime"
            GREEN -> "Green"
            EMERALD -> "Emerald"
            TEAL -> "Teal"
            CYAN -> "Cyan"
            SKY -> "Sky"
            BLUE -> "Blue"
            INDIGO -> "Indigo"
            VIOLET -> "Violet"
            PURPLE -> "Purple"
            FUCHSIA -> "Fuchsia"
            PINK -> "Pink"
            ROSE -> "Rose"
        }
}
