package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.TileSetLoader
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell as Cell
import com.badlogic.gdx.math.GridPoint2
import ktx.assets.async.*

abstract class Level {
    companion object {
        val assetStorage = AssetStorage()
        init {
            assetStorage.setLoader(suffix = ".tsx") {
                TileSetLoader(assetStorage.fileResolver)
            }
        }
    }

    val tiledSet by lazy<TiledMapTileSet> { assetStorage.loadSync(tileSetPath) }

    abstract val tileSetPath: String
    abstract fun generateCell(mapPosition: GridPoint2): Cell
}