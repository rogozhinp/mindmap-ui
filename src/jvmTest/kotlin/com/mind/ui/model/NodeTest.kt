package com.mind.ui.model

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NodeTest {

    @Test
    fun `node should have correct parent`() {
        val mindMapId = UUID.randomUUID()
        val parent = Node(UUID.randomUUID(), "Parent", 0.0, 0.0, mindMapId)
        val child = Node(UUID.randomUUID(), "Child", 100.0, 100.0, mindMapId, parentNodeId = parent.id)

        assertEquals(parent.id, child.parentNodeId)
        assertEquals("Child", child.text)
    }

    @Test
    fun `node without parent should have null parentNodeId`() {
        val mindMapId = UUID.randomUUID()
        val node = Node(UUID.randomUUID(), "Standalone", 0.0, 0.0, mindMapId)

        assertNull(node.parentNodeId)
        assertEquals("Standalone", node.text)
    }
}
