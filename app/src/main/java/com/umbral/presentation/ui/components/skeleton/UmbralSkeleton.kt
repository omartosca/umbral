package com.umbral.presentation.ui.components.skeleton

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.unit.dp

/**
 * Umbral Design System 2.0 - Skeleton Loading Component
 *
 * Base skeleton component with shimmer animation for loading states.
 *
 * ## Visual Specs
 * - Background: surfaceContainerHigh (MD3 role, adapts to light/dark + dynamic color)
 * - Shimmer Highlight: surfaceContainerHighest (MD3 role, slightly lighter/darker than background)
 * - Animation: shimmer left-to-right, 1200ms, infinite
 *
 * ## Accessibility
 * - Respects "Reduce Motion" setting - shows static skeleton without animation
 *
 * ## Usage
 * ```kotlin
 * // Simple skeleton rectangle
 * UmbralSkeleton(
 *     modifier = Modifier.size(100.dp, 20.dp)
 * )
 *
 * // Custom shape
 * UmbralSkeleton(
 *     modifier = Modifier.size(48.dp),
 *     shape = CircleShape
 * )
 * ```
 *
 * @param modifier Modifier for size and positioning
 * @param shape Shape of the skeleton element (default: 8dp rounded corners)
 */
@Composable
fun UmbralSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    val accessibilityManager = LocalAccessibilityManager.current

    // Check if "Reduce Motion" is enabled
    val isReduceMotionEnabled = accessibilityManager?.calculateRecommendedTimeoutMillis(
        originalTimeoutMillis = Long.MAX_VALUE,
        containsIcons = false,
        containsText = false,
        containsControls = false
    ) == Long.MAX_VALUE

    // Skeleton colors from MD3 surface container roles (auto-adapts to light/dark + dynamic color)
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val highlightColor = MaterialTheme.colorScheme.surfaceContainerHighest

    // Shimmer animation colors
    val shimmerColors = listOf(
        backgroundColor,
        highlightColor,
        backgroundColor
    )

    // Animated shimmer effect (unless reduced motion is enabled)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (isReduceMotionEnabled) 0f else 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = if (isReduceMotionEnabled) {
        // Static gradient for reduced motion
        Brush.linearGradient(
            colors = listOf(backgroundColor, backgroundColor)
        )
    } else {
        // Animated shimmer gradient
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 1000f, translateAnim - 1000f),
            end = Offset(translateAnim, translateAnim)
        )
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}

/**
 * Skeleton component for text lines with variable widths
 *
 * ## Usage
 * ```kotlin
 * SkeletonText(
 *     lines = 3,
 *     modifier = Modifier.fillMaxWidth()
 * )
 * ```
 *
 * @param lines Number of text lines to show (default: 3)
 * @param modifier Modifier for container
 * @param lineHeight Height of each line (default: 16dp)
 * @param lineSpacing Space between lines (default: 8dp)
 */
@Composable
fun SkeletonText(
    lines: Int = 3,
    modifier: Modifier = Modifier,
    lineHeight: androidx.compose.ui.unit.Dp = 16.dp,
    lineSpacing: androidx.compose.ui.unit.Dp = 8.dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(lineSpacing)
    ) {
        repeat(lines) { index ->
            val widthFraction = when (index) {
                lines - 1 -> 0.6f // Last line: 60%
                lines - 2 -> 0.9f // Second to last: 90%
                else -> 1f // Other lines: 100%
            }

            UmbralSkeleton(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .height(lineHeight)
            )
        }
    }
}
