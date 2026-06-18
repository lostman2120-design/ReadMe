package com.hikiyose.app.ui.screens.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.data.entity.TodoItem
import com.hikiyose.app.ui.components.SectionCard
import com.hikiyose.app.ui.hikiyoseViewModel

@Composable
fun EntryScreen(viewModel: EntryViewModel = hikiyoseViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("記入", style = MaterialTheme.typography.titleLarge)
        }

        item {
            SectionCard(title = "必ず引き寄せること") {
                Spacer(Modifier.height(8.dp))
                if (state.manifestations.isEmpty()) {
                    Hint("引き寄せたいことを書き出しましょう。")
                }
                state.manifestations.forEach { m ->
                    DeletableRow(text = m.text, onDelete = { viewModel.deleteManifestation(m) })
                }
                AddRow(placeholder = "引き寄せることを追加", onAdd = viewModel::addManifestation)
            }
        }

        item {
            FulfilledMessageCard(
                message = state.fulfilledMessage,
                onSave = viewModel::saveFulfilledMessage,
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
    Row(verticalAlignment = Alignment.CenterVertically) {
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

@Composable
private fun FulfilledMessageCard(message: String, onSave: (String) -> Unit) {
    var text by remember(message) { mutableStateOf(message) }
    var saved by remember(message) { mutableStateOf(false) }
    SectionCard(title = "叶った自分からのメッセージ") {
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; saved = false },
            placeholder = { Text("願いが叶った未来のあなたから、今の自分へ。") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
        )
        Spacer(Modifier.height(8.dp))
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
            Button(onClick = { onSave(text); saved = true }) { Text("保存") }
        }
    }
}
