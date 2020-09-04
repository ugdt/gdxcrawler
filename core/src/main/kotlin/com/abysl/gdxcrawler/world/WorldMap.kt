package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.utils.TileConstants
import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import kotlin.math.floor

/**
 * @property chunkSize size of a chunk in tiles
 * @property level
 * @constructor creates a Tile World
 * @author Andrew Bueide and Emery Tanghanwaye
 * @param chunkSize
 */
class WorldMap(private val chunkSize: Int, private val level: Level) {
    private val chunkMap: HashMap<GridPoint2, Chunk> = hashMapOf()

    /**
     * Generates the chunk using [Level.generateChunk] if not found in the [chunkMap]
     * @param position
     * @return the chunk at the given [position]
     */
    private fun getChunk(position: GridPoint2): Chunk {
        return chunkMap[position] ?: level.generateChunk(position, chunkSize).also { chunkMap[position] = it }
    }

    /**
     * @param position
     * @param radius
     * @return a list of chunks surrounding the given [Chunk] position (including the center [Chunk])
     */
    fun getChunksAround(position: GridPoint2, radius: Int = 1): List<Chunk> {
        return getPointsAround(position, radius).map { getChunk(it) }
    }

    /**
     * @return a list of active [Chunk]s
     */
    fun getActiveChunks(): List<Chunk> {
        return chunkMap.values.filter { it.active }
    }

    /**
     * Activates the given chunks, deactivates all other chunks.
     * @param chunks
     */
    fun setActiveChunks(chunks: List<Chunk>) {
        val positions = chunks.map { it.chunkPosition }
        for (chunk in chunkMap.values) {
            chunk.active = chunk.chunkPosition in positions
        }
    }

    /**
     * converts world position to chunk position
     * @param worldPosition
     * @return chunk's coordinates
     */
    fun worldToChunk(worldPosition: GridPoint2): GridPoint2 {
        val size = chunkSize.toFloat()
        return GridPoint2(floor(worldPosition.x / size).toInt(), floor(worldPosition.y / size).toInt())
    }

    /**
     * @param center
     * @param radius
     * @return a list of [GridPoint2] containing all of the coordinates surrounding the [center] (including the center)
     */
    private fun getPointsAround(center: GridPoint2, radius: Int = 1): List<GridPoint2> {
        val xRange = (center.x - radius)..(center.x + radius)
        val yRange = (center.y - radius)..(center.y + radius)
        return xRange.flatMap { x -> yRange.map { y -> GridPoint2(x, y) } }
    }

    fun collides(points: List<GridPoint2>): List<GridPoint2> {
        val collidingTiles = mutableListOf<GridPoint2>()

        points.forEach { point ->
            val chunk = getChunk(worldToChunk(point))
            val relativePosition: GridPoint2 = chunk.worldToRelative(point)
                    ?: error("ERROR: Relative position outside of chunk boundaries!")
            val collisionLayers: List<TiledMapTileLayer> =
                    chunk.tileMap.layers.filter {
                        it.properties[TileConstants.COLLISION] == true
                    }.map {
                        it as TiledMapTileLayer
                    }

            val collides = collisionLayers.any {
                it.getCell(relativePosition.x, relativePosition.y) != null
            }

            if (collides) {
                collidingTiles.add(point)
            }
        }

        return collidingTiles
    }

    fun collides(vararg vectors: Vector2): List<GridPoint2> {
        val gridPoints = vectors.map {
            val x: Int = if (it.x >= 0) it.x.toInt() else it.x.toInt() - 1
            val y: Int = if (it.y >= 0) it.y.toInt() else it.y.toInt() - 1
            GridPoint2(x, y)
        }
        return collides(gridPoints)
    }
}
