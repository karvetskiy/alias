package dev.karvetskiy

import spark.Spark


    val rooms = ArrayList<Room>()

fun main(args: Array<String>) {

    var portNumber = 9999

    try{
        portNumber = args[0].toInt()
    }catch (e: Exception){
        e.printStackTrace()
    }
    //подключаемся к полученному порту
    Spark.port(portNumber)

    //Создаем комнату
    Spark.get("createRoom"){ _, _ ->
        val room = Room()
        isRoomIDExist(room.id, room)
        rooms.add(room)
        room.id
    }

    //Удаляем комнату
    Spark.get("destroyRoom"){r,_ ->
        val roomid = r.queryParams("roomid").toInt()
        val index = rooms.indexOf(rooms.find{it.id.toInt() == roomid})
        rooms.removeAt(index)
        "Room Destroyed"
    }

    //Добавляем игрока в комнату
    Spark.get("addUser"){r,_ ->
        val roomid = r.queryParams("roomid").toInt()
        val user = User()
        val room = rooms.find { it.id.toInt() == roomid } as Room
        isUserIDExist(user, room)
        room.users.add(user)
        1
    }

    //Удаляем игрока из комнаты
    Spark.get("deleteUser"){r,_ ->
        val roomid = r.queryParams("roomid").toInt()
        val userid = r.queryParams("userid").toInt()
        val room = rooms.find { it.id.toInt() == roomid } as Room
        room.users.remove(room.users.find { it.id.toInt()==userid })
        1
    }


    //Обработка некорректных запросов
    Spark.get("*"){_,_ ->
            "404 / Page not found"
    }
}

fun isRoomIDExist(id: Integer, room: Room){
    while (rooms.find{it.id == id}!=null){
        room.createRoomID()
    }
}

fun isUserIDExist(user: User, room: Room){
    while (room.users.find{it.id == user.id}!=null){
        user.createUserID()
    }
}


