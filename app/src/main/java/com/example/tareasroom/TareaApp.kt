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

    var tareas by remember { mutableStateOf(listOf<Tarea>()) }
    var newTareaName by remember { mutableStateOf("") }

    // Cargar tareas al iniciar
    LaunchedEffect(Unit) {
        tareas = taskDao.getAllTareasAndTipos()
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
            label = { Text("New Task") },
            modifier = Modifier.fillMaxWidth()
        )

        // Botón para agregar tarea
        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    val newTask = Tarea(tituloTarea = newTareaName)
                    taskDao.insert(newTask)
                    tasks = taskDao.getAllTasks() // Actualizar la lista
                    newTaskName = "" // Limpiar el campo
                }
            }
        ) {
            Text("Add Task")
        }

        // Mostrar lista de tareas
        tasks.forEach { task ->
            Text(text = task.name)
        }
    }
}