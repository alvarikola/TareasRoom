package com.example.tareasroom

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FormularioTipos(dao: TareaDao) {
    var newTituloTipo by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    OutlinedTextField(
        value = newTituloTipo,
        onValueChange = { newTituloTipo = it },
        label = { Text("Tipo") },
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = {
            scope.launch(Dispatchers.IO) {
                val newTipo = TipoTarea(tituloTipoTarea = newTituloTipo)
                dao.insertTipoTarea(newTipo)
                newTituloTipo = ""
            }
        }
    ) {
        Text(text = "Añadir tipo")
    }
}

@Composable
fun FormularioTareas(dao: TareaDao) {
    var expandedDesplegable by remember { mutableStateOf(false) }

    var tiposList by remember { mutableStateOf(listOf<TipoTarea>()) }

    var newTareaName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newTipoTarea by remember { mutableIntStateOf(0) }
    var tipoSeleccionado by remember { mutableStateOf("-") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        tiposList = dao.getAllTipos()
    }

    // Campo de texto para agregar una nueva tarea
    OutlinedTextField(
        value = newTareaName,
        onValueChange = { newTareaName = it },
        label = { Text("Nueva Tarea") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = newDescription,
        onValueChange = { newDescription = it },
        label = { Text("Descripción") },
        modifier = Modifier.fillMaxWidth()
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = tipoSeleccionado,
            onValueChange = { tipoSeleccionado = it },
            readOnly = true,
            label = { Text("Tipo") },
            modifier = Modifier.weight(1f).padding(end = 5.dp)
        )
        TextButton(
            onClick = { expandedDesplegable = true }
        ) {
            Text("Seleccionar tipo")
        }
    }

    DropdownMenu(
        expanded = expandedDesplegable,
        onDismissRequest = { expandedDesplegable = false }
    ) {
        tiposList.forEach { tipo ->
            DropdownMenuItem(
                text = {
                    Text(tipo.tituloTipoTarea)
                },
                onClick = {
                    newTipoTarea = tipo.idTipoTarea
                    tipoSeleccionado = tipo.tituloTipoTarea
                    expandedDesplegable = false
                }
            )
        }
    }

    // Botón para agregar tarea
    Button(
        onClick = {
            scope.launch(Dispatchers.IO) {
                dao.insertTarea(Tarea(
                    tituloTarea = newTareaName,
                    descripcionTarea = newDescription,
                    idTipoTareaOwner = newTipoTarea
                ))

                newTareaName = "" // Limpiar el campo
                newDescription = ""
                newTipoTarea = 0
            }
        }
    ) {
        Text("Añadir tarea")
    }
}

@Composable
fun ListaTareas(dao: TareaDao) {
    var tareasWithTipo by remember { mutableStateOf(listOf<TareasWithTipo>()) }

    LaunchedEffect(Unit) {
        tareasWithTipo = dao.getAllTareasAndTipos()
    }

    // Mostrar lista de tareas
    tareasWithTipo.forEach { tareaWithTipo ->
        Text(text = "${tareaWithTipo.tarea.idTarea} ${tareaWithTipo.tarea.tituloTarea} ${tareaWithTipo.tarea.idTipoTareaOwner}")
    }
}

@Composable
fun TareaApp(database: AppDatabase) {
    val taskDao = database.tareaDao()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormularioTipos(taskDao)
        FormularioTareas(taskDao)
        ListaTareas(taskDao)
    }
}