package com.example.carmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    val userName : String ,
    val password : String ,
    val image : String ,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
)
