package com.mind.ui.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mind.ui.model.Node
import com.mind.ui.service.NodeActionService
import com.mind.ui.service.NodeLayoutService
import com.mind.ui.service.impl.NodeLayoutServiceImpl
import com.mind.ui.theme.MindMapTheme

@Composable
fun getNodeElement(
    node: Node,
    canvasCenterX: Float,
    canvasCenterY: Float,
    layoutService: NodeLayoutService = NodeLayoutServiceImpl(),
    actionService: NodeActionService
) {
    val density = LocalDensity.current

    var isEditing by remember { mutableStateOf(false) }
    var textDraft by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    val offset = layoutService.calculateOffset(
        node = node,
        canvasCenterX = canvasCenterX,
        canvasCenterY = canvasCenterY,
        density = density
    )

    Box(
        modifier = Modifier
            .offset { offset }
            .size(
                width = MindMapTheme.nodeWidth,
                height = MindMapTheme.nodeHeight
            )
            .background(
                color = MindMapTheme.nodeColor,
                shape = MindMapTheme.nodeShape
            )
            .pointerInput(node.id) {
                detectTapGestures(
                    onDoubleTap = {
                        textDraft = ""
                        isEditing = true
                    }
                )
            }
            .pointerInput(node.id) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.buttons.isSecondaryPressed) {
                            actionService.onRightClick(node)
                        }
                    }
                }
            }
            .pointerInput(node.id) {
                detectDragGestures { change, dragAmount ->
                    if (!isEditing) {
                        change.consume()
                        actionService.onNodeDragged(
                            node,
                            dragAmount.x,
                            dragAmount.y
                        )
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (isEditing) {
            TextField(
                value = textDraft,
                onValueChange = { textDraft = it },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .focusRequester(focusRequester)
                    .onPreviewKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            when (event.key) {
                                Key.Enter, Key.NumPadEnter -> {
                                    actionService.onRename(node, textDraft)
                                    isEditing = false
                                    true
                                }

                                Key.Escape -> {
                                    textDraft = node.text
                                    isEditing = false
                                    true
                                }

                                else -> false
                            }
                        } else {
                            false
                        }
                    }
            )

            LaunchedEffect(isEditing) {
                if (isEditing) {
                    focusRequester.requestFocus()
                }
            }
        } else {
            Text(
                text = node.text,
                style = MaterialTheme.typography.h6,
                color = MindMapTheme.nodeTextColor
            )
        }
    }
}
