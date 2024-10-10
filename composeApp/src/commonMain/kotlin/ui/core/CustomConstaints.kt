package ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

// This class acts as a reference for positioning the children in the layout
data class Ref(val id: Int)

// ConstraintType Enum to define various constraints between the references
enum class ConstraintType {
    TopToBottomOf,
    BottomToTopOf,
    StartToEndOf,
    EndToStartOf,
    CenterHorizontallyTo,
    CenterVerticallyTo,
    AlignParentStart,
    AlignParentEnd,
    AlignParentTop,
    AlignParentBottom,
    CenterInParent,
    CenterHorizontallyInParent,
    CenterVerticallyInParent,
    CenterParentTop,
    CenterParentBottom
}

// Define constraints between references
data class RefConstraint(
    val first: Ref?,
    val second: Ref,
    val constraintType: ConstraintType
)

// A helper function to create unique references for each composable
@Composable
fun createRef(): Ref {
    return remember { Ref(id = nextId++) }
}

// This is a global ID generator for the references
var nextId = 0

// Modifier extension to add constraints dynamically to a composable
fun Modifier.constraintAs(
    ref: Ref,
    vararg constraints: RefConstraint
): Modifier = this.then(
    ConstraintModifier(ref, constraints.toList())
)

// Modifier to hold the reference and its constraints, passed through `ParentDataModifier`
data class ConstraintModifier(
    val ref: Ref,
    val constraints: List<RefConstraint>
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return this@ConstraintModifier
    }
}

@Composable
fun CustomConstraintLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // This list will hold all the collected constraints dynamically
    val allConstraints = mutableListOf<RefConstraint>()

    Layout(
        content = content,
        modifier = modifier
    ) { measurables, layoutConstraints ->

        // Measure each child composable, collecting their constraints from the modifiers
        val placeables = measurables.mapIndexed { index, measurable ->
            val modifier = measurable.parentData as? ConstraintModifier
            modifier?.let {
                // Collect all constraints from the modifier
                allConstraints.addAll(it.constraints)
            }
            measurable.measure(layoutConstraints)
        }

        // Map to store the calculated positions of each child
        val positions = mutableMapOf<Int, IntOffset>()

        // Set of unresolved children (waiting for dependency resolution)
        val unresolvedConstraints = allConstraints.toMutableList()

        // Continue processing unresolved constraints until all are resolved
        while (unresolvedConstraints.isNotEmpty()) {
            val iterator = unresolvedConstraints.iterator()

            while (iterator.hasNext()) {
                val constraint = iterator.next()
                val firstId = constraint.first?.id
                val secondId = constraint.second.id

                // If firstId is null, it's a parent constraint
                val firstPlaceable = firstId?.let { placeables[it] }
                val secondPlaceable = placeables[secondId]

                // Handle parent constraints or regular constraints
                when {
                    firstId == null -> {
                        // Parent constraints
                        when (constraint.constraintType) {
                            ConstraintType.AlignParentStart -> {
                                positions[secondId] = IntOffset(0, positions[secondId]?.y ?: 0)
                            }
                            ConstraintType.AlignParentEnd -> {
                                positions[secondId] = IntOffset(
                                    layoutConstraints.maxWidth - secondPlaceable.width,
                                    positions[secondId]?.y ?: 0
                                )
                            }
                            ConstraintType.AlignParentTop -> {
                                positions[secondId] = IntOffset(positions[secondId]?.x ?: 0, 0)
                            }
                            ConstraintType.AlignParentBottom -> {
                                positions[secondId] = IntOffset(
                                    positions[secondId]?.x ?: 0,
                                    layoutConstraints.maxHeight - secondPlaceable.height
                                )
                            }
                            ConstraintType.CenterInParent -> {
                                val centerX = (layoutConstraints.maxWidth - secondPlaceable.width) / 2
                                val centerY = (layoutConstraints.maxHeight - secondPlaceable.height) / 2
                                positions[secondId] = IntOffset(centerX, centerY)
                            }
                            ConstraintType.CenterHorizontallyInParent -> {
                                val centerX = (layoutConstraints.maxWidth - secondPlaceable.width) / 2
                                positions[secondId] = IntOffset(centerX, positions[secondId]?.y ?: 0)
                            }
                            ConstraintType.CenterVerticallyInParent -> {
                                val centerY = (layoutConstraints.maxHeight - secondPlaceable.height) / 2
                                positions[secondId] = IntOffset(positions[secondId]?.x ?: 0, centerY)
                            }
                            ConstraintType.CenterParentTop -> {
                                val centerX = (layoutConstraints.maxWidth - secondPlaceable.width) / 2
                                positions[secondId] = IntOffset(centerX, 0) // Center horizontally, aligned to the top
                            }
                            ConstraintType.CenterParentBottom -> {
                                val centerX = (layoutConstraints.maxWidth - secondPlaceable.width) / 2
                                positions[secondId] = IntOffset(centerX, layoutConstraints.maxHeight - secondPlaceable.height) // Center horizontally, aligned to the bottom
                            }
                            else -> {} // Ignore unsupported types here
                        }
                    }
                    positions.containsKey(firstId) -> {
                        // Regular constraints based on another composable
                        val firstPos = positions[firstId] ?: IntOffset(0, 0)
                        when (constraint.constraintType) {
                            ConstraintType.TopToBottomOf -> {
                                positions[secondId] = IntOffset(
                                    firstPos.x, // Same X position
                                    firstPos.y + firstPlaceable!!.height // Below the first
                                )
                            }
                            ConstraintType.BottomToTopOf -> {
                                positions[secondId] = IntOffset(
                                    firstPos.x, // Same X position
                                    firstPos.y - secondPlaceable.height // Above the first
                                )
                            }
                            ConstraintType.StartToEndOf -> {
                                positions[secondId] = IntOffset(
                                    firstPos.x + firstPlaceable!!.width, // Right of the first
                                    firstPos.y // Same Y position
                                )
                            }
                            ConstraintType.EndToStartOf -> {
                                positions[secondId] = IntOffset(
                                    firstPos.x - secondPlaceable.width, // Left of the first
                                    firstPos.y // Same Y position
                                )
                            }
                            ConstraintType.CenterHorizontallyTo -> {
                                val centerXFirst = firstPos.x + (firstPlaceable!!.width / 2)
                                positions[secondId] = IntOffset(
                                    centerXFirst - (secondPlaceable.width / 2),
                                    firstPos.y // Same Y position
                                )
                            }
                            ConstraintType.CenterVerticallyTo -> {
                                val centerYFirst = firstPos.y + (firstPlaceable!!.height / 2)
                                positions[secondId] = IntOffset(
                                    firstPos.x, // Same X position
                                    centerYFirst - (secondPlaceable.height / 2)
                                )
                            }
                            else->null
                        }
                    }
                }

                // Remove the resolved constraint
                iterator.remove()
            }
        }

        // Calculate the total layout width and height based on positions and sizes
        val layoutWidth = layoutConstraints.maxWidth // Use parent-given width
        val layoutHeight = layoutConstraints.maxHeight // Use parent-given height

        // Layout the children based on their calculated positions
        layout(layoutWidth, layoutHeight) {
            placeables.forEachIndexed { index, placeable ->
                val position = positions[index] ?: IntOffset(0, 0)
                placeable.placeRelative(position.x, position.y)
            }
        }
    }
}

@Composable
fun CustomConstraintLayoutExample() {
    // Manually create references for the children (without remember, for preview to work)
    val firstRef = createRef()
    val secondRef =createRef()
    val thirdRef =createRef()
    val fourthRef = createRef()

    // Define the layout with parent linking and composable linking
    CustomConstraintLayout {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .constraintAs(secondRef, RefConstraint(null, secondRef, ConstraintType.AlignParentStart))
        ) {
            Text("Start ")
        }
        // Second composable aligned to the parent center-top
        Box(
            modifier = Modifier
                .padding(8.dp)
                .constraintAs(secondRef, RefConstraint(null, secondRef, ConstraintType.CenterParentTop))
        ) {
            Text("Second composable (Center Parent Top)")
        }

        // Third composable centered in the parent
        Box(
            modifier = Modifier
                .padding(8.dp)
                .constraintAs(thirdRef, RefConstraint(null, thirdRef, ConstraintType.CenterInParent))
        ) {
            Text("Third composable (Centered in Parent)")
        }

        // Fourth composable centered horizontally at the bottom of the parent
        Box(
            modifier = Modifier
                .padding(8.dp)
                .constraintAs(fourthRef, RefConstraint(null, fourthRef, ConstraintType.CenterParentBottom))
        ) {
            Text("Fourth composable (Center Parent Bottom)")
        }
    }
}
