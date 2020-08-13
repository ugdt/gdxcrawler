package com.abysl.gdxcrawler.world.level

import com.badlogic.gdx.math.GridPoint2
import java.util.*;
import kotlin.collections.ArrayList
import kotlin.random.Random;

class Floor (var zLevel: Int){      //floors have a z level to keep track of what we're rendering at any time
    var rooms = ArrayList<Room>()
    var hallways = ArrayList<Hallway>()
    var gridsize = 20

    fun generateRooms (numberOfRooms: Int): ArrayList<Room>{
        //var generatedRooms = ArrayList<Room>()

        var firstRoom = StairsRoom(GridPoint2(2048,2048), GridPoint2(10,10), intArrayOf(0,0,0,0), GridPoint2(0,0))    //this should be a subclass of Room
        /* eventually, with multiple dungeons being created, the firstRoom origin
        should be set to the origin of the dungeon/previous floor's entrance */
        rooms.add(firstRoom)
        System.out.println("Adding the first room of the floor at " + firstRoom.origin.x + "," + firstRoom.origin.y +
        " with center " + firstRoom.center)

        for (i in 2..numberOfRooms)
        {
            rooms.add(newRoom(rooms))
        }
        System.out.println("new rooms created- returning a list of " + rooms.size + " rooms")
        return rooms
    }

    private fun newRoom (currentRooms: ArrayList<Room>): Room{
        val indexOfList = Random.nextInt(0,currentRooms.size)
        val indexOfNeighbors = Random.nextInt(0,4)

        if (currentRooms[indexOfList].neighbors[indexOfNeighbors] == 0)
        {
            val r = currentRooms[indexOfList]
            val randomHeight = Random.nextInt(12,17)
            val randomWidth = Random.nextInt(12,17)
            val offset1 = Random.nextInt(-3,4)  //for an offset that shifts what "angle" the room is at relative to its parent
            //val offset2 = Random.nextInt(1,7)   //for an offset that shifts how close the room is relative to its parent-
                                                          // notably, it cannot be a distance of 0 from its parent
                                                            //may not need offset2 anymore...

            //make variables for values that we're going to be using often
            val x = r.origin.x
            val y = r.origin.y
            val g = gridsize
            val p = r.position.x
            val q = r.position.y

            when (indexOfNeighbors)
            {
                0-> {   //generate a Room to the North
                    System.out.println("creating new NORTH ROOM at position " + p + "," + (q+1))

                    notifyNeighbors(GridPoint2(p, q+1))

                    val newOrigin = GridPoint2(x + offset1, y + g + offset1)  //this calculates the origin
                    return Room(newOrigin, GridPoint2(randomWidth, randomHeight),
                            checkForNeighbors(GridPoint2(p, q+1)), GridPoint2(r.position.x, r.position.y + 1))
                    }
                1-> {   //generate a Room to the South
                    System.out.println("creating new SOUTH ROOM at position " + p + "," + (q-1))

                    notifyNeighbors(GridPoint2(p, q-1))

                    val newOrigin = GridPoint2(x + offset1, y - g + offset1)  //this calculates the origin
                    return Room(newOrigin, GridPoint2(randomWidth, randomHeight),
                            checkForNeighbors(GridPoint2(p, q-1)), GridPoint2(r.position.x, r.position.y - 1))
                }
                2-> {   //generate a Room to the East
                    System.out.println("creating new EAST ROOM at position " + (p+1) + "," + q)

                    notifyNeighbors(GridPoint2(p+1, q))

                    val newOrigin = GridPoint2(x + g + offset1, y + offset1)  //this calculates the origin
                    return Room(newOrigin, GridPoint2(randomWidth, randomHeight),
                            checkForNeighbors(GridPoint2(p+1, q)), GridPoint2(r.position.x + 1, r.position.y))
                }
                3-> {   //generate a Room to the West
                    System.out.println("creating new WEST ROOM at position " + (p-1) + "," + q)

                    notifyNeighbors(GridPoint2(p-1, q))

                    val newOrigin = GridPoint2(x - g + offset1, y + offset1)  //this calculates the origin
                    return Room(newOrigin, GridPoint2(randomWidth, randomHeight),
                            checkForNeighbors(GridPoint2(p-1, q)), GridPoint2(r.position.x - 1, r.position.y))
                }
            }
        }
        return newRoom(currentRooms)
    }

    private fun notifyNeighbors (position: GridPoint2){      //takes a new room's position and notifies all adjacent rooms that they have a neighbor, to avoid overlapping rooms
        for (i in 0 until rooms.size){
            //System.out.println("Checking room " + i)
            if (rooms[i].position == GridPoint2(position.x, position.y + 1)){
                rooms[i].neighbors[1] = 1
            }
            if (rooms[i].position == GridPoint2(position.x, position.y - 1)){
                rooms[i].neighbors[0] = 1
            }
            if (rooms[i].position == GridPoint2(position.x + 1, position.y)){
                rooms[i].neighbors[3] = 1
            }
            if (rooms[i].position == GridPoint2(position.x - 1, position.y)){
                rooms[i].neighbors[2] = 1
            }
        }
    }

    private fun checkForNeighbors (position: GridPoint2): IntArray{
        var neighbors = intArrayOf(0,0,0,0)
        for (i in 0 until rooms.size){
            //System.out.println("Checking room " + i)
            if (rooms[i].position == GridPoint2(position.x, position.y + 1)){
                neighbors[0] = 1
            }
            if (rooms[i].position == GridPoint2(position.x, position.y - 1)){
                neighbors[1] = 1
            }
            if (rooms[i].position == GridPoint2(position.x + 1, position.y)){
                neighbors[2] = 1
            }
            if (rooms[i].position == GridPoint2(position.x - 1, position.y)){
                neighbors[3] = 1
            }
        }
        return neighbors
    }
}