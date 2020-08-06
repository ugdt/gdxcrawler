package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.noise2
import com.abysl.gdxcrawler.utils.OpenSimplex2S
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2

class DungeonLevel : Level() {
    private val openSimplex2S = OpenSimplex2S(1337)
    override val tileSetPath = TileSetAssetEnum.DUNGEON.filePath

    var dungeons = ArrayList<Dungeon>();

    private val plainDirtTileId = 73
    private val dirt1TileId = 77
    private val dirt2TileId = 76
    private val dirt3TileId = 74
    private val dirt4TileId = 78
    private val dirt5TileId = 75
    private val stairsId = 13
    private val wallsId = 1
    private val floorId = 65

    init {
        System.out.println("KIll Meee")
        this.dungeons = generateDungeons(1)
    }

    override fun generateCell(mapPosition: GridPoint2): TiledMapTileLayer.Cell {
        val noise = openSimplex2S.noise2(mapPosition)
        //val tileId = if (noise > 0) dirt1TileId else plainDirtTileId
        var tileId: Int

        if (noise < .25){
            tileId = plainDirtTileId
        }else if (noise < .40){
            tileId = dirt1TileId
        }else if (noise < .65){
            tileId = dirt2TileId
        }else if (noise < .80){
            tileId = dirt3TileId
        }else if (noise < .90){
            tileId = dirt4TileId
        }else{
            tileId = dirt5TileId
        }

        for (d in dungeons){
            for (f in d.floors){
                for (r in f.rooms){
                    if (r.walls.contains(Vector2(mapPosition.x.toFloat(), mapPosition.y.toFloat()))){
                        tileId = wallsId
                    }
                    if (r.inside.contains(Vector2(mapPosition.x.toFloat(), mapPosition.y.toFloat()))){
                        tileId = floorId
                    }
                    if (mapPosition == r.center && r is StairsRoom){
                        System.out.println("hey, trying to change the center tile...")
                        tileId = stairsId
                    }
                }
            }
        }

        val cell = TiledMapTileLayer.Cell()
        cell.tile = tiledSet.getTile(tileId)

        return cell
    }

    private fun generateDungeons (numberOfDungeons: Int): ArrayList<Dungeon>{
        val generatedDungeons = ArrayList<Dungeon>()

        for (i in 1..numberOfDungeons){
            val new = Dungeon()
            System.out.println("new DUNGEON created")
            new.floors = new.generateFloors(1) //this should eventually not be HARDCODED
            generatedDungeons.add(new)
        }
        System.out.println("new dungeons created- returning a list of " + generatedDungeons.size + " dungeons")
        return generatedDungeons
    }
}