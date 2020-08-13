package com.abysl.gdxcrawler.world.level

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle;

open class Room (var origin: GridPoint2 = GridPoint2(0,0), var size: GridPoint2 = GridPoint2(0,0),
                 var neighbors: IntArray = intArrayOf(0,0,0,0), var position: GridPoint2 = GridPoint2(0,0)){    //neighbors = (N,S,E,W)

    var walls = Rectangle()
    var inside = Rectangle()
    var center = GridPoint2()

    init {
        walls = Rectangle(origin.x.toFloat(), origin.y.toFloat(), size.x.toFloat(), size.y.toFloat())
        inside = Rectangle(origin.x.toFloat()+1, origin.y.toFloat()+1, size.x.toFloat()-2, size.y.toFloat()-2) //1 smaller on all sides... adds 1 to x,y of origin because origin is at the bottom left
        center = GridPoint2((inside.x + (inside.width/2)).toInt(), (inside.y + (inside.height/2)).toInt())
    }
}