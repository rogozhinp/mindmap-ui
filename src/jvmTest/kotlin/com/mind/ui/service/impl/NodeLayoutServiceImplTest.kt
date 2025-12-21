package com.mind.ui.service.impl

import androidx.compose.ui.unit.Density
import com.mind.ui.model.Node
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class NodeLayoutServiceImplTest {

    private val service = NodeLayoutServiceImpl()
    private val mindMapId = UUID.randomUUID()

    private val testDensity = Density(density = 1f, fontScale = 1f)

    @Test
    fun `calculateOffset should center the node at the canvas center when node coordinates are zero`() {
        // Arrange
        val node = Node(UUID.randomUUID(), "Center Node", 0.0, 0.0, mindMapId)
        val canvasCenterX = 500f
        val canvasCenterY = 500f

        // Act - Now passing testDensity
        val offset = service.calculateOffset(node, canvasCenterX, canvasCenterY, testDensity)

        // Assert
        // Expected X: 500 (center) + 0 (node.x) - 80 (half of 160px) = 420
        // Expected Y: 500 (center) + 0 (node.y) - 30 (half of 60px) = 470
        assertEquals(420, offset.x)
        assertEquals(470, offset.y)
    }

    @Test
    fun `calculateOffset should correctly apply positive relative coordinates`() {
        // Arrange
        val node = Node(UUID.randomUUID(), "Right Down Node", 100.0, 200.0, mindMapId)
        val canvasCenterX = 500f
        val canvasCenterY = 500f

        // Act
        val offset = service.calculateOffset(node, canvasCenterX, canvasCenterY, testDensity)

        // Assert
        // Expected X: 500 + 100 - 80 = 520
        // Expected Y: 500 + 200 - 30 = 670
        assertEquals(520, offset.x)
        assertEquals(670, offset.y)
    }

    @Test
    fun `calculateOffset should correctly apply negative relative coordinates`() {
        // Arrange
        val node = Node(UUID.randomUUID(), "Left Up Node", -100.0, -50.0, mindMapId)
        val canvasCenterX = 500f
        val canvasCenterY = 500f

        // Act
        val offset = service.calculateOffset(node, canvasCenterX, canvasCenterY, testDensity)

        // Assert
        // Expected X: 500 - 100 - 80 = 320
        // Expected Y: 500 - 50 - 30 = 420
        assertEquals(320, offset.x)
        assertEquals(420, offset.y)
    }
}
