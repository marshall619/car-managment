package com.example.carmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Services(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String ,
    val carDestination : String ,
    val price : String,
    val date : String,
    val carId : Int
)
