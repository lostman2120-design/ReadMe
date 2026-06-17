package com.hikiyose.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.compose.runtime.Composable
import com.hikiyose.app.HikiyoseApplication
import com.hikiyose.app.ui.screens.affirmation.AffirmationViewModel
import com.hikiyose.app.ui.screens.home.HomeViewModel
import com.hikiyose.app.ui.screens.journal.JournalViewModel
import com.hikiyose.app.ui.screens.template.TemplateViewModel

/**
 * Builds ViewModels from the app's manually-wired dependencies.
 * Kept as one factory so screens just call the [hikiyoseViewModel] helper.
 */
class ViewModelFactory(private val app: HikiyoseApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(app.repository) as T
            modelClass.isAssignableFrom(JournalViewModel::class.java) ->
                JournalViewModel(app.repository, app.settings) as T
            modelClass.isAssignableFrom(AffirmationViewModel::class.java) ->
                AffirmationViewModel(app.repository) as T
            modelClass.isAssignableFrom(TemplateViewModel::class.java) ->
                TemplateViewModel(app.settings) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}

@Composable
inline fun <reified T : ViewModel> hikiyoseViewModel(): T = viewModel(
    factory = object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>, extras: CreationExtras): VM {
            val app = extras[APPLICATION_KEY] as HikiyoseApplication
            return ViewModelFactory(app).create(modelClass)
        }
    }
)
