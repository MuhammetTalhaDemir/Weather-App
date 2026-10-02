package com.kampplus.hava.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kampplus.hava.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Detay ekranı açıldığında havanın durumuna göre 1.5 saniyelik karşılama efektini çizen Composable.
 */
@Composable
fun WeatherEntranceOverlay(
    weatherCode: Int?,
    isDay: Boolean,
    modifier: Modifier = Modifier,
) {
    var isVisible by remember { mutableStateOf(true) }
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(weatherCode, isDay) {
        isVisible = true
        animProgress.snapTo(0f)
        launch {
            animProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            )
        }
        delay(1500)
        isVisible = false
    }

    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut(animationSpec = tween(500)),
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val progress = animProgress.value

            val code = weatherCode ?: -1
            val isClearCode = (code == 0) || (code == 1)
            val isSunnyEntrance = isClearCode && isDay
            val isSnow1 = (code >= 71) && (code <= 77)
            val isSnow2 = (code >= 85) && (code <= 86)
            val isSnowyEntrance = isSnow1 || isSnow2
            val isRain1 = (code >= 51) && (code <= 67)
            val isRain2 = (code >= 80) && (code <= 82)
            val isRainyEntrance = isRain1 || isRain2
            val isStormyEntrance = (code >= 95) && (code <= 99)

            when {
                // Güneşli / Açık (Gündüz)
                isSunnyEntrance -> {
                    val scaleVal = 0.5f + (progress * 0.8f)
                    val alphaVal = (1f - progress).coerceIn(0f, 1f)
                    Image(
                        painter = painterResource(id = R.drawable.ic_weather_sunny),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize(0.35f)
                            .graphicsLayer {
                                scaleX = scaleVal
                                scaleY = scaleVal
                                rotationZ = progress * 180f
                                alpha = alphaVal
                            },
                    )
                }

                // Karlı
                isSnowyEntrance -> {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val flakeCount = 20
                        val alphaVal = (1f - progress).coerceIn(0f, 1f)

                        for (i in 0 until flakeCount) {
                            val x = (width * 0.05f) + (((width * 0.9f) / flakeCount) * i)
                            val y = (progress * height * 1.2f) + (i * 12f)
                            drawCircle(
                                color = Color(0xFF0288D1).copy(alpha = alphaVal * 0.8f),
                                radius = 6f + ((i % 4) * 2f),
                                center = Offset(x, y % height),
                            )
                        }
                    }
                }

                // Yağmurlu
                isRainyEntrance -> {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val dropCount = 24
                        val alphaVal = (1f - progress).coerceIn(0f, 1f)

                        for (i in 0 until dropCount) {
                            val x = (width * 0.05f) + (((width * 0.9f) / dropCount) * i)
                            val y = (progress * height * 1.5f) + (i * 20f)
                            drawLine(
                                color = Color(0xFF0288D1).copy(alpha = alphaVal * 0.7f),
                                start = Offset(x, y % height),
                                end = Offset(x - 6f, (y % height) + 30f),
                                strokeWidth = 4f,
                            )
                        }
                    }
                }

                // Fırtınalı
                isStormyEntrance -> {
                    val isFlashTime = (progress < 0.3f) || ((progress >= 0.5f) && (progress <= 0.7f))
                    val flashAlpha = if (isFlashTime) {
                        (1f - progress).coerceIn(0f, 0.6f)
                    } else {
                        0.05f
                    }
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(color = Color(0xFFFFD54F).copy(alpha = flashAlpha))
                    }
                }
            }
        }
    }
}
