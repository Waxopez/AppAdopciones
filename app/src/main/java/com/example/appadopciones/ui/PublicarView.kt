package com.example.appadopciones.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appadopciones.viewModel.PublicarViewModel
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarView(
    viewModel: PublicarViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.publicacionExitosa) {
        if (viewModel.publicacionExitosa) {
            navController.popBackStack()
            viewModel.resetearEstado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Mascota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver a lista")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.uriFotoSeleccionada != null) {
                Image(
                    painter = rememberAsyncImagePainter(viewModel.uriFotoSeleccionada),
                    contentDescription = "Vista previa de la mascota",
                    modifier = Modifier.size(150.dp).padding(bottom = 8.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            BotonTomarFoto(
                onSuccess = { uri ->
                    viewModel.onUriFotoSeleccionada(uri)
                }
            )

            if (!viewModel.verificarFoto() && viewModel.publicacion.nombreMascota.isNotEmpty()) {
                Text(
                    text = "Debes tomar una foto de la mascota",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CampoPublicacion(
                valor = viewModel.publicacion.especie,
                etiqueta = "Especie (Ej: Perro)",
                errorMensaje = viewModel.mensajesError.errorEspecie,
                onValueChange = {
                    viewModel.onEspecieChange(it)
                    viewModel.verificarEspecie()
                }
            )

            CampoPublicacion(
                valor = viewModel.publicacion.edad,
                etiqueta = "Edad (Ej: 2 años)",
                errorMensaje = viewModel.mensajesError.errorEdad,
                onValueChange = {
                    viewModel.onEdadChange(it)
                    viewModel.verificarEdad()
                }
            )

            CampoPublicacion(
                valor = viewModel.publicacion.nombreMascota,
                etiqueta = "Nombre de la Mascota",
                errorMensaje = viewModel.mensajesError.errorNombreMascota,
                onValueChange = {
                    viewModel.onNombreMascotaChange(it)
                    viewModel.verificarNombreMascota()
                }
            )

            CampoPublicacion(
                valor = viewModel.publicacion.telefonoContacto,
                etiqueta = "Teléfono de Contacto",
                errorMensaje = viewModel.mensajesError.errorTelefonoContacto,
                onValueChange = {
                    viewModel.onTelefonoContactoChange(it)
                    viewModel.verificarTelefonoContacto()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { viewModel.publicarMascota() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar en Adopción")
            }

            AnimatedVisibility(
                visible = viewModel.publicacionExitosa,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier.padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Text("¡Publicación Exitosa!", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable
fun CampoPublicacion(
    valor: String,
    etiqueta: String,
    errorMensaje: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            label = { Text(etiqueta) },
            isError = errorMensaje.isNotEmpty(),
            trailingIcon = {
                if (errorMensaje.isNotEmpty()) {
                    Icon(Icons.Filled.Warning, "Error", tint = MaterialTheme.colorScheme.error)
                } else if (valor.isNotEmpty()) {
                    Icon(Icons.Filled.Done, "Válido", tint = MaterialTheme.colorScheme.primary)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMensaje.isNotEmpty()) {
            Text(
                text = errorMensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}