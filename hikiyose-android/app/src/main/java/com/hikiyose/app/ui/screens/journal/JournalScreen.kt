package com.hikiyose.app.ui.screens.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikiyose.app.data.entity.JournalEntry
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.ui.components.SectionCard
import com.hikiyose.app.ui.hikiyoseViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun JournalScreen(viewModel: JournalViewModel = hikiyoseViewModel()) {
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val entryDates by viewModel.entryDates.collectAsStateWithLifecycle()
    val currentEntry by viewModel.currentEntry.collectAsStateWithLifecycle()
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val manifestations by viewModel.manifestations.collectAsStateWithLifecycle()

    var visibleMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { ManifestationsHeader(manifestations) }

        item {
            SectionCard(title = "カレンダー") {
                Spacer(Modifier.height(8.dp))
                MonthCalendar(
                    month = visibleMonth,
                    selectedDate = selectedDate,
                    entryDates = entryDates,
                    onPrev = { visibleMonth = visibleMonth.minusMonths(1) },
                    onNext = { visibleMonth = visibleMonth.plusMonths(1) },
                    onSelect = { viewModel.selectDate(it) },
                )
            }
        }

        item {
            JournalEditor(
                date = selectedDate,
                entry = currentEntry,
                onSave = viewModel::save,
            )
        }

        if (entries.isNotEmpty()) {
            item { Text("これまでの記録", style = MaterialTheme.typography.titleMedium) }
            items(entries, key = { it.id }) { entry ->
                EntryRow(entry = entry, onClick = { viewModel.selectDate(entry.date) })
            }
        }
    }
}

@Composable
private fun ManifestationsHeader(manifestations: List<Manifestation>) {
    SectionCard(title = "引き寄せること") {
        Spacer(Modifier.height(8.dp))
        if (manifestations.isEmpty()) {
            Text(
                "「記入」タブで引き寄せたいことを登録しましょう。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            manifestations.forEach { m ->
                Text("・${m.text}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun MonthCalendar(
    month: YearMonth,
    selectedDate: LocalDate,
    entryDates: Set<Long>,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onSelect: (LocalDate) -> Unit,
) {
    val today = LocalDate.now()
    val monthFormatter = DateTimeFormatter.ofPattern("yyyy年 M月", Locale.JAPANESE)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onPrev) { Icon(Icons.Filled.ChevronLeft, contentDescription = "前の月") }
        Text(month.format(monthFormatter), style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = onNext) { Icon(Icons.Filled.ChevronRight, contentDescription = "次の月") }
    }

    Row(Modifier.fillMaxWidth()) {
        listOf("日", "月", "火", "水", "木", "金", "土").forEach { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }

    val firstDay = month.atDay(1)
    val leadingBlanks = firstDay.dayOfWeek.value % 7 // SUN=0
    val daysInMonth = month.lengthOfMonth()
    val rows = (leadingBlanks + daysInMonth + 6) / 7

    Column {
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val dayNumber = row * 7 + col - leadingBlanks + 1
                    if (dayNumber in 1..daysInMonth) {
                        val date = month.atDay(dayNumber)
                        DayCell(
                            day = dayNumber,
                            isSelected = date == selectedDate,
                            isToday = date == today,
                            hasEntry = entryDates.contains(date.toEpochDay()),
                            onClick = { onSelect(date) },
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Box(Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    hasEntry: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.aspectRatio(1f).padding(2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        isToday -> MaterialTheme.colorScheme.primaryContainer
                        else -> androidx.compose.ui.graphics.Color.Transparent
                    }
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
            )
        }
        if (hasEntry && !isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 26.dp)
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
            )
        }
    }
}

@Composable
private fun JournalEditor(
    date: LocalDate,
    entry: JournalEntry?,
    onSave: (idealDay: String, body: String, goodThings: String) -> Unit,
) {
    var idealDay by remember(entry?.id) { mutableStateOf(entry?.idealDay ?: "") }
    var body by remember(entry?.id) { mutableStateOf(entry?.body ?: "") }
    var goodThings by remember(entry?.id) { mutableStateOf(entry?.goodThings ?: "") }
    var saved by remember(entry?.id) { mutableStateOf(false) }

    LaunchedEffect(entry?.id) {
        idealDay = entry?.idealDay ?: ""
        body = entry?.body ?: ""
        goodThings = entry?.goodThings ?: ""
    }

    val dateFormatter = DateTimeFormatter.ofPattern("M月d日 (E)", Locale.JAPANESE)

    SectionCard(title = date.format(dateFormatter)) {
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = idealDay,
            onValueChange = { idealDay = it; saved = false },
            label = { Text("今日はどんな日になれば最高？") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = body,
            onValueChange = { body = it; saved = false },
            label = { Text("やること・メモ") },
            placeholder = { Text("・\n・\n・") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = goodThings,
            onValueChange = { goodThings = it; saved = false },
            label = { Text("今日よかった・嬉しかったこと") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
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
            Button(
                onClick = { onSave(idealDay, body, goodThings); saved = true },
                enabled = idealDay.isNotBlank() || body.isNotBlank() || goodThings.isNotBlank(),
            ) { Text("保存") }
        }
    }
}

@Composable
private fun EntryRow(entry: JournalEntry, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("M/d (E)", Locale.JAPANESE)
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(entry.date.format(formatter), style = MaterialTheme.typography.labelLarge)
            val preview = listOf(entry.idealDay, entry.body, entry.goodThings)
                .filter { it.isNotBlank() }
                .joinToString(" / ")
                .ifBlank { "（内容なし）" }
            Text(
                text = preview,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
