package com.abysl.gdxcrawler.rendering

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Matrix4
import ktx.graphics.use
import ktx.tiled.property

class DrawableLayer(private val camera: OrthographicCamera, private val parent: TiledMap, private val layer: TiledMapTileLayer, private val tileSize: Int) : Drawable {
    override var depth: Int = layer.property("depth", 0)

    override fun draw(batch: SpriteBatch, x: Float, y: Float, width: Float, height: Float) {
        val renderer = OrthogonalTiledMapRenderer(parent, 1f / tileSize, batch)
        val matrix = Matrix4(camera.combined).translate(x, y, 0f)
        renderer.setView(matrix, 0f, 0f, layer.width.toFloat(), layer.height.toFloat())
        batch.use {
            renderer.renderTileLayer(layer)
        }
    }
}
