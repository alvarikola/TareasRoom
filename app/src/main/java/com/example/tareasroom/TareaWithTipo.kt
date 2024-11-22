package com.example.tareasroom

import androidx.room.Embedded
import androidx.room.Relation

data class TareasWithTipo(
    @Embedded val tarea: Tarea,
    @Relation(
        parentColumn = "idTipoTareaOwner",
        entityColumn = "idTipoTarea"
    )
    val tiposTareas: List<TipoTarea>
)