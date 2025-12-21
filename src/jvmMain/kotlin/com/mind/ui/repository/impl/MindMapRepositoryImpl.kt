package com.mind.ui.repository.impl

import com.mind.ui.model.BlankMindMapResponse
import com.mind.ui.model.Node
import com.mind.ui.network.MindMapApi
import com.mind.ui.repository.MindMapRepository
import com.mind.ui.theme.MindMapTheme
import java.util.*

class MindMapRepositoryImpl(private val api: MindMapApi) : MindMapRepository {

    override suspend fun getBlankMindMap(): BlankMindMapResponse {
        return api.getBlankMindMap()
    }

    override suspend fun addChildNode(
        parentNode: Node,
        text: String,
        currentNodes: List<Node>
    ): Node {
        // Count existing children of this parent
        val siblings = currentNodes.filter { it.parentNodeId == parentNode.id }
        val siblingCount = siblings.size

        // Calculate Y offset: 0, +1, -1, +2, -2 pattern
        val multiplier = if (siblingCount == 0) 0
        else if (siblingCount % 2 == 1) (siblingCount / 2 + 1)
        else -(siblingCount / 2)

        val verticalOffset = multiplier * MindMapTheme.nodeVerticalSpacing

        return api.addChildNode(
            parentNodeId = parentNode.id,
            text = text,
            x = parentNode.x + MindMapTheme.nodeHorizontalSpacing,
            y = parentNode.y + verticalOffset
        )
    }

    override suspend fun updateNodeText(nodeId: UUID, newText: String): Node {
        return api.updateNodeText(nodeId, newText)
    }
}
