package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.GridPoint2
import ktx.collections.GdxArray
import ktx.tiled.property


/**
 * @property position
 * @property size
 * @property tileMap
 * @property active determines whether the chunk is rendered and updated
 */
class Chunk(val position: GridPoint2, val size: Int, val tileMap: TiledMap, var active: Boolean = false) {
    /**
     * Updates the chunk state
     * @param delta
     */
    fun update(delta: Float){

    }

    fun getMinDepth(): Int?{
        return tileMap.layers.map { (it as TiledMapTileLayer).property<Int>("depth") }.minBy { it }
    }

    fun getMaxDepth(): Int?{
        return tileMap.layers.map { (it as TiledMapTileLayer).property<Int>("depth") }.maxBy { it }
    }
}
