package com.hikiyose.app.ui.screens.achievement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.entity.Manifestation
import com.hikiyose.app.data.repository.HikiyoseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Wireframe ④ : record an achieved manifestation (達成した引き寄せ / 達成日 / 思っていること). */
class AchievementViewModel(private val repo: HikiyoseRepository) : ViewModel() {

    val activeManifestations: StateFlow<List<Manifestation>> =
        repo.activeManifestations().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList(),
        )

    /** Mark one of the active manifestations as achieved. */
    fun markAchieved(item: Manifestation, date: LocalDate, thoughts: String) {
        viewModelScope.launch {
            repo.updateManifestation(
                item.copy(
                    isAchieved = true,
                    achievedEpochDay = date.toEpochDay(),
                    thoughts = thoughts.trim(),
                )
            )
        }
    }

    /** Add a brand-new, already-achieved record directly. */
    fun addAchievedDirectly(text: String, date: LocalDate, thoughts: String) {
        val t = text.trim()
        if (t.isEmpty()) return
        viewModelScope.launch {
            repo.saveManifestation(
                Manifestation(
                    text = t,
                    isAchieved = true,
                    achievedEpochDay = date.toEpochDay(),
                    thoughts = thoughts.trim(),
                )
            )
        }
    }
}
