package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskColorPicker(
    modifier: Modifier = Modifier,
    state: SheetState
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { if (state.isVisible) scope.launch { state.hide() } },
        sheetState = state
    ) {
        Text(
            text = "Color",
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.titleLarge,
        )
        LazyRow(contentPadding = PaddingValues(vertical = 8.dp)) {

        }

    }
}