package com.mind.ui

import androidx.compose.ui.window.singleWindowApplication
import com.mind.ui.network.MindMapApi
import com.mind.ui.repository.impl.MindMapRepositoryImpl
import com.mind.ui.view.WelcomeView
import com.mind.ui.view.impl.MainViewModel

fun main() = singleWindowApplication(title = "Mind Map Designer") {
    val repository = MindMapRepositoryImpl(MindMapApi.Default)

    val viewModel = MainViewModel(repository)

    WelcomeView(viewModel)
}
