package org.example.a2024_05_sopra_server

data class StudentBean(var name:String = "", var note : Int = 0)
data class UserBean(var login:String = "", var password : String = "", var sessionId:String = "")