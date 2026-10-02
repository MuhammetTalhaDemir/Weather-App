package com.kampplus.hava.presentation.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

/**
 * Mevcut vektör arkaplan görselinin üzerine sonsuz döngülü canlı hava efektleri çizen Composable.
 */
@Composable
fun AnimatedWeatherBackground(
    backgroundRes: Int,
    weatherCode: Int?,
    isDay: Boolean,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "weather_anim")

    // Rain drop fall animation
    val rainOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rain",
    )

    // Snow fall & sway animation
    val snowOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 120f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "snow_y",
    )

    val snowOffsetX by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "snow_x",
    )

    // Sun scale pulse
    val sunScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "sun_scale",
    )

    // Cloud drift
    val cloudDriftX by infiniteTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "cloud_drift",
    )

    // Twinkling star alpha
    val starAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "star_twinkle",
    )

    // Lightning flash alpha
    val lightningAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1700, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "lightning",
    )

    val code = weatherCode ?: -1
    val isDrizzleRain = (code >= 51) && (code <= 67)
    val isHeavyRain = (code >= 80) && (code <= 82)
    val isRainy = isDrizzleRain || isHeavyRain
    val isLightSnow = (code >= 71) && (code <= 77)
    val isHeavySnow = (code >= 85) && (code <= 86)
    val isSnowy = isLightSnow || isHeavySnow
    val isThunderstorm = (code >= 95) && (code <= 99)
    val isNightClear = (!isDay) && ((code == 0) || (code == 1) || (weatherCode == null))
    val isSunnyDay = (isDay) && ((code == 0) || (code == 1))
    val isCloudy = ((code >= 2) && (code <= 3)) || (code == 45) || (code == 48)

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            when {
                // Rain / Drizzle
                isRainy -> {
                    val dropCount = 12
                    val dropLength = 22f
                    for (i in 0 until dropCount) {
                        val startX = (width * 0.15f) + (((width * 0.75f) / dropCount) * i)
                        val initialY = (height * 0.1f) + (i * 15f)
                        val currentY = (initialY + rainOffsetY) % (height + dropLength)
                        drawLine(
                            color = Color(0xFF0288D1).copy(alpha = 0.65f),
                            start = Offset(startX, currentY),
                            end = Offset(startX - 4f, currentY + dropLength),
                            strokeWidth = 3f,
                        )
                    }
                }

                // Snow
                isSnowy -> {
                    val flakeCount = 10
                    for (i in 0 until flakeCount) {
                        val baseX = (width * 0.1f) + (((width * 0.8f) / flakeCount) * i)
                        val offsetSign = if ((i % 2) == 0) snowOffsetX else -snowOffsetX
                        val currentX = baseX + offsetSign
                        val initialY = (height * 0.05f) + (i * 18f)
                        val currentY = (initialY + snowOffsetY) % (height + 10f)
                        drawCircle(
                            color = Color.White.copy(alpha = 0.85f),
                            radius = 5f + ((i % 3) * 1.5f),
                            center = Offset(currentX, currentY),
                        )
                    }
                }

                // Thunderstorm
                isThunderstorm -> {
                    drawCircle(
                        color = Color(0xFFFFD54F).copy(alpha = lightningAlpha * 0.35f),
                        radius = width * 0.25f,
                        center = Offset(width * 0.75f, height * 0.4f),
                    )
                }

                // Night Stars
                isNightClear -> {
                    val starPositions = listOf(
                        Offset(width * 0.65f, height * 0.20f),
                        Offset(width * 0.72f, height * 0.40f),
                        Offset(width * 0.88f, height * 0.25f),
                        Offset(width * 0.55f, height * 0.35f),
                        Offset(width * 0.80f, height * 0.50f),
                    )
                    starPositions.forEachIndexed { idx, pos ->
                        val isEvenStar = (idx % 2) == 0
                        val alpha = if (isEvenStar) starAlpha else (1.2f - starAlpha).coerceIn(0.2f, 1.0f)
                        drawCircle(
                            color = Color.White.copy(alpha = alpha),
                            radius = 3.5f,
                            center = pos,
                        )
                    }
                }

                // Sunny / Clear Sky
                isSunnyDay -> {
                    scale(sunScale, pivot = Offset(width * 0.85f, height * 0.3f)) {
                        drawCircle(
                            color = Color(0xFFFFA726).copy(alpha = 0.20f),
                            radius = width * 0.22f,
                            center = Offset(width * 0.85f, height * 0.3f),
                        )
                    }
                }

                // Cloudy / Overcast / Foggy
                isCloudy -> {
                    translate(left = cloudDriftX, top = 0f) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.12f),
                            radius = width * 0.20f,
                            center = Offset(width * 0.70f, height * 0.4f),
                        )
                    }
                }
            }
        }
    }
}
