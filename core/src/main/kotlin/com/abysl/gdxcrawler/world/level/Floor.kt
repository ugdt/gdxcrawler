package com.abysl.gdxcrawler.world.level

import com.badlogic.gdx.math.GridPoint2
import java.util.*;
import kotlin.random.Random;

class Floor (var zLevel: Int){      //floors have a z level to keep track of what we're rendering at any time
    var rooms = ArrayList<Room>()

    fun generateRooms (numberOfRooms: Int): ArrayList<Room>{
        var generatedRooms = ArrayList<Room>()

        var firstRoom = StairsRoom(GridPoint2(2048,2048), GridPoint2(10,10), intArrayOf(0,0,0,0), GridPoint2(0,0))    //this should be a subclass of Room
        /* eventually, with multiple dungeons being created, the firstRoom origin
        should be set to the origin of the dungeon/previous floor's entrance */
        generatedRooms.add(firstRoom)
        System.out.println("Adding the first room of the floor at " + firstRoom.origin.x + "," + firstRoom.origin.y +
        " with center " + firstRoom.center)

        for (i in 2..numberOfRooms)
        {
            generatedRooms.add(newRoom(generatedRooms))
        }
        System.out.println("new rooms created- returning a list of " + generatedRooms.size + " rooms")
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
                    System.out.println("creating new NORTH ROOM")
                    return Room(GridPoint2(r.origin.x + offset1, r.origin.y + randomHeight + offset2), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(0, 1, 0, 0), GridPoint2(r.position.x, r.position.y + 1))
                    }
                1-> {   //generate a Room to the South
                    generatedRooms[indexOfList].neighbors[1] = 1
                    System.out.println("creating new SOUTH ROOM")
                    return Room(GridPoint2(r.origin.x + offset1, r.origin.y - randomHeight - offset2), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(1, 0, 0, 0), GridPoint2(r.position.x, r.position.y - 1))
                }
                2-> {   //generate a Room to the East
                    generatedRooms[indexOfList].neighbors[2] = 1
                    System.out.println("creating new EAST ROOM")
                    return Room(GridPoint2(r.origin.x + randomWidth + offset2, r.origin.y + offset1), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(0, 0, 0, 1), GridPoint2(r.position.x + 1, r.position.y))
                }
                3-> {   //generate a Room to the West
                    generatedRooms[indexOfList].neighbors[3] = 1
                    System.out.println("creating new WEST ROOM")
                    return Room(GridPoint2(r.origin.x - randomWidth - offset2, r.origin.y + offset1), GridPoint2(randomWidth, randomHeight),
                            intArrayOf(0, 0, 1, 0), GridPoint2(r.position.x - 1, r.position.y + 1))
                }
            }
        }
        return newRoom(generatedRooms)
    }
}