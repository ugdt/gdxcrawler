package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.math.GridPoint2
import kotlin.math.floor

/**
 * @property chunkSize size of a chunk in tiles
 * @property level
 * @constructor creates a Tile World
 * @author Andrew Bueide and Emery Tanghanwaye
 * @param tileSize
 */
class TileWorld(private val chunkSize: Int, private val level: Level) {
    val chunkMap: HashMap<GridPoint2, Chunk> = hashMapOf()

    /**
     * Generates the chunk using [Level.generateChunkTileMap] if not found in the [chunkMap]
     * @param position
     * @return the chunk at the given [position]
     */
    fun getChunk(position: GridPoint2): Chunk {
        return chunkMap[position] ?: Chunk(position, chunkSize, level.generateChunkTileMap(position, chunkSize)).also { chunkMap[position] = it }
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
    fun setActiveChunks(chunks: List<Chunk>){
        val positions = chunks.map { it.position }
        for (chunk in chunkMap.values){
            chunk.active = chunk.position in positions
        }
    }

    fun worldToChunk(worldPosition: GridPoint2): GridPoint2{
        val size = chunkSize.toFloat()
        val res = GridPoint2(floor(worldPosition.x / size).toInt(), floor(worldPosition.y / size).toInt())
        return res
    }

    /**
     * @param center
     * @param radius
     * @return a list of [GridPoint2] containing all of the coordinates surrounding the [center] (including the center)
     */
    private fun getPointsAround(center: GridPoint2, radius: Int = 1): List<GridPoint2> {
        val xs = (center.x - radius)..(center.x + radius)
        val ys = (center.y - radius)..(center.y + radius)
        val res = xs.flatMap { x -> ys.map { y -> GridPoint2(x, y) } }
//        println(res.size)
//        res.forEach { println("${it.x}, ${it.y}") }
//        println("---------------")
        return res
    }
}