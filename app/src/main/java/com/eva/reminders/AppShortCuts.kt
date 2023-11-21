package com.eva.reminders

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.eva.reminders.presentation.navigation.NavigationDeepLinks

class AppShortCuts(private val context: Context) {

    private val createTaskShortCut: ShortcutInfoCompat
        get() = ShortcutInfoCompat
            .Builder(context, context.getString(R.string.create_task_shortcut_id))
            .setShortLabel(context.getString(R.string.create_task_shortcut_text))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_add_icon))
            .setIntent(
                Intent(context, MainActivity::class.java).apply {
                    data = NavigationDeepLinks.createNewTaskUri
                    action = Intent.ACTION_VIEW
                }
            )
            .build()


    private val createLabelShortCut: ShortcutInfoCompat
        get() = ShortcutInfoCompat
            .Builder(context, context.getString(R.string.create_label_shortcut_id))
            .setShortLabel(context.getString(R.string.create_label_shortcut_text))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_label_icon))
            .setIntent(
                Intent(context, MainActivity::class.java).apply {
                    data = NavigationDeepLinks.editLabelsUri
                    action = Intent.ACTION_VIEW

                }
            )
            .build()


    /**
     * Sets the shortcuts for the app
     */
    fun setShortCuts() = ShortcutManagerCompat.addDynamicShortcuts(
        context,
        listOf(
            createTaskShortCut, createLabelShortCut,
        )
    )

}