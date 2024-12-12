package com.example.tareasroom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tareasroom.data.TipoTareaDao


@Dao
interface TareaDao {

    @Insert
    suspend fun insertTarea(tarea: Tarea)

    @Update
    suspend fun update(tarea: Tarea)

    @Delete
    suspend fun delete(tarea: Tarea)


    @Query("SELECT * FROM Tareas")
    suspend fun getAllTareasAndTipos(): List<TareasWithTipo>
}