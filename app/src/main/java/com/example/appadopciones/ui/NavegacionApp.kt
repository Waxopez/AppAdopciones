package com.example.appadopciones.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appadopciones.viewModel.DetalleMascotaViewModel
import com.example.appadopciones.viewModel.ListaMascotasViewModel
import com.example.appadopciones.viewModel.PublicarViewModel

@Composable
fun NavegacionApp() {
    val navController = rememberNavController()
    val listaMascotasViewModel: ListaMascotasViewModel = viewModel()
    val publicarViewModel: PublicarViewModel = viewModel()

    NavHost(navController = navController, startDestination = "lista_mascotas") {

        composable("lista_mascotas") {
            ListaMascotasView(navController = navController, viewModel = listaMascotasViewModel)
        }

        composable("detalle_mascota/{mascotaId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("mascotaId")?.toIntOrNull()

            if (id != null) {
                DetalleMascotaView(
                    navController = navController,
                    mascotaId = id,
                    viewModel = viewModel<DetalleMascotaViewModel>()
                )
            } else {
                Text("Error: Mascota ID no válida.")
            }
        }

        composable("publicar_mascota") {
            PublicarView(viewModel = publicarViewModel, navController = navController)
        }
    }
}