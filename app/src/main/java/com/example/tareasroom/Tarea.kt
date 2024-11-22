package com.example.tareasroom

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "Tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val idTarea: Int = 0,
    @ColumnInfo(name = "Título") val tituloTarea: String,
    @ColumnInfo(name = "Descripción") val descripcionTarea: String?,
    val idTipoTareaOwner: Int
)