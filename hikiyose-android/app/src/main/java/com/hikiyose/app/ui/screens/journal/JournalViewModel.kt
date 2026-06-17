package com.hikiyose.app.ui.screens.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.JournalTemplate
import com.hikiyose.app.data.JournalTemplates
import com.hikiyose.app.data.entity.JournalEntry
import com.hikiyose.app.data.repository.HikiyoseRepository
import com.hikiyose.app.data.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class JournalViewModel(
    private val repo: HikiyoseRepository,
    settings: SettingsRepository,
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val entries: StateFlow<List<JournalEntry>> =
        repo.journalEntries().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    /** Dates (epoch-day) that already have an entry, for marking the calendar. */
    val entryDates: StateFlow<Set<Long>> =
        entries.map { list -> list.map { it.dateEpochDay }.toSet() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val selectedTemplate: StateFlow<JournalTemplate> =
        settings.selectedTemplateId.map { JournalTemplates.byId(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), JournalTemplates.default)

    val currentEntry: StateFlow<JournalEntry?> =
        _selectedDate.flatMapLatest { date -> repo.journalByDate(date.toEpochDay()) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun save(gratitude: String, body: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val existing = repo.getJournalByDate(date.toEpochDay())
            val entry = (existing ?: JournalEntry(
                dateEpochDay = date.toEpochDay(),
                templateId = selectedTemplate.value.id,
            )).copy(
                gratitude = gratitude,
                body = body,
                updatedAt = System.currentTimeMillis(),
            )
            repo.saveJournal(entry)
        }
    }

    fun delete(entry: JournalEntry) {
        viewModelScope.launch { repo.deleteJournal(entry) }
    }
}
