package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.*
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell as Cell

class Chunk(private val sideLength: Int, private val level: Level, val tileMapPosition: GridPoint2) {
    private val tiles: ObjectMap<GridPoint2, Cell> = ObjectMap(sideLength * sideLength)

    fun getTiles(): ObjectMap<GridPoint2, Cell> {
        for (x in 0 until sideLength) {
            for (y in 0 until sideLength) {
                val relativeTilePosition = GridPoint2(x, y)

                val mapX = x + tileMapPosition.x
                val mapY = y + tileMapPosition.y

                val mapPosition = GridPoint2(mapX, mapY)

                if (! tiles.containsKey(relativeTilePosition)) {
                    tiles[relativeTilePosition] = level.generateCell(mapPosition)
                }
            }
        }

        return tiles
    }
}
