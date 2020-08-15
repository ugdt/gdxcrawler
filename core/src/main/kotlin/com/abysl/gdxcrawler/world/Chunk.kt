package com.abysl.gdxcrawler.world

import com.abysl.gdxcrawler.world.level.Level
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.GridPoint2
import ktx.collections.GdxArray

class Chunk(private val sideLength: Int, private val level: Level, val tileMapPosition: GridPoint2) {
    private val tiles: GdxArray<GdxArray<Cell>> = GdxArray(false, sideLength)

    init {
        for (i in 0 until sideLength) {
            tiles.add(GdxArray(false, sideLength))
        }

        generateTiles()
    }

    private fun generateTiles() {
        for (x in 0 until sideLength) {
            for (y in 0 until sideLength) {
                val mapX = x + tileMapPosition.x
                val mapY = y + tileMapPosition.y

                val mapPosition = GridPoint2(mapX, mapY)
                tiles[x].add(level.generateCell(mapPosition))
            }
        }
    }

    private fun tilesToList(tiles: GdxArray<GdxArray<Cell>>): List<List<Cell>> {
        val mutableTiles = mutableListOf<List<Cell>>()

        for (row in tiles) {
            mutableTiles.add(row.toList())
        }

        return mutableTiles.toList()
    }

    fun getTiles(): List<List<Cell>> {
        return tilesToList(tiles)
    }

    fun getTilemap(): TiledMap {
        return TiledMap()
    }
}
