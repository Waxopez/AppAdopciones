package com.example.appadopciones.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appadopciones.R
import com.example.appadopciones.viewModel.ListaMascotasViewModel
import coil.compose.rememberAsyncImagePainter
import android.net.Uri
import com.example.appadopciones.repository.ID_USUARIO_ACTUAL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMascotasView(
    navController: NavController,
    viewModel: ListaMascotasViewModel = viewModel()
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val filtroActivo by viewModel.estaFiltradoPorUbicacion.collectAsState()

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
                title = {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_app),
                            contentDescription = "Logo App Adopciones",
                            modifier = Modifier.size(32.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AppAdopciones")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("publicar_mascota") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Poner en adopción") },
                text = { Text("Pone una mascota en adopción") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                if (filtroActivo) {
                    Button(onClick = { viewModel.quitarFiltro() }) {
                        Text("Quitar Filtro")
                    }
                } else {
                    BotonObtenerUbicacion { ubicacion ->
                        val esValido = ubicacion != null
                        viewModel.filtrarPorUbicacion(esValido)
                    }
                }
            }

            LazyColumn {
                items(mascotas, key = { it.id }) { mascota ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate("detalle_mascota/${mascota.id}") },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {

                            val esRecursoEstatico = mascota.urlImagen.startsWith("drawable://")

                            val pintorImagen = if (esRecursoEstatico) {
                                val nombreRecurso = mascota.urlImagen.substringAfter("drawable://")
                                painterResource(id = obtenerIdRecurso(nombreRecurso))
                            } else {
                                rememberAsyncImagePainter(model = Uri.parse(mascota.urlImagen))
                            }

                            Image(
                                painter = pintorImagen,
                                contentDescription = "Foto de ${mascota.nombre}",
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(mascota.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text("${mascota.especie} | ${mascota.edad}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Button(onClick = { navController.navigate("detalle_mascota/${mascota.id}") }) {
                                        Text("Ver Detalles")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))

                                    if (mascota.idUsuarioPublicador == ID_USUARIO_ACTUAL) {
                                        IconButton(
                                            onClick = { viewModel.eliminarMascota(mascota.id) }
                                        ) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "Eliminar mi publicación",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}