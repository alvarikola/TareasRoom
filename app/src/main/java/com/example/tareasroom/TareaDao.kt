package com.example.tareasroom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


@Dao
interface TareaDao : List<Tarea> {

    @Insert
    suspend fun insert(tarea: Tarea)

    @Update
    suspend fun update(tarea: Tarea)

    @Delete
    suspend fun delete(tarea: Tarea)

    @Transaction
    @Query("SELECT * FROM Tareas_database")
    suspend fun getAllTareasAndTipos(): List<Tarea>
}