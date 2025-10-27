package com.example.appadopciones.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appadopciones.model.Mascota
import com.example.appadopciones.repository.MascotaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ListaMascotasViewModel : ViewModel() {
    private val repositorio = MascotaRepository

    private val _estaFiltradoPorUbicacion = MutableStateFlow(false)
    val estaFiltradoPorUbicacion: StateFlow<Boolean> = _estaFiltradoPorUbicacion

    val mascotas: StateFlow<List<Mascota>> = repositorio.listaMascotas
        .combine(_estaFiltradoPorUbicacion) { listaMascotas, estaFiltrado ->
            if (estaFiltrado) {
                listaMascotas.filter { it.especie.contains("Perro", ignoreCase = true) }
            } else {
                listaMascotas
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = repositorio.listaMascotas.value
        )

    fun filtrarPorUbicacion(ubicacionValida: Boolean) {
        _estaFiltradoPorUbicacion.value = ubicacionValida
    }

    fun quitarFiltro() {
        _estaFiltradoPorUbicacion.value = false
    }

    fun eliminarMascota(mascotaId: Int) {
        repositorio.eliminarMascota(mascotaId)
    }
}