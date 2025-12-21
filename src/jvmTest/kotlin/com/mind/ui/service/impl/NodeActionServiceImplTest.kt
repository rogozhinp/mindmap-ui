package com.mind.ui.service.impl

import com.mind.ui.model.Node
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class NodeActionServiceImplTest {

    private var addChildCalled = false
    private var nodeMovedCalled = false
    private var renameCalled = false

    private lateinit var service: NodeActionServiceImpl
    private val testNode = Node(UUID.randomUUID(), "Test Node", 0.0, 0.0, UUID.randomUUID())

    @BeforeEach
    fun setUp() {
        addChildCalled = false
        nodeMovedCalled = false
        renameCalled = false

        service = NodeActionServiceImpl(
            onAddChild = { addChildCalled = true },
            onNodeMoved = { _, _, _ -> nodeMovedCalled = true },
            onRenameNode = { _, _ -> renameCalled = true }
        )
    }

    @Test
    fun `onRightClick should call onAddChild callback`() {
        service.onRightClick(testNode)
        assertTrue(addChildCalled, "onAddChild was not called")
    }

    @Test
    fun `onNodeDragged should call onNodeMoved callback`() {
        service.onNodeDragged(testNode, 10f, 20f)
        assertTrue(nodeMovedCalled, "onNodeMoved was not called")
    }

    @Test
    fun `onRename should call onRenameNode callback`() {
        service.onRename(testNode, "New Name")
        assertTrue(renameCalled, "onRenameNode was not called")
    }
}
