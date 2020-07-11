package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.noise2
import com.abysl.gdxcrawler.utils.OpenSimplex2S
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.GridPoint2


class DesertLevel : Level() {
    private val openSimplex2S = OpenSimplex2S(1337)
    override val tiledSet: TiledMapTileSet = TiledMapTileSet()

    private val waterTileId = 151 // 16 * 9 + 7
    private val sandTileId = 247 // 16 * 15 + 3

    init {
        val tiles = Texture("levels/desertlevel/SB-LandTileset.png") // fetch sprite sheet from assets
        val splitTiles = TextureRegion.split(tiles, 16, 16) // chop up the sprite sheet

        var id = 0
        splitTiles.forEach { tileRow ->
            tileRow.forEach {  textureRegion ->
                val tile = StaticTiledMapTile(textureRegion)
                tiledSet.putTile(id++, tile)
            }
        }
    }

    override fun generateCell(mapPosition: GridPoint2): TiledMapTileLayer.Cell {
        val noise = openSimplex2S.noise2(mapPosition)
        val tileId = if (noise > 0) sandTileId else waterTileId

        val cell = TiledMapTileLayer.Cell()
        cell.tile = tiledSet.getTile(tileId)

        return cell
    }
}