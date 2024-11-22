package com.example.tareasroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tareasroom.data.AppDatabase
import com.example.tareasroom.view.TareaApp

class MainActivity : ComponentActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)

        setContent{
            TareaApp(database)
        }
    }
}