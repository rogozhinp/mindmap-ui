package com.mind.ui.renderer

import androidx.compose.runtime.Composable
import com.mind.ui.model.Node
import com.mind.ui.service.NodeActionService
import com.mind.ui.service.NodeLayoutService
import com.mind.ui.service.impl.NodeActionServiceImpl
import com.mind.ui.service.impl.NodeLayoutServiceImpl
import java.util.UUID

@Composable
fun renderNode(
    nodes: List<Node>,
    canvasCenterX: Float,
    canvasCenterY: Float,
    onAddChild: (parentNode: Node) -> Unit,
    onNodeMoved: (id: UUID, dx: Float, dy: Float) -> Unit,
    onRenameNode: (id: UUID, newText: String) -> Unit,
    layoutService: NodeLayoutService = NodeLayoutServiceImpl()
) {
    val actionService: NodeActionService = NodeActionServiceImpl(
        onAddChild = onAddChild,
        onNodeMoved = onNodeMoved,
        onRenameNode = onRenameNode
    )

    nodes.forEach { node ->
        getNodeElement(
            node = node,
            canvasCenterX = canvasCenterX,
            canvasCenterY = canvasCenterY,
            layoutService = layoutService,
            actionService = actionService
        )
    }
}
