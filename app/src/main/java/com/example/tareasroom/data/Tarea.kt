package com.example.tareasroom.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val idTarea: Int = 0,
    @ColumnInfo(name = "Título") val tituloTarea: String,
    @ColumnInfo(name = "Descripción") val descripcionTarea: String?,
    val idTipoTareaOwner: Int
)