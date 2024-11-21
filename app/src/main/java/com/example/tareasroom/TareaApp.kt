package com.example.tareasroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun TareaApp(database: AppDatabase) {
    val taskDao = database.tareaDao()
    val scope = rememberCoroutineScope()

    var tareasWithTipo by remember { mutableStateOf(listOf<TareasWithTipo>()) }
    var tipos by remember { mutableStateOf(listOf<TipoTarea>()) }

    var newTareaName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newTituloTipo by remember { mutableStateOf("") }


    // Cargar tareas al iniciar
    LaunchedEffect(Unit) {
        tareasWithTipo = taskDao.getAllTareasAndTipos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        OutlinedTextField(
            value = newTituloTipo,
            onValueChange = { newTituloTipo = it },
            label = { Text("Tipo") },
            modifier = Modifier.fillMaxWidth()
        )
        // Botón para agregar tarea
        tipos.forEach { tipo ->
            Button(
                onClick = {
                    var tituloTipo = tipo.idTipoTarea

                    scope.launch(Dispatchers.IO) {
                        val newTipo = TipoTarea(tituloTipoTarea = newTituloTipo)
                        taskDao.insertTipoTarea(newTipo)

                        val newTask = Tarea(tituloTarea = newTareaName, descripcionTarea = "Tarea de mates", idTipoTareaOwner = tituloTipo)
                        taskDao.insertTarea(newTask)

                        tareasWithTipo = taskDao.getAllTareasAndTipos() // Actualizar la lista
                        newTareaName = "" // Limpiar el campo
                        newDescription = ""
                        newTituloTipo = ""
                    }
                }
            ) {
                Text("Añadir tarea")
            }
        }

        // Mostrar lista de tareas
        tareasWithTipo.forEach { tareaWithTipo ->
            Text(text = "${tareaWithTipo.tarea.idTarea} ${tareaWithTipo.tarea.tituloTarea} ${tareaWithTipo.tarea.idTipoTareaOwner}")
        }
    }
}