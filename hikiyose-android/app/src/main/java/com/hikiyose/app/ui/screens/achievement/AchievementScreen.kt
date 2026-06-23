package com.hikiyose.app.ui.screens.achievement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.ui.components.EmptyState
import com.hikiyose.app.ui.hikiyoseViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AchievementScreen(viewModel: AchievementViewModel = hikiyoseViewModel()) {
    val active by viewModel.activeManifestations.collectAsStateWithLifecycle()
    var dialog by remember { mutableStateOf<Dialog?>(null) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { dialog = Dialog.AddNew },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("達成を追加") },
            )
        },
    ) { padding ->
        if (active.isEmpty()) {
            EmptyState(
                message = "達成できる引き寄せがまだありません。\n「記入」タブで登録するか、右下から直接追加できます。",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Column {
                        Text("達成", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "叶った引き寄せを選んで「達成」を記録しましょう。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                items(active, key = { it.id }) { m ->
                    ActiveCard(m = m, onAchieve = { dialog = Dialog.MarkExisting(m) })
                }
            }
        }
    }

    when (val d = dialog) {
        is Dialog.MarkExisting -> AchievementDialog(
            title = "達成を記録",
            fixedText = d.item.text,
            onDismiss = { dialog = null },
            onConfirm = { _, date, thoughts ->
                viewModel.markAchieved(d.item, date, thoughts); dialog = null
            },
        )
        Dialog.AddNew -> AchievementDialog(
            title = "達成を直接追加",
            fixedText = null,
            onDismiss = { dialog = null },
            onConfirm = { text, date, thoughts ->
                viewModel.addAchievedDirectly(text, date, thoughts); dialog = null
            },
        )
        null -> Unit
    }
}

private sealed interface Dialog {
    data object AddNew : Dialog
    data class MarkExisting(val item: Manifestation) : Dialog
}

@Composable
private fun ActiveCard(m: Manifestation, onAchieve: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(m.text, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onAchieve) { Text("達成にする") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AchievementDialog(
    title: String,
    fixedText: String?,
    onDismiss: () -> Unit,
    onConfirm: (text: String, date: LocalDate, thoughts: String) -> Unit,
) {
    var text by remember { mutableStateOf(fixedText ?: "") }
    var thoughts by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日", Locale.JAPANESE)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                if (fixedText == null) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("達成した引き寄せ") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Text("・$fixedText", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("達成日: ${date.format(dateFormatter)}")
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = thoughts,
                    onValueChange = { thoughts = it },
                    label = { Text("思っていること") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text, date, thoughts) },
                enabled = fixedText != null || text.isNotBlank(),
            ) { Text("保存") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("キャンセル") } },
    )

    if (showDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = date.toEpochDay() * 86_400_000L,
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("キャンセル") }
            },
        ) {
            DatePicker(state = state)
        }
    }
}
