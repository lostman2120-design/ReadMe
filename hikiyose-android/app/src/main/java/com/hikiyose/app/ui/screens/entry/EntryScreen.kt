package com.hikiyose.app.ui.screens.entry

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.data.entity.TodoItem
import com.hikiyose.app.data.repository.ReminderSettings
import com.hikiyose.app.ui.components.SectionCard
import com.hikiyose.app.ui.hikiyoseViewModel

@Composable
fun EntryScreen(viewModel: EntryViewModel = hikiyoseViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val reminder by viewModel.reminderSettings.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item { Text("記入", style = MaterialTheme.typography.titleLarge) }

        item {
            SectionCard(title = "必ず引き寄せること") {
                Spacer(Modifier.height(8.dp))
                if (state.manifestations.isEmpty()) {
                    Hint("引き寄せたいことを書き出しましょう。各項目に「叶った自分からのメッセージ」を添えられます。")
                }
                AddRow(placeholder = "引き寄せることを追加", onAdd = viewModel::addManifestation)
            }
        }

        items(state.manifestations, key = { it.id }) { m ->
            ManifestationCard(
                manifestation = m,
                onSaveMessage = { msg -> viewModel.saveFulfilledMessage(m, msg) },
                onDelete = { viewModel.deleteManifestation(m) },
            )
        }

        item {
            SectionCard(title = "毎日唱えること（アファメーション）") {
                Spacer(Modifier.height(8.dp))
                if (state.affirmations.isEmpty()) {
                    Hint("「私は〜」という現在形・肯定形の言葉が効果的です。")
                }
                state.affirmations.forEach { a: Affirmation ->
                    DeletableRow(text = a.text, onDelete = { viewModel.deleteAffirmation(a) })
                }
                AddRow(placeholder = "アファメーションを追加", onAdd = viewModel::addAffirmation)
            }
        }

        item {
            ReminderCard(
                reminder = reminder,
                onSetReminder = viewModel::setReminder,
            )
        }

        item {
            SectionCard(title = "TO DO") {
                Spacer(Modifier.height(8.dp))
                state.todos.forEach { todo: TodoItem ->
                    TodoRow(
                        todo = todo,
                        onToggle = { viewModel.toggleTodo(todo) },
                        onDelete = { viewModel.deleteTodo(todo) },
                    )
                }
                AddRow(placeholder = "やることを追加", onAdd = viewModel::addTodo)
            }
        }
    }
}

@Composable
private fun ManifestationCard(
    manifestation: Manifestation,
    onSaveMessage: (String) -> Unit,
    onDelete: () -> Unit,
) {
    var message by remember(manifestation.id, manifestation.fulfilledMessage) {
        mutableStateOf(manifestation.fulfilledMessage)
    }
    var saved by remember(manifestation.id, manifestation.fulfilledMessage) {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    manifestation.text,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Close, contentDescription = "削除")
                }
            }
            OutlinedTextField(
                value = message,
                onValueChange = { message = it; saved = false },
                label = { Text("叶った自分からのメッセージ") },
                placeholder = { Text("これが叶った未来のあなたから、今の自分へ。") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (saved) {
                    Text(
                        "保存しました",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 12.dp),
                    )
                }
                TextButton(onClick = { onSaveMessage(message); saved = true }) {
                    Text("メッセージを保存")
                }
            }
        }
    }
}

@Composable
private fun ReminderCard(
    reminder: ReminderSettings,
    onSetReminder: (enabled: Boolean, hour: Int, minute: Int) -> Unit,
) {
    val context = LocalContext.current
    var showTimePicker by remember { mutableStateOf(false) }

    // On Android 13+, enabling needs the POST_NOTIFICATIONS runtime permission.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) onSetReminder(true, reminder.hour, reminder.minute)
    }

    fun requestEnable() {
        val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        if (needsPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onSetReminder(true, reminder.hour, reminder.minute)
        }
    }

    SectionCard(
        title = "毎朝のアファメーション通知",
        trailing = {
            Switch(
                checked = reminder.enabled,
                onCheckedChange = { checked ->
                    if (checked) requestEnable() else onSetReminder(false, reminder.hour, reminder.minute)
                },
            )
        },
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            "設定した時刻に、登録したアファメーションをランダムで通知します。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = { showTimePicker = true },
            enabled = reminder.enabled,
        ) {
            Text("通知時刻: %02d:%02d".format(reminder.hour, reminder.minute))
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            initialHour = reminder.hour,
            initialMinute = reminder.minute,
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                showTimePicker = false
                onSetReminder(true, hour, minute)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit,
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("通知時刻") },
        text = { TimePicker(state = state) },
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("キャンセル") } },
    )
}

@Composable
private fun Hint(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 4.dp),
    )
}

@Composable
private fun AddRow(placeholder: String, onAdd: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text(placeholder) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onAdd(input); input = "" }) {
            Icon(Icons.Filled.Add, contentDescription = "追加")
        }
    }
}

@Composable
private fun DeletableRow(text: String, onDelete: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("・$text", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Close, contentDescription = "削除")
        }
    }
}

@Composable
private fun TodoRow(todo: TodoItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onToggle)
            .padding(vertical = 2.dp),
    ) {
        Checkbox(checked = todo.isDone, onCheckedChange = { onToggle() })
        Text(
            text = todo.title,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
            color = if (todo.isDone) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Close, contentDescription = "削除")
        }
    }
}
