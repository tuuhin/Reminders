package com.eva.reminders.presentation.feature_labels.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith

/**
 * Animates the content by swiping the previous to its left and next to right
 */
fun <T : Comparable<T>> AnimatedContentTransitionScope<T>.slideContentHorizontally(): ContentTransform {
    return if (targetState > initialState)
        slideInHorizontally { height -> height } togetherWith slideOutHorizontally { height -> -height }
    else slideInHorizontally { height -> -height } togetherWith slideOutHorizontally { height -> height }
}

/**
 * Animates the content by swiping the previous to top and next to down
 */
fun <T : Comparable<T>> AnimatedContentTransitionScope<T>.slideContentVertically(): ContentTransform {
    return if (targetState > initialState)
        slideInVertically { height -> -height } togetherWith slideOutVertically { height -> height }
    else slideInVertically { height -> height } togetherWith slideOutVertically { height -> -height }
}