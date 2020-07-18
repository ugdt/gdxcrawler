package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.utils.PixelPerfectOrthoTiledMapRenderer
import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.*

class TileWorld(private val tileSize: Int, private val chunkSideLength: Int, private val level: Level) {
    val tiledMap = TiledMap()
    private val baseLayer = TiledMapTileLayer(4096, 4096, tileSize, tileSize)
    private val allChunks = ObjectMap<GridPoint2, Chunk>()

    init {
        tiledMap.layers.add(baseLayer)
        tiledMap.tileSets.addTileSet(level.tiledSet)
    }


    fun getRenderer(batch: Batch): BatchTiledMapRenderer {
        return PixelPerfectOrthoTiledMapRenderer(tiledMap, 1f, batch)
    }

    fun generateChunksAround(pixelPosition: GridPoint2, radius: Int) {
        val chunks = findChunksAround(pixelPosition, radius)

        for (chunk in chunks) {
            placeChunk(chunk)
        }
    }

    private fun placeChunk(chunk: Chunk) {
        val chunkTiles = chunk.getTiles()

        for (x in 0 until chunkSideLength) {
            for (y in 0 until chunkSideLength) {
                val tileMapX = x + chunk.tileMapPosition.x
                val tileMapY = y + chunk.tileMapPosition.y

                val cell = chunkTiles[x][y]

                baseLayer.setCell((tileMapX), (tileMapY), cell)
            }
        }
    }

    private fun findChunksAround(pixelPosition: GridPoint2, radius: Int): List<Chunk> {
        val centerChunkPosition = dividePosByChunkLength(dividePosByTileSize(pixelPosition))
        val chunks = mutableListOf<Chunk>()

        for (x in (centerChunkPosition.x - radius)..(centerChunkPosition.x + radius))
            for (y in (centerChunkPosition.y - radius)..(centerChunkPosition.y + radius)) {
                val chunkPosition = GridPoint2(x, y)

                if (! allChunks.containsKey(chunkPosition)) {
                    allChunks[chunkPosition] = Chunk(chunkSideLength, level, multiplyPosByChunkLength(chunkPosition))
                }

                chunks.add(allChunks[chunkPosition])
            }

        return chunks.toList()
    }

    private fun multiplyPosByChunkLength(chunkPosition: GridPoint2): GridPoint2 {
        val chunkX = chunkPosition.x * chunkSideLength
        val chunkY = chunkPosition.y * chunkSideLength

        return GridPoint2(chunkX, chunkY)
    }


    private fun dividePosByTileSize(pixelPosition: GridPoint2): GridPoint2 {
        val tileX = pixelPosition.x / tileSize
        val tileY = pixelPosition.y / tileSize

        return GridPoint2(tileX, tileY)
    }

    private fun dividePosByChunkLength(tilePosition: GridPoint2): GridPoint2 {
        val chunkX = tilePosition.x / chunkSideLength
        val chunkY = tilePosition.y / chunkSideLength

        return GridPoint2(chunkX, chunkY)
    }
}