package com.abysl.gdxcrawler.world.level

import com.abysl.gdxcrawler.utils.OpenSimplex2S
import com.abysl.gdxcrawler.utils.noise2XBeforeY
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2

class TutorialLevel : Level() {
    private val openSimplex2S = OpenSimplex2S(69420)
    override val tileSetPath = TileSetAssetEnum.TUTORIAL.filePath

    private val waterTileId = 152
    private val sandTileId = 244

    override fun generateCell(mapPosition: GridPoint2): TiledMapTileLayer.Cell {
        val noise = openSimplex2S.noise2XBeforeY(mapPosition)
        val tileId = if (noise > 0) sandTileId else waterTileId

        val cell = TiledMapTileLayer.Cell()
        cell.tile = tiledSet.getTile(tileId)

        return cell
    }
}