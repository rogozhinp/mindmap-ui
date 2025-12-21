package com.mind.ui.view

import com.mind.ui.model.Node
import java.util.UUID

interface MainView {

    suspend fun loadBlankMindMap()

    suspend fun addChildNode(parentNode: Node)

    fun moveNode(id: UUID, dx: Float, dy: Float)

    suspend fun renameNode(id: UUID, newText: String)
}
