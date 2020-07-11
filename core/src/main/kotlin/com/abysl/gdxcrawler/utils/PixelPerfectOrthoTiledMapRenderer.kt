package com.abysl.gdxcrawler.utils

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile

class PixelPerfectOrthoTiledMapRenderer(map: TiledMap, unitScale: Float, batch: Batch) : OrthogonalTiledMapRenderer(map, unitScale, batch) {
    override fun render() {
        AnimatedTiledMapTile.updateAnimationBaseTime()

        for (layer in map.layers) {
            renderMapLayer(layer)
        }
    }
}