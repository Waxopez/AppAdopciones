package com.example.appadopciones.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appadopciones.R
import com.example.appadopciones.viewModel.DetalleMascotaViewModel
import coil.compose.rememberAsyncImagePainter
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMascotaView(
    navController: NavController,
    mascotaId: Int,
    viewModel: DetalleMascotaViewModel = viewModel()
) {
    androidx.compose.runtime.LaunchedEffect(mascotaId) {
        viewModel.cargarMascota(mascotaId)
    }

    val mascota by viewModel.mascotaDetalle.collectAsState()
    val estaCargando by viewModel.estaCargando.collectAsState()

    val obtenerIdRecurso: (String) -> Int = { nombreRecurso ->
        when (nombreRecurso) {
            "perro" -> R.drawable.perro
            "gato" -> R.drawable.gato
            else -> R.drawable.logo_app
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mascota?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (estaCargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                return@Column
            }

            val mascotaActual = mascota
            if (mascotaActual != null) {

                val esRecursoEstatico = mascotaActual.urlImagen.startsWith("drawable://")

                val pintorImagen = if (esRecursoEstatico) {
                    val nombreRecurso = mascotaActual.urlImagen.substringAfter("drawable://")
                    painterResource(id = obtenerIdRecurso(nombreRecurso))
                } else {
                    rememberAsyncImagePainter(model = Uri.parse(mascotaActual.urlImagen))
                }

                Card(
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        painter = pintorImagen,
                        contentDescription = "Foto de ${mascotaActual.nombre}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = mascotaActual.nombre,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Divider()

                DetalleFila(etiqueta = "Especie", valor = mascotaActual.especie)
                DetalleFila(etiqueta = "Edad", valor = mascotaActual.edad)

                DetalleFila(etiqueta = "Descripción", valor = "Un animal cariñoso, lleno de energía y listo para un nuevo hogar. Ideal para familias con niños.")

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { /* Lógica para iniciar proceso de adopción */ }) {
                    Text("¡Quiero Adoptar a ${mascotaActual.nombre}!")
                }

            } else {
                Text("Mascota no encontrada.", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun DetalleFila(etiqueta: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(etiqueta, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(valor, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
    }
}