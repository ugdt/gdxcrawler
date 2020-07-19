package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.noise2
import com.abysl.gdxcrawler.utils.OpenSimplex2S
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.GridPoint2

class DungeonLevel : Level() {
    private val openSimplex2S = OpenSimplex2S(1337)
    override val tileSetPath = "levels/dungeonlevel/dungeonlevel.tsx"

    var dungeons = ArrayList<Dungeon>();

    private val waterTileId = 151 // 16 * 9 + 7
    private val sandTileId = 243 // 16 * 15 + 3

    override fun generateCell(mapPosition: GridPoint2): TiledMapTileLayer.Cell {
        val noise = openSimplex2S.noise2(mapPosition)
        val tileId = if (noise > 0) sandTileId else waterTileId

        val cell = TiledMapTileLayer.Cell()
        cell.tile = tiledSet.getTile(tileId)

        return cell
    }

    private fun generateDungeons (numberOfDungeons: Int): ArrayList<Dungeon>{
        val generatedDungeons = ArrayList<Dungeon>()

        for (i in 1..numberOfDungeons){
            val new = Dungeon()
            new.generateFloors(1) //this should eventually not be HARDCODED
            generatedDungeons.add(new)
        }
        return generatedDungeons
    }
}