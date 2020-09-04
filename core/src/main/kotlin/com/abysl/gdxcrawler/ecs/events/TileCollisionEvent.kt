package com.abysl.gdxcrawler.ecs.events

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2

class TileCollisionEvent(
    val entityId: Int,
    val position: Vector2,
    val tile: Pair<GridPoint2, TiledMapTileLayer.Cell>,
) : Event()
