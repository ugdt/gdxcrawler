package com.abysl.gdxcrawler.world.level

import com.badlogic.gdx.math.GridPoint2

class StairsRoom(origin: GridPoint2 = GridPoint2(0,0), size: GridPoint2 = GridPoint2(0,0),
                 neighbors: IntArray = intArrayOf(0,0,0,0), position: GridPoint2 = GridPoint2(0,0)) : Room(origin, size, neighbors, position) {

    //constructor(room: StairsRoom) : this(room.origin, room.size, room.neighbors, room.position)   copying using constructor
}