package com.example.tareasroom

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "Tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val idTarea: Int,
    @ColumnInfo(name = "Título") val tituloTarea: String,
    @ColumnInfo(name = "Descripción") val descripcionTarea: String?,
    val idTipoTareaOwner: Int
)

@Entity(tableName = "TiposTareas")
data class TipoTarea(
    @PrimaryKey val idTipoTarea: Int,
    val tituloTipoTarea: String
)

data class TareasWithTipo(
    @Embedded val tarea: Tarea,
    @Relation(
        parentColumn = "idTipoTarea",
        entityColumn = "idTipoTareaOwner"
    )
    val tiposTareas: List<TipoTarea>
)