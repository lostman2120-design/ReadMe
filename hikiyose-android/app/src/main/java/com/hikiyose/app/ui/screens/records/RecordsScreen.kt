package com.hikiyose.app.ui.screens.records

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.ui.components.EmptyState
import com.hikiyose.app.ui.hikiyoseViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RecordsScreen(viewModel: RecordsViewModel = hikiyoseViewModel()) {
    val achieved by viewModel.achieved.collectAsStateWithLifecycle()

    if (achieved.isEmpty()) {
        EmptyState(message = "まだ達成記録がありません。\n「達成」タブで引き寄せの達成を記録しましょう。")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column {
                Text("達成記録", style = MaterialTheme.typography.titleLarge)
                Text(
                    "これまでに叶えた引き寄せ（${achieved.size}件）",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        items(achieved, key = { it.id }) { m ->
            RecordCard(
                m = m,
                onRestore = { viewModel.restore(m) },
                onDelete = { viewModel.delete(m) },
            )
        }
    }
}

@Composable
private fun RecordCard(m: Manifestation, onRestore: () -> Unit, onDelete: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("yyyy年M月d日", Locale.JAPANESE)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    m.achievedDate?.format(formatter) ?: "達成日未設定",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onRestore) {
                    Icon(Icons.Filled.Restore, contentDescription = "未達成に戻す")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "削除")
                }
            }
            Text(m.text, style = MaterialTheme.typography.titleMedium)
            if (m.thoughts.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    m.thoughts,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
