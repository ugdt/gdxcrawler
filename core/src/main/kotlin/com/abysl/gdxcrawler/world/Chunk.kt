package com.abysl.gdxcrawler.world

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import ktx.collections.*
import ktx.tiled.property


/**
 * @property position
 * @property size
 * @property tileMap
 * @property active determines whether or not the chunk is rendered and updated
 */
class Chunk(val position: GridPoint2, val size: Int, val tileMap: TiledMap, var active: Boolean = true) {
    /**
     * Updates the chunk state
     * @param delta
     */
    fun update(delta: Float){

    }

    fun toDrawables(){}
}
