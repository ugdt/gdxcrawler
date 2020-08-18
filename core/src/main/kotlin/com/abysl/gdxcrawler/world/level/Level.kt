package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.TileSetLoader
import com.abysl.gdxcrawler.world.Chunk
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.math.GridPoint2
import ktx.assets.async.AssetStorage

abstract class Level protected constructor(tilesetPath: TilesetPaths) {
    companion object {
        val assetStorage = AssetStorage()

        init {
            assetStorage.setLoader(suffix = ".tsx") {
                TileSetLoader(assetStorage.fileResolver)
            }
        }
    }

    val tileSet by lazy<TiledMapTileSet> { assetStorage.loadSync(tilesetPath.filePath) }

    abstract fun generateChunk(chunkPosition: GridPoint2, chunkSize: Int): Chunk
}