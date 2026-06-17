package com.hikiyose.app.ui.screens.affirmation

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.ui.components.EmptyState
import com.hikiyose.app.ui.hikiyoseViewModel

@Composable
fun AffirmationScreen(viewModel: AffirmationViewModel = hikiyoseViewModel()) {
    val affirmations by viewModel.affirmations.collectAsStateWithLifecycle()
    var editorTarget by remember { mutableStateOf<EditorTarget?>(null) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { editorTarget = EditorTarget.New },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("追加") },
            )
        },
    ) { padding ->
        if (affirmations.isEmpty()) {
            EmptyState(
                message = "アファメーションを追加しましょう。\n「私は〜」という現在形・肯定形の言葉が効果的です。",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        text = "あなたのアファメーション",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                items(affirmations, key = { it.id }) { item ->
                    AffirmationCard(
                        affirmation = item,
                        onToggleFavorite = { viewModel.toggleFavorite(item) },
                        onEdit = { editorTarget = EditorTarget.Edit(item) },
                        onDelete = { viewModel.delete(item) },
                    )
                }
            }
        }
    }

    when (val target = editorTarget) {
        is EditorTarget.New -> EditorDialog(
            initial = "",
            title = "アファメーションを追加",
            onDismiss = { editorTarget = null },
            onConfirm = { viewModel.add(it); editorTarget = null },
        )
        is EditorTarget.Edit -> EditorDialog(
            initial = target.affirmation.text,
            title = "編集",
            onDismiss = { editorTarget = null },
            onConfirm = { viewModel.update(target.affirmation, it); editorTarget = null },
        )
        null -> Unit
    }
}

private sealed interface EditorTarget {
    data object New : EditorTarget
    data class Edit(val affirmation: Affirmation) : EditorTarget
}

@Composable
private fun AffirmationCard(
    affirmation: Affirmation,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = affirmation.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (affirmation.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "お気に入り",
                    tint = if (affirmation.isFavorite) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "編集")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "削除")
            }
        }
    }
}

@Composable
private fun EditorDialog(
    initial: String,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("私は〜") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(4.dp))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }, enabled = text.isNotBlank()) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("キャンセル") }
        },
    )
}
