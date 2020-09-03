package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.TileConstants
import com.abysl.gdxcrawler.world.Chunk
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import ktx.tiled.property
import ktx.tiled.set

class C25DemoLevel : Level(TilesetPaths.TUTORIAL) {
    private val waterTileId = 151
    private val grassTile = 17
    private val hillId = 22

    override fun generateChunk(chunkPosition: GridPoint2, chunkSize: Int): Chunk {
        val layer = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))
        val layer2 = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))
        val layer3 = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))

        layer2.properties[TileConstants.DEPTH] = 1
        layer3.properties[TileConstants.DEPTH] = 0
        layer3.properties[TileConstants.COLLISION] = true

        for (x in 0 until chunkSize) {
            for (y in 0 until chunkSize) {
                if (!(x == 0 || y == 0 || x == chunkSize - 1 || y == chunkSize - 1)) {
                    layer.setCell(x, y, TiledMapTileLayer.Cell().also { it.tile = tileSet.getTile(waterTileId) })
                }
                if (x % 4 == 0 && y % 4 == 0) {
                    layer2.setCell(x, y, TiledMapTileLayer.Cell().also { it.tile = tileSet.getTile(grassTile) })
                }
                if (x % 4 == 2 && y % 4 == 2) {
                    layer3.setCell(x, y, TiledMapTileLayer.Cell().also { it.tile = tileSet.getTile(hillId) })
                }
            }
        }

        val tileMap = TiledMap()
        tileMap.tileSets.addTileSet(tileSet)
        tileMap.layers.add(layer)
        tileMap.layers.add(layer2)
        tileMap.layers.add(layer3)

        return Chunk(chunkPosition, chunkSize, tileMap, false)
    }
}
