package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.world.Chunk
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import ktx.tiled.property
import ktx.tiled.set

class C25DemoLevel : Level(TilesetPaths.TUTORIAL) {
    private val waterTileId = 151
    private val grassTile = 17

    override fun generateChunk(chunkPosition: GridPoint2, chunkSize: Int): Chunk {
        val layer = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))
        val layer2 = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))
        layer2.properties["depth"] = 1
        layer2.properties["collision"] = true
        var flowerX = chunkPosition.x
        var flowerY = chunkPosition.y

        for (x in 0 until chunkSize) {
            for (y in 0 until chunkSize) {
                val tileId = if (x == 0 && flowerX > 0) {
                    flowerX--
                    grassTile
                } else if (y == 0 && flowerY > 0) {
                    flowerY--
                    grassTile
                } else {
                    waterTileId
                }
                if (tileId == grassTile) {
                    layer2.setCell(x, y, TiledMapTileLayer.Cell().also { it.tile = tileSet.getTile(tileId) })
                } else {
                    layer.setCell(x, y, TiledMapTileLayer.Cell().also { it.tile = tileSet.getTile(tileId) })
                }
            }
        }

        val tileMap = TiledMap()
        tileMap.tileSets.addTileSet(tileSet)
        tileMap.layers.add(layer)
        tileMap.layers.add(layer2)

        return Chunk(chunkPosition, chunkSize, tileMap, false)
    }
}
