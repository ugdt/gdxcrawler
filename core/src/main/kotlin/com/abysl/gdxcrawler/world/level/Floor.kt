package com.abysl.gdxcrawler.world.level

import com.badlogic.gdx.math.GridPoint2
import java.util.*;
import kotlin.random.Random;

class Floor (var zLevel: Int){      //floors have a z level to keep track of what we're rendering at any time
    var rooms = ArrayList<Room>()

    fun generateRooms (numberOfRooms: Int): ArrayList<Room>{
        var generatedRooms = ArrayList<Room>()

        var firstRoom = Room(GridPoint2(0,0), GridPoint2(0,0), intArrayOf(0,0,0,0), GridPoint2(0,0))
        generatedRooms.add(firstRoom)

        for (i in 2..numberOfRooms)
        {
            generatedRooms.add(newRoom(generatedRooms))
        }
        return generatedRooms
    }

    private fun newRoom (generatedRooms: ArrayList<Room>): Room{
        val indexOfList = Random.nextInt(0,generatedRooms.size)
        val indexOfNeighbors = Random.nextInt(0,4)

        if (generatedRooms[indexOfList].neighbors[indexOfNeighbors] == 0)
        {
            val r = generatedRooms[indexOfList]
            val randomHeight = Random.nextInt(12,17)
            val randomWidth = Random.nextInt(12,17)
            val offset1 = Random.nextInt(-3,4)  //for an offset that shifts what "angle" the room is at relative to its parent
            val offset2 = Random.nextInt(1,7)   //for an offset that shifts how close the room is relative to its parent-
                                                          // notably, it cannot be a distance of 0 from its parent

            when (indexOfNeighbors)
            {
                0-> {   //generate a Room to the North
                    generatedRooms[indexOfList].neighbors[0] = 1
                    return Room(GridPoint2(r.origin.x + offset1, r.origin.y + randomHeight + offset2), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(0, 1, 0, 0), GridPoint2(r.position.x, r.position.y + 1))
                    }
                1-> {   //generate a Room to the South
                    generatedRooms[indexOfList].neighbors[1] = 1
                    return Room(GridPoint2(r.origin.x + offset1, r.origin.y - randomHeight - offset2), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(1, 0, 0, 0), GridPoint2(r.position.x, r.position.y - 1))
                }
                2-> {   //generate a Room to the East
                    generatedRooms[indexOfList].neighbors[2] = 1
                    return Room(GridPoint2(r.origin.x + randomWidth + offset2, r.origin.y + offset1), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(0, 0, 0, 1), GridPoint2(r.position.x + 1, r.position.y))
                }
                3-> {   //generate a Room to the West
                    generatedRooms[indexOfList].neighbors[3] = 1
                    return Room(GridPoint2(r.origin.x - randomWidth - offset2, r.origin.y + offset1), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(0, 0, 1, 0), GridPoint2(r.position.x - 1, r.position.y + 1))
                }
            }
        }
        return newRoom(generatedRooms)
    }
}