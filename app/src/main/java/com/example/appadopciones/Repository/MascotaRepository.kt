package com.example.appadopciones.repository

import com.example.appadopciones.model.Mascota
import com.example.appadopciones.model.PublicacionModel
import com.example.appadopciones.model.MensajeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.net.Uri

const val ID_USUARIO_ACTUAL = 100

object MascotaRepository {
    private val _listaMascotas = MutableStateFlow(
        listOf(
            Mascota(1, "Bimbo", "Perro", "2 años", "drawable://perro", idUsuarioPublicador = 200),
            Mascota(2, "Luna", "Gato", "1 año", "drawable://gato", idUsuarioPublicador = 200)
        )
    )
    val listaMascotas: StateFlow<List<Mascota>> = _listaMascotas.asStateFlow()

    private var proximoId = 3

    fun obtenerMascotas(): List<Mascota> = listaMascotas.value

    fun agregarMascota(publicacion: PublicacionModel, uriFoto: Uri?) {

        val imagenUri = uriFoto?.toString() ?: "error_uri_nula"

        val nuevaMascota = Mascota(
            id = proximoId++,
            nombre = publicacion.nombreMascota,
            especie = publicacion.especie,
            edad = publicacion.edad,
            urlImagen = imagenUri,
            idUsuarioPublicador = ID_USUARIO_ACTUAL
        )
        _listaMascotas.value = _listaMascotas.value + nuevaMascota
    }

    fun eliminarMascota(mascotaId: Int) {
        _listaMascotas.value = _listaMascotas.value.filter { it.id != mascotaId }
    }

    fun getMensajesErrorInicial(): MensajeError = MensajeError()

    fun validacionNoVacio(campo: String): Boolean {
        return campo.isNotBlank()
    }

    fun validacionTelefono(telefono: String): Boolean {
        return telefono.matches(Regex("^[0-9]{8,12}\$"))
    }
}