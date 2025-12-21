package com.mind.ui.view.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import com.mind.ui.model.BlankMindMapResponse
import com.mind.ui.model.Node
import com.mind.ui.repository.MindMapRepository
import com.mind.ui.view.MainView
import java.util.*

class MainViewModel(private val repository: MindMapRepository) : MainView {

    val nodes = mutableStateListOf<Node>()
    var mindMapResponse by mutableStateOf<BlankMindMapResponse?>(null)
        private set

    override suspend fun loadBlankMindMap() {
        mindMapResponse = repository.getBlankMindMap()
        nodes.clear()
        nodes.addAll(mindMapResponse?.nodes ?: emptyList())
    }

    override suspend fun addChildNode(parentNode: Node) {
        // Pass the snapshot of the current state 'nodes' to the repository
        val newNode = repository.addChildNode(
            parentNode = parentNode,
            text = "New Child",
            currentNodes = nodes.toList()
        )
        nodes.add(newNode)
    }

    override fun moveNode(id: UUID, dx: Float, dy: Float) {
        val index = nodes.indexOfFirst { it.id == id }
        if (index != -1) {
            val node = nodes[index]
            nodes[index] = node.copy(x = node.x + dx, y = node.y + dy)
        }
    }

    override suspend fun renameNode(id: UUID, newText: String) {
        val index = nodes.indexOfFirst { it.id == id }
        if (index != -1) {
            val oldNode = nodes[index]
            val updatedNode = repository.updateNodeText(id, newText)
                .copy(x = oldNode.x, y = oldNode.y)
            nodes[index] = updatedNode
        }
    }
}
