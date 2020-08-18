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

    override fun generateChunk(chunkPosition: GridPoint2, chunkSize: Int): Chunk {
        val layer = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))

        for (x in 0 until chunkSize) {
            for (y in 0 until chunkSize) {
                val noise = openSimplex2S.noise2XBeforeY(x + (chunkPosition.x * chunkSize), y + (chunkPosition.y * chunkSize))
                val tileId = if (noise > 0) sandTileId else waterTileId

                val cell = TiledMapTileLayer.Cell()
                cell.tile = tileSet.getTile(tileId)

                layer.setCell(x, y, cell)
            }
        }

        val tileMap = TiledMap()
        tileMap.tileSets.addTileSet(tileSet)
        tileMap.layers.add(layer)

        return Chunk(chunkPosition, chunkSize, tileMap, false)
    }
}