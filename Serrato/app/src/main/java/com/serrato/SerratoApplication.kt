package com.serrato

import android.app.Application
import com.serrato.dao.SerratoDatabase
import androidx.room.Room

class SerratoApplication : Application() {

    val database: SerratoDatabase by lazy {
        Room.databaseBuilder(this, SerratoDatabase::class.java, "serratodb")
            .build()
    }
}
