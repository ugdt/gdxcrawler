package com.abysl.gdxcrawler.world

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.GridPoint2

/**
 * @property chunkPosition
 * @property size
 * @property tileMap
 * @property active determines whether or not the chunk is rendered and updated
 */
class Chunk(val chunkPosition: GridPoint2, val size: Int, val tileMap: TiledMap, var active: Boolean = true) {
    val worldPosition: GridPoint2
        get() = GridPoint2(chunkPosition.x * size, chunkPosition.y * size)

    /**
     * Updates the chunk state
     * @param delta
     */
    fun update(delta: Float) {
        println(delta)
    }
}
