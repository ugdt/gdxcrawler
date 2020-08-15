package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.math.GridPoint2

/**
 * TODO
 *
 * @property chunkSize
 * @property level
 * @constructor
 * @author Andrew Bueide and Emery Tanghanwaye
 * @param tileSize
 */
class TileWorld(tileSize: Int, private val chunkSize: Int, private val level: Level) {
    val chunkMap: HashMap<GridPoint2, Chunk> = hashMapOf()

    fun getChunk(position: GridPoint2): Chunk {
        return chunkMap[position] ?: Chunk(level.generateChunk(position)).also { chunkMap[position] = it }
    }

    fun getChunksAround(position: GridPoint2, radius: Int = 1): List<Chunk> {
        return getRadius(position, radius).map { getChunk(it) }.toList()
    }

    /**
     * @param position
     * @param radius
     * @return a list of [GridPoint2] containing all of the coordinates surrounding the [position] (inclusive)
     */
    fun getRadius(position: GridPoint2, radius: Int = 1): List<GridPoint2>{
        val xs = (position.x - radius) .. (position.x + radius)
        val ys = (position.y - radius) .. (position.y + radius)
        return xs.zip(ys).map { GridPoint2(it.first, it.second) }
    }
}