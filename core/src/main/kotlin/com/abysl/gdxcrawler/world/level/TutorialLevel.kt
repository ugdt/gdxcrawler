package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.OpenSimplex2S
import com.abysl.gdxcrawler.utils.noise2XBeforeY
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import ktx.tiled.property

class TutorialLevel(chunkSize: Int) : Level(Tilesets.TUTORIAL, chunkSize) {
    private val openSimplex2S = OpenSimplex2S(69420)
    private val waterTileId = 152
    private val sandTileId = 244


    override fun generateChunk(chunkPosition: GridPoint2): TiledMap {
        val layer = TiledMapTileLayer(chunkSize, chunkSize, tileSet.property("tilewidth"), tileSet.property("tileheight"))
        for(x in 0 until chunkSize){
            for(y in 0 until chunkSize){
                val noise = openSimplex2S.noise2XBeforeY(x * chunkPosition.x, y * chunkPosition.y)
                val tileId = if (noise > 0) sandTileId else waterTileId
                layer.getCell(x, y).tile = tileSet.getTile(tileId)
            }
        }
        val chunk = TiledMap()
        chunk.tileSets.addTileSet(tileSet)
        chunk.layers.add(layer)
        return chunk
    }
}