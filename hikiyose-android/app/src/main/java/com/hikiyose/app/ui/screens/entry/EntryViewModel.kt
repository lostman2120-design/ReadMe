package com.hikiyose.app.ui.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.data.entity.TodoItem
import com.hikiyose.app.data.repository.HikiyoseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EntryUiState(
    val manifestations: List<Manifestation> = emptyList(),
    val affirmations: List<Affirmation> = emptyList(),
    val todos: List<TodoItem> = emptyList(),
)

/** Wireframe ② : the entry page where the user sets up their manifestation. */
class EntryViewModel(private val repo: HikiyoseRepository) : ViewModel() {

    val uiState: StateFlow<EntryUiState> = combine(
        repo.activeManifestations(),
        repo.affirmations(),
        repo.todos(),
    ) { manifestations, affirmations, todos ->
        EntryUiState(manifestations, affirmations, todos)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EntryUiState(),
    )

    // 必ず引き寄せること
    fun addManifestation(text: String) {
        val t = text.trim()
        if (t.isEmpty()) return
        viewModelScope.launch { repo.saveManifestation(Manifestation(text = t)) }
    }

    fun updateManifestationText(item: Manifestation, text: String) {
        val t = text.trim()
        if (t.isEmpty()) return
        viewModelScope.launch { repo.updateManifestation(item.copy(text = t)) }
    }

    /** 叶った自分からのメッセージ — saved per manifestation item. */
    fun saveFulfilledMessage(item: Manifestation, message: String) {
        viewModelScope.launch { repo.updateManifestation(item.copy(fulfilledMessage = message)) }
    }

    fun deleteManifestation(item: Manifestation) {
        viewModelScope.launch { repo.deleteManifestation(item) }
    }

    // 毎日唱えること（アファメーション）
    fun addAffirmation(text: String) {
        val t = text.trim()
        if (t.isEmpty()) return
        viewModelScope.launch { repo.saveAffirmation(Affirmation(text = t)) }
    }

    fun deleteAffirmation(a: Affirmation) {
        viewModelScope.launch { repo.deleteAffirmation(a) }
    }

    // TO DO
    fun addTodo(title: String) {
        val t = title.trim()
        if (t.isEmpty()) return
        viewModelScope.launch { repo.saveTodo(TodoItem(title = t)) }
    }

    fun toggleTodo(item: TodoItem) {
        viewModelScope.launch { repo.updateTodo(item.copy(isDone = !item.isDone)) }
    }

    fun deleteTodo(item: TodoItem) {
        viewModelScope.launch { repo.deleteTodo(item) }
    }
}
