package com.mind.ui.repository

import com.mind.ui.model.BlankMindMapResponse
import com.mind.ui.model.Node
import java.util.*

interface MindMapRepository {

    suspend fun getBlankMindMap(): BlankMindMapResponse

    suspend fun addChildNode(
        parentNode: Node,
        text: String = "New Child",
        currentNodes: List<Node>
    ): Node

    suspend fun updateNodeText(nodeId: UUID, newText: String): Node
}
