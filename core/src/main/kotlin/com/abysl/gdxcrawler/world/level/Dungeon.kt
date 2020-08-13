package com.abysl.gdxcrawler.world.level

import  kotlin.random.Random;

class Dungeon {
    var floors = ArrayList<Floor>()

    fun generateFloors (numberOfFloors: Int): ArrayList<Floor> {
        val generatedFloors = ArrayList<Floor>()

        for (i in 1..numberOfFloors){
            val new = Floor(i-1)        //the zLevel of the "highest/first" floor is 0, increasing as you descend
            System.out.println("new FLOOR created")
            new.rooms = new.generateRooms(8) //HARDCODED: this should eventually be changed to something like generateRooms(Random.nextInt(10,21))
            generatedFloors.add(new)
        }
        System.out.println("new floors created- returning a list of " + generatedFloors.size + " floors")
        for (i in 0 until generatedFloors[0].rooms.size){
            System.out.println("Room " + i + " at position " + generatedFloors[0].rooms[i].position.x + "," + generatedFloors[0].rooms[i].position.y +
                    " with neighbors " + generatedFloors[0].rooms[i].neighbors[0] + generatedFloors[0].rooms[i].neighbors[1] + generatedFloors[0].rooms[i].neighbors[2] + generatedFloors[0].rooms[i].neighbors[3] + ", ")
        }
        return generatedFloors
    }
}