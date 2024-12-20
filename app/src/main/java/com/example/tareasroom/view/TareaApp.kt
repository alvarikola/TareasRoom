package com.example.tareasroom.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tareasroom.data.AppDatabase
import com.example.tareasroom.data.TareaDao
import com.example.tareasroom.data.Tarea
import com.example.tareasroom.data.TareasWithTipo
import com.example.tareasroom.data.TipoTarea
import com.example.tareasroom.data.TipoTareaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FormularioTipos(tipoDao: TipoTareaDao, mostrar: Boolean) {
    var newTituloTipo by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoBorrar by remember { mutableStateOf(false) }

    var tipoSeleccionado by remember { mutableStateOf("-") }
    var expandedDesplegable by remember { mutableStateOf(false) }
    var tiposList by remember { mutableStateOf(listOf<TipoTarea>()) }
    var newTipoTarea by remember { mutableIntStateOf(0) }
    var actualizaEstado by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tiposList = tipoDao.getAllTipos()
    }
    if (mostrar) {
        OutlinedTextField(
            value = newTituloTipo,
            onValueChange = { newTituloTipo = it },
            label = { Text("Tipo") },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        val newTipo = TipoTarea(tituloTipoTarea = newTituloTipo)
                        tipoDao.insertTipoTarea(newTipo)
                        newTituloTipo = ""
                        actualizaEstado = true
                        tiposList = tipoDao.getAllTipos()

                    }
                }
            ) {
                Text(text = "Añadir tipo")
            }
            Button(
                onClick = {
                    mostrarDialogoEditar = true
                }
            ) {
                Text(text = "Editar tipo")
            }
            if (mostrarDialogoEditar) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoEditar = false },
                    title = { Text(text = "Editar") },
                    text = {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedTextField(
                                value = newTituloTipo,
                                onValueChange = { newTituloTipo = it },
                                label = { Text("Tipo") },
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
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch(Dispatchers.IO) {
                                val tipoActualizado = TipoTarea(idTipoTarea = newTipoTarea, tituloTipoTarea = newTituloTipo)
                                tipoDao.update(tipoActualizado)
                                newTituloTipo = ""
                                actualizaEstado = true
                                tiposList = tipoDao.getAllTipos()
                            }
                        }) {
                            Text("Actualizar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogoEditar = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
            Button(
                onClick = {
                    mostrarDialogoBorrar = true
                }
            ) {
                Text(text = "Borrar tipo")
            }
            if (mostrarDialogoBorrar) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoBorrar = false },
                    title = { Text(text = "Borrar") },
                    text = {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch(Dispatchers.IO) {
                                val tipoBorrar = TipoTarea(idTipoTarea = newTipoTarea, tituloTipoTarea = tipoSeleccionado)
                                tipoDao.delete(tipoBorrar)
                                actualizaEstado = true
                                tiposList = tipoDao.getAllTipos()
                            }
                        }) {
                            Text("Borrar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogoBorrar = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun FormularioTareas(taskDao: TareaDao, tipoDao: TipoTareaDao, mostrar: Boolean) {
    var expandedDesplegable by remember { mutableStateOf(false) }

    var tiposList by remember { mutableStateOf(listOf<TipoTarea>()) }
    var tareaList by remember { mutableStateOf(listOf<TareasWithTipo>()) }


    var newTareaName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newTipoTarea by remember { mutableIntStateOf(0) }
    var tipoSeleccionado by remember { mutableStateOf("-") }


    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        tiposList = tipoDao.getAllTipos()
        tareaList = taskDao.getAllTareasAndTipos()
    }

    if (mostrar) {
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
                scope.launch (Dispatchers.IO){
                    tiposList = tipoDao.getAllTipos()
                }
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
                    taskDao.insertTarea(
                        Tarea(
                            tituloTarea = newTareaName,
                            descripcionTarea = newDescription,
                            idTipoTareaOwner = newTipoTarea
                        )
                    )

                    newTareaName = "" // Limpiar el campo
                    newDescription = ""
                    newTipoTarea = 0

                }
            }
        ) {
            Text("Añadir tarea")
        }
    }
}

@Composable
fun ListaTareas(dao: TareaDao, tipoDao: TipoTareaDao) {

    var tareasWithTipo by remember { mutableStateOf(listOf<TareasWithTipo>()) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    var tiposList by remember { mutableStateOf(listOf<TipoTarea>()) }


    var expandedDesplegable by remember { mutableStateOf(false) }

    var newTareaName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf<String?>("") }
    var newTipoTarea by remember { mutableIntStateOf(0) }
    var tipoSeleccionado by remember { mutableStateOf("-") }
    var idTareaSeleccionada by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        tareasWithTipo = dao.getAllTareasAndTipos()
        tiposList = tipoDao.getAllTipos()
    }

    Column (
        modifier = Modifier
        .verticalScroll(rememberScrollState())
    ){
        Button(
            onClick = {
                scope.launch {
                    tareasWithTipo = dao.getAllTareasAndTipos()
                    tiposList = tipoDao.getAllTipos()
                }
            }
        ) {
            Text("Actualizar")
        }
        // Mostrar lista de tareas
        if (!mostrarDialogoEditar) {
            tareasWithTipo.forEach { tareaWithTipo ->
                Log.i("prueba", "Primer: ${tareaWithTipo.tarea.idTarea}")
                idTareaSeleccionada = tareaWithTipo.tarea.idTarea
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color(0xFFb7bbff)
                    )
                ){
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    ){
                        Text(text = "Tarea: ", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Tipo: ${tareaWithTipo.tiposTareas[0].tituloTipoTarea}", fontSize = 20.sp)
                        Text(text = "${tareaWithTipo.tarea.idTarea} ${tareaWithTipo.tarea.tituloTarea}", fontSize = 20.sp)
                        tareaWithTipo.tarea.descripcionTarea?.let { Text(text = it, fontSize = 20.sp) }
                    }
                    Column (
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f).padding(10.dp)
                    ){
                        Button(
                            onClick = {
                                idTareaSeleccionada = tareaWithTipo.tarea.idTarea
                                newTareaName = tareaWithTipo.tarea.tituloTarea
                                newDescription = tareaWithTipo.tarea.descripcionTarea
                                newTipoTarea = tareaWithTipo.tarea.idTipoTareaOwner
                                mostrarDialogoEditar = true
                                Log.i("prueba", "Segundo: ${tareaWithTipo.tarea.idTarea}")
                            }
                        ) {
                            Text(text = "Editar tarea")
                        }
                        Button(
                            onClick = {
                                scope.launch(Dispatchers.IO) {
                                    Log.i("prueba", "Antes de borrar${tareaWithTipo.tarea.idTarea}")
                                    dao.delete(
                                        Tarea(
                                            idTarea = tareaWithTipo.tarea.idTarea,
                                            tituloTarea = tareaWithTipo.tarea.tituloTarea,
                                            descripcionTarea = tareaWithTipo.tarea.descripcionTarea,
                                            idTipoTareaOwner = tareaWithTipo.tarea.idTipoTareaOwner
                                        )
                                    )
                                    tiposList = tipoDao.getAllTipos()
                                }
                            }
                        ) {
                            Text(text = "Borrar tarea")
                        }
                    }
                }
            }
        }
        else {
            AlertDialog(
                onDismissRequest = { mostrarDialogoEditar = false },
                title = { Text(text = "Editar") },
                text = {
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo de texto para agregar una nueva tarea
                        OutlinedTextField(
                            value = newTareaName,
                            onValueChange = { newTareaName = it },
                            label = { Text("Editar Tarea") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        newDescription?.let {
                            OutlinedTextField(
                                value = it,
                                onValueChange = { newDescription = it },
                                label = { Text("Descripción") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

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
                                scope.launch (Dispatchers.IO){
                                    tiposList = tipoDao.getAllTipos()
                                }
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
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch(Dispatchers.IO) {
                            Log.i("prueba", "Antes de editar${idTareaSeleccionada}")
                            dao.update(
                                Tarea(
                                    idTarea = idTareaSeleccionada,
                                    tituloTarea = newTareaName,
                                    descripcionTarea = newDescription,
                                    idTipoTareaOwner = newTipoTarea
                                )
                            )
                            tiposList = tipoDao.getAllTipos()
                            mostrarDialogoEditar = false
                        }
                    }) {
                        Text("Actualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoEditar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

}

@Composable
fun TareaApp(database: AppDatabase) {
    val taskDao = database.tareaDao()
    val tipoDao = database.tipoTareaDao()
    var tareasPorActualizar by remember { mutableStateOf(true) }
    var mostrarFormularioTipo by remember { mutableStateOf(false) }
    var mostrarFormularioTarea by remember { mutableStateOf(false) }



    if (tareasPorActualizar) {
        LaunchedEffect(tareasPorActualizar) {
            tareasPorActualizar = false // Resetear el flag después de actualizar
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gestor de tareas", fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
        Row {
            Button(
                onClick = { mostrarFormularioTipo = !mostrarFormularioTipo }
            ) {
                Text("Formulario para tipos")
            }
            Button(
                onClick = { mostrarFormularioTarea = !mostrarFormularioTarea }
            ) {
                Text("Formulario para tareas")
            }
        }
        FormularioTipos(tipoDao, mostrarFormularioTipo)
        FormularioTareas(taskDao, tipoDao, mostrarFormularioTarea)
        ListaTareas(taskDao, tipoDao)
    }
}