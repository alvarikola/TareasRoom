package com.example.tareasroom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


@Dao
interface TareaDao {

    @Insert
    suspend fun insertTarea(tarea: Tarea)

    @Update
    suspend fun update(tarea: Tarea)

    @Delete
    suspend fun delete(tarea: Tarea)

    @Insert
    suspend fun insertTipoTarea(tipoTarea: TipoTarea)

    @Transaction
    suspend fun insertTareaConTipo(tarea: Tarea, tipoTarea: TipoTarea) {
        insertTipoTarea(tipoTarea)  // Primero insertamos el TipoTarea
        insertTarea(tarea)  // Luego insertamos la Tarea
    }


    @Query("SELECT * FROM TiposTareas")
    suspend fun getAllTipos(): List<TipoTarea>


    @Query("SELECT * FROM Tareas")
    suspend fun getAllTareasAndTipos(): List<TareasWithTipo>
}