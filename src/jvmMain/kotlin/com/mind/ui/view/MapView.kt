package com.mind.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mind.ui.model.Node
import com.mind.ui.renderer.renderLine
import com.mind.ui.renderer.renderNode
import com.mind.ui.service.NodeLayoutService
import com.mind.ui.service.impl.NodeLayoutServiceImpl
import com.mind.ui.theme.MindMapTheme
import java.util.UUID

@Composable
fun MapView(
    nodes: List<Node>,
    onAddChild: (parentNode: Node) -> Unit = {},
    onNodeMoved: (id: UUID, dx: Float, dy: Float) -> Unit,
    onRenameNode: (id: UUID, newText: String) -> Unit,
    layoutService: NodeLayoutService = NodeLayoutServiceImpl()
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MindMapTheme.backgroundColor)
    ) {
        val centerX = constraints.maxWidth / 2f
        val centerY = constraints.maxHeight / 2f

        renderLine(nodes, centerX, centerY)

        renderNode(
            nodes = nodes,
            canvasCenterX = centerX,
            canvasCenterY = centerY,
            onAddChild = onAddChild,
            onNodeMoved = onNodeMoved,
            onRenameNode = onRenameNode,
            layoutService = layoutService
        )
    }
}
