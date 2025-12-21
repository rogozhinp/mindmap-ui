package com.mind.ui.service.impl

import com.mind.ui.model.Node
import com.mind.ui.service.NodeActionService
import java.util.UUID

class NodeActionServiceImpl(
    private val onAddChild: (Node) -> Unit,
    private val onNodeMoved: (UUID, Float, Float) -> Unit,
    private val onRenameNode: (UUID, String) -> Unit
) : NodeActionService {

    override fun onRightClick(node: Node) {
        onAddChild(node)
    }

    override fun onNodeDragged(node: Node, dx: Float, dy: Float) {
        onNodeMoved(node.id, dx, dy)
    }

    override fun onRename(node: Node, newText: String) {
        onRenameNode(node.id, newText)
    }
}