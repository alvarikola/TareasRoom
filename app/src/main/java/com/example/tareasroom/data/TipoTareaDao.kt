package com.example.tareasroom.data

import androidx.room.Insert
import androidx.room.Query

interface TipoTareaDao {



    @Query("SELECT * FROM TiposTareas")
    suspend fun getAllTipos(): List<TipoTarea>

    companion object {
        @Insert
        fun insertTipoTarea(tipoTarea: TipoTarea) {}
    }
}