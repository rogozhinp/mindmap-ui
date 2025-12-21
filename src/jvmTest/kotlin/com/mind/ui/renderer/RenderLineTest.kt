package com.mind.ui.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.mind.ui.model.Node
import org.junit.jupiter.api.Test
import java.util.UUID

@OptIn(ExperimentalTestApi::class)
class LineRendererTest {

    @Test
    fun `renderLine should not crash when nodes have valid parent-child links`() = runDesktopComposeUiTest {
        // Arrange
        val mindMapId = UUID.randomUUID()
        val parentId = UUID.randomUUID()

        val nodes = listOf(
            Node(parentId, "Parent", 0.0, 0.0, mindMapId),
            Node(UUID.randomUUID(), "Child", 100.0, 100.0, mindMapId, parentNodeId = parentId)
        )

        // Act & Assert
        // Since Canvas drawing doesn't create accessibility nodes,
        // we verify the composition completes successfully.
        setContent {
            renderLine(
                nodes = nodes,
                canvasCenterX = 400f,
                canvasCenterY = 400f
            )
        }
    }

    @Test
    fun `renderLine should handle empty list safely`() = runDesktopComposeUiTest {
        setContent {
            renderLine(emptyList(), 0f, 0f)
        }
    }
}
