package com.hikiyose.app.ui.screens.affirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.data.repository.HikiyoseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AffirmationViewModel(private val repo: HikiyoseRepository) : ViewModel() {

    val affirmations: StateFlow<List<Affirmation>> =
        repo.affirmations().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun add(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repo.saveAffirmation(Affirmation(text = trimmed)) }
    }

    fun update(affirmation: Affirmation, newText: String) {
        val trimmed = newText.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repo.updateAffirmation(affirmation.copy(text = trimmed)) }
    }

    fun toggleFavorite(affirmation: Affirmation) {
        viewModelScope.launch {
            repo.updateAffirmation(affirmation.copy(isFavorite = !affirmation.isFavorite))
        }
    }

    fun delete(affirmation: Affirmation) {
        viewModelScope.launch { repo.deleteAffirmation(affirmation) }
    }
}
