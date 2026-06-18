package com.hikiyose.app.ui.screens.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.entity.JournalEntry
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.data.repository.HikiyoseRepository
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
class JournalViewModel(private val repo: HikiyoseRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    /** 引き寄せること (shown at the top of the journal, wireframe ③). */
    val manifestations: StateFlow<List<Manifestation>> =
        repo.activeManifestations().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList(),
        )

    val entries: StateFlow<List<JournalEntry>> =
        repo.journalEntries().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList(),
        )

    val entryDates: StateFlow<Set<Long>> =
        entries.map { list -> list.map { it.dateEpochDay }.toSet() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val currentEntry: StateFlow<JournalEntry?> =
        _selectedDate.flatMapLatest { date -> repo.journalByDate(date.toEpochDay()) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun save(idealDay: String, body: String, goodThings: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val existing = repo.getJournalByDate(date.toEpochDay())
            val entry = (existing ?: JournalEntry(dateEpochDay = date.toEpochDay())).copy(
                idealDay = idealDay,
                body = body,
                goodThings = goodThings,
                updatedAt = System.currentTimeMillis(),
            )
            repo.saveJournal(entry)
        }
    }
}
