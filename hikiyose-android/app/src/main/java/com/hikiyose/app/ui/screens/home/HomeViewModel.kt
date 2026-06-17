package com.hikiyose.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.Quote
import com.hikiyose.app.data.QuotesData
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.data.entity.TodoItem
import com.hikiyose.app.data.repository.HikiyoseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeUiState(
    val affirmationOfDay: Affirmation? = null,
    val quoteOfDay: Quote = QuotesData.quoteOfDay(),
    val todos: List<TodoItem> = emptyList(),
)

class HomeViewModel(private val repo: HikiyoseRepository) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(repo.affirmations(), repo.todos()) { affirmations, todos ->
            HomeUiState(
                affirmationOfDay = pickAffirmationOfDay(affirmations),
                quoteOfDay = QuotesData.quoteOfDay(),
                todos = todos,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(),
        )

    fun addTodo(title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repo.saveTodo(TodoItem(title = trimmed)) }
    }

    fun toggleTodo(item: TodoItem) {
        viewModelScope.launch { repo.updateTodo(item.copy(isDone = !item.isDone)) }
    }

    fun deleteTodo(item: TodoItem) {
        viewModelScope.launch { repo.deleteTodo(item) }
    }

    /** Rotates through the user's affirmations, one stable choice per calendar day. */
    private fun pickAffirmationOfDay(list: List<Affirmation>): Affirmation? {
        if (list.isEmpty()) return null
        val index = (LocalDate.now().toEpochDay() % list.size).toInt()
        return list[index]
    }
}
