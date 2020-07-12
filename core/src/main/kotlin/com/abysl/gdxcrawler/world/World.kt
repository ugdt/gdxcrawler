package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.utils.PixelPerfectOrthoTiledMapRenderer
import com.abysl.gdxcrawler.world.level.DesertLevel
import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.*

class World(private val tileSize: Int, private val chunkSideLength: Int) {
    private val tiledMap = TiledMap()
    private val baseLayer: TiledMapTileLayer
        get() = tiledMap.layers[0] as TiledMapTileLayer
    private val allChunks = ObjectMap<GridPoint2, Chunk>()
    private val level: Level

    init {
        level = DesertLevel()
        val tileLayer = TiledMapTileLayer(16384, 16384, 16, 16)
        tiledMap.layers.add(tileLayer)
        tiledMap.tileSets.addTileSet(level.tiledSet)
    }

    fun getRenderer(batch: Batch): BatchTiledMapRenderer {
        return PixelPerfectOrthoTiledMapRenderer(tiledMap, (1f / tileSize), batch)
    }

    fun generateChunksAround(pixelPosition: GridPoint2, radius: Int) {
        val chunks = findChunksAround(pixelPosition, radius)

        for (chunk in chunks) {
            placeChunk(chunk)
        }
    }

    private fun placeChunk(chunk: Chunk) {
        val chunkTiles = chunk.getTiles()

        for (x in 0 until chunkSideLength)
            for (y in 0 until chunkSideLength) {
                val tileMapX = x + chunk.tileMapPosition.x
                val tileMapY = y + chunk.tileMapPosition.y

                val cell = chunkTiles[GridPoint2(x, y)]

                baseLayer.setCell((tileMapX), (tileMapY), cell)
            }
    }

    private fun findChunksAround(position: GridPoint2, radius: Int): List<Chunk> {
        val centerChunkPosition = dividePosByChunkLength(dividePosByTileSize(position))
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
        val chunkX = chunkPosition.x * 16
        val chunkY = chunkPosition.y * 16

        return GridPoint2(chunkX, chunkY)
    }


    private fun dividePosByTileSize(pixelPosition: GridPoint2): GridPoint2 {
        val tileX = pixelPosition.x / tileSize
        val tileY = pixelPosition.y / tileSize

        return GridPoint2(tileX, tileY)
    }

    private fun dividePosByChunkLength(pixelPosition: GridPoint2): GridPoint2 {
        val tileX = pixelPosition.x / chunkSideLength
        val tileY = pixelPosition.y / chunkSideLength

        return GridPoint2(tileX, tileY)
    }
}