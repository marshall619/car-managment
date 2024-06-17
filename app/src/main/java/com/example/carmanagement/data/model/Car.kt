package com.example.carmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Car(
    val name : String ,
    val model : String ,
    val color : String,
    val userId : Int ,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
)
