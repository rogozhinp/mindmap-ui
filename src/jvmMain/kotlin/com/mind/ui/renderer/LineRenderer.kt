package com.mind.ui.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.mind.ui.model.Node
import com.mind.ui.theme.MindMapTheme

@Composable
fun renderLine(nodes: List<Node>, canvasCenterX: Float, canvasCenterY: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        nodes.forEach { child ->
            child.parentNodeId?.let { parentId ->
                val parent = nodes.find { it.id == parentId }
                if (parent != null) {
                    val childOffset = Offset(
                        x = canvasCenterX + child.x.toFloat(),
                        y = canvasCenterY + child.y.toFloat()
                    )
                    val parentOffset = Offset(
                        x = canvasCenterX + parent.x.toFloat(),
                        y = canvasCenterY + parent.y.toFloat()
                    )
                    drawLine(
                        color = MindMapTheme.lineColor,
                        start = parentOffset,
                        end = childOffset,
                        strokeWidth = MindMapTheme.lineWidth
                    )
                }
            }
        }
    }
}