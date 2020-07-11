package com.abysl.gdxcrawler.utils

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport

class IntFitViewport(targetWidth: Float, targetHeight: Float) : ScalingViewport(Scaling.fit, targetWidth, targetHeight) {
    private companion object {
        private val temp: Vector2 = Vector2()
    }

    private fun applyIntScaling(sourceWidth: Float, sourceHeight: Float, targetWidth: Float, targetHeight: Float): Vector2 {
        val targetRatio = targetHeight / targetWidth
        val sourceRatio = sourceHeight / sourceWidth

        val exactScale = if (targetRatio > sourceRatio) targetWidth / sourceWidth else targetHeight / sourceHeight
        val scale = MathUtils.round(exactScale)

        temp.x = sourceWidth * scale
        temp.y = sourceHeight * scale
        return temp
    }

    override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        val scaled = applyIntScaling(worldWidth, worldHeight, screenWidth.toFloat(), screenHeight.toFloat())

        val viewportWidth = MathUtils.round(scaled.x)
        val viewportHeight = MathUtils.round(scaled.y)

        setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2,
                viewportWidth, viewportHeight)

        apply(centerCamera)
    }
}