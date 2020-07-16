package com.abysl.gdxcrawler.world.level

import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell as Cell
import com.badlogic.gdx.math.GridPoint2
import ktx.assets.async.*

abstract class Level {
    companion object {
        val assetStorage = AssetStorage()
    }

    abstract val tiledSet: TiledMapTileSet
    abstract fun generateCell(mapPosition: GridPoint2): Cell
}