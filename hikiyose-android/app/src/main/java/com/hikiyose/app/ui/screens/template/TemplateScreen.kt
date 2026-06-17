package com.hikiyose.app.ui.screens.template

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.JournalTemplate
import com.hikiyose.app.data.JournalTemplates
import com.hikiyose.app.ui.hikiyoseViewModel

@Composable
fun TemplateScreen(viewModel: TemplateViewModel = hikiyoseViewModel()) {
    val selectedId by viewModel.selectedTemplateId.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column {
                Text("ジャーナルの書式", style = MaterialTheme.typography.titleLarge)
                Text(
                    "選んだ書式が、ジャーナル作成時の見出しになります。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        items(JournalTemplates.all, key = { it.id }) { template ->
            TemplateCard(
                template = template,
                selected = template.id == selectedId,
                onSelect = { viewModel.select(template.id) },
            )
        }
    }
}

@Composable
private fun TemplateCard(
    template: JournalTemplate,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selected, onClick = onSelect)
                Column(Modifier.weight(1f).padding(start = 4.dp)) {
                    Text(template.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        template.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (selected) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "選択中",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            if (template.prompts.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                template.prompts.forEach { prompt ->
                    Text(
                        text = "・$prompt",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp),
                    )
                }
            }
        }
    }
}
