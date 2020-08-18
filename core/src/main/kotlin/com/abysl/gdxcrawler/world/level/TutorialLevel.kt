package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.OpenSimplex2S
import com.abysl.gdxcrawler.utils.noise2XBeforeY
import com.abysl.gdxcrawler.world.Chunk
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import ktx.tiled.property

class TutorialLevel : Level(TilesetPaths.TUTORIAL) {
    private val openSimplex2S = OpenSimplex2S(69420)
    private val waterTileId = 151
    private val sandTileId = 243
    private val flowerTile = 2

    override fun generateChunk(chunkPosition: GridPoint2, chunkSize: Int): Chunk {
        val layer = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))
        var flowerX = chunkPosition.x
        var flowerY = chunkPosition.y

        for (x in 0 until chunkSize) {
            for (y in 0 until chunkSize) {
                val noise = openSimplex2S.noise2XBeforeY(x * chunkPosition.x, y * chunkPosition.y)
                val tileId = if(x == 0 && flowerX > 0) {
                    flowerX--
                    flowerTile
                } else if (y == 0 && flowerY > 0) {
                    flowerY--
                    flowerTile
                } else {
                    waterTileId
                }
                if (tileId != waterTileId && tileId != flowerTile){
                    println("Shits bugged yo")
                }
                layer.setCell(x, y, TiledMapTileLayer.Cell().also { it.tile = tileSet.getTile(tileId) })
            }
        }

        val tileMap = TiledMap()
        tileMap.tileSets.addTileSet(tileSet)
        tileMap.layers.add(layer)

        return Chunk(chunkPosition, chunkSize, tileMap, false)
    }
}