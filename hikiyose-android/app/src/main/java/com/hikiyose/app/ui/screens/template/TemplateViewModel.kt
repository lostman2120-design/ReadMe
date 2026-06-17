package com.hikiyose.app.ui.screens.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.JournalTemplates
import com.hikiyose.app.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TemplateViewModel(private val settings: SettingsRepository) : ViewModel() {

    val selectedTemplateId: StateFlow<String> =
        settings.selectedTemplateId.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = JournalTemplates.default.id,
        )

    fun select(id: String) {
        viewModelScope.launch { settings.setSelectedTemplate(id) }
    }
}
