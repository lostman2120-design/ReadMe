package com.hikiyose.app.ui.screens.home

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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.entity.TodoItem
import com.hikiyose.app.ui.components.SectionCard
import com.hikiyose.app.ui.hikiyoseViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeViewModel = hikiyoseViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { Header() }

        item {
            SectionCard(title = "今日のアファメーション") {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.affirmationOfDay?.text
                        ?: "「アファメーション」タブで、あなたの言葉を追加しましょう。",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        item {
            SectionCard(title = "偉人の言葉") {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "“${state.quoteOfDay.text}”",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "— ${state.quoteOfDay.author}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        }

        item {
            TodoCard(
                todos = state.todos,
                onAdd = viewModel::addTodo,
                onToggle = viewModel::toggleTodo,
                onDelete = viewModel::deleteTodo,
            )
        }
    }
}

@Composable
private fun Header() {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("M月d日 (E)", Locale.JAPANESE)
    Column {
        Text(
            text = today.format(formatter),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "今日もいい一日にしよう",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun TodoCard(
    todos: List<TodoItem>,
    onAdd: (String) -> Unit,
    onToggle: (TodoItem) -> Unit,
    onDelete: (TodoItem) -> Unit,
) {
    var input by remember { mutableStateOf("") }
    SectionCard(title = "TO DO") {
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("やることを追加") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = {
                onAdd(input)
                input = ""
            }) {
                Icon(Icons.Filled.Add, contentDescription = "追加")
            }
        }
        Spacer(Modifier.height(4.dp))
        if (todos.isEmpty()) {
            Text(
                text = "まだタスクがありません。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        } else {
            todos.forEach { todo ->
                TodoRow(todo, onToggle, onDelete)
            }
        }
    }
}

@Composable
private fun TodoRow(
    todo: TodoItem,
    onToggle: (TodoItem) -> Unit,
    onDelete: (TodoItem) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = todo.isDone, onCheckedChange = { onToggle(todo) })
        Text(
            text = todo.title,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
            color = if (todo.isDone) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onDelete(todo) }) {
            Icon(Icons.Filled.Delete, contentDescription = "削除")
        }
    }
}
