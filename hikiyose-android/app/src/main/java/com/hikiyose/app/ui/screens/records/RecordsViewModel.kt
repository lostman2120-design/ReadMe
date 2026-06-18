package com.hikiyose.app.ui.screens.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.data.repository.HikiyoseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Wireframe ⑤ : browse past achieved manifestations. */
class RecordsViewModel(private val repo: HikiyoseRepository) : ViewModel() {

    val achieved: StateFlow<List<Manifestation>> =
        repo.achievedManifestations().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList(),
        )

    /** Move an achievement back to the active list. */
    fun restore(item: Manifestation) {
        viewModelScope.launch {
            repo.updateManifestation(item.copy(isAchieved = false, achievedEpochDay = null))
        }
    }

    fun delete(item: Manifestation) {
        viewModelScope.launch { repo.deleteManifestation(item) }
    }
}
