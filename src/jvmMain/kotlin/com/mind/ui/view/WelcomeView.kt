package com.mind.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mind.ui.view.impl.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun WelcomeView(viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val mindMapResponse = viewModel.mindMapResponse
    val nodes = viewModel.nodes

    if (mindMapResponse == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                scope.launch {
                    viewModel.loadBlankMindMap()
                }
            }) {
                Text("Create New Mind Map")
            }
        }
    } else {
        MapView(
            nodes = nodes,
            onAddChild = { parentNode ->
                scope.launch {
                    viewModel.addChildNode(parentNode)
                }
            },
            onNodeMoved = { id, dx, dy ->
                viewModel.moveNode(id, dx, dy)
            },
            onRenameNode = { id, newText ->
                if (newText.isNotBlank()) {
                    scope.launch {
                        viewModel.renameNode(id, newText)
                    }
                }
            }
        )
    }
}
