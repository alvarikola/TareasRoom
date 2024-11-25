package com.example.tareasroom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TipoTareaDao {



    @Query("SELECT * FROM TiposTareas")
    suspend fun getAllTipos(): List<TipoTarea>


    @Insert
    suspend fun insertTipoTarea(tipoTarea: TipoTarea)

}