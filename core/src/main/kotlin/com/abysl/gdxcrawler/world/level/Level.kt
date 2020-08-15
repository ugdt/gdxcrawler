package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.TileSetLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.math.GridPoint2
import ktx.assets.async.AssetStorage

abstract class Level(tileset: Tilesets, val chunkSize: Int) {
    companion object {
        val assetStorage = AssetStorage()

        init {
            assetStorage.setLoader(suffix = ".tsx") {
                TileSetLoader(assetStorage.fileResolver)
            }
        }
    }

    val tileSet by lazy<TiledMapTileSet> { assetStorage.loadSync(tileset.filePath) }

    abstract fun generateChunk(chunkPosition: GridPoint2): TiledMap
}