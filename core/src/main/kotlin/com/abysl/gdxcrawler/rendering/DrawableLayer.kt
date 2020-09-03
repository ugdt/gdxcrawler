package com.abysl.gdxcrawler.rendering

import com.abysl.gdxcrawler.utils.TileConstants
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import ktx.graphics.use
import ktx.tiled.property

class DrawableLayer(
    private val camera: OrthographicCamera,
    private val parent: TiledMap,
    private val layer: TiledMapTileLayer,
    private val tileSize: Int,
) : Drawable {

    private val bitmapFont: BitmapFont = BitmapFont()
    override var depth: Int = layer.property(TileConstants.DEPTH, 0)

    override fun draw(batch: SpriteBatch, x: Float, y: Float, width: Float, height: Float) {
        val renderer = OrthogonalTiledMapRenderer(parent, 1f / tileSize, batch)
        val matrix = Matrix4(camera.combined).translate(x, y, 0f)
        renderer.setView(matrix, 0f, 0f, layer.width.toFloat(), layer.height.toFloat())
        batch.use {
            renderer.renderTileLayer(layer)
        }
        fontbatch.use {
            for (xs in 0 until layer.width) {
                for (ys in 0 until layer.height) {
                    val pos = camera.project(Vector3(x + xs, y + ys, 0f))
                    bitmapFont.draw(it, "$xs, $ys", pos.x, pos.y)
                }
            }
        }
    }

    companion object {
        private val fontbatch = SpriteBatch()
    }
}
