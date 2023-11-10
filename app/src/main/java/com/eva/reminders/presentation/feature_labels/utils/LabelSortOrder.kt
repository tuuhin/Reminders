package com.eva.reminders.presentation.feature_labels.utils

import androidx.annotation.StringRes
import com.eva.reminders.R

enum class LabelSortOrder(@StringRes val textRes: Int) {
    REGULAR(R.string.sort_order_normal),
    ALPHABETICALLY_ASC(R.string.sort_order_alpha_asc),
    ALPHABETICALLY_DESC(R.string.sort_order_alpha_desc)
}