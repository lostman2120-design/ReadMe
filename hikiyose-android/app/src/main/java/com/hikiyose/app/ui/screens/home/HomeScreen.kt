package com.hikiyose.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.Method
import com.hikiyose.app.data.MethodsData
import com.hikiyose.app.ui.hikiyoseViewModel

/**
 * Wireframe ① : left-hand tabs to pick a 〇〇式, right pane shows that method's
 * column (how-to of that manifestation method).
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel = hikiyoseViewModel()) {
    val selectedId by viewModel.selectedMethodId.collectAsStateWithLifecycle()
    val method = MethodsData.byId(selectedId)

    Row(Modifier.fillMaxSize()) {
        MethodTabs(
            selectedId = selectedId,
            onSelect = viewModel::select,
            modifier = Modifier
                .width(108.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        MethodColumn(
            method = method,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 28.dp),
        )
    }
}

@Composable
private fun MethodTabs(
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        MethodsData.all.forEach { method ->
            val selected = method.id == selectedId
            Text(
                text = method.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selected) MaterialTheme.colorScheme.primaryContainer
                        else androidx.compose.ui.graphics.Color.Transparent
                    )
                    .clickable { onSelect(method.id) }
                    .padding(horizontal = 10.dp, vertical = 12.dp),
            )
        }
    }
}

@Composable
private fun MethodColumn(method: Method, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(method.name, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(
            method.summary,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        Text(
            method.body,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(24.dp))
    }
}
