package com.example.appadopciones.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appadopciones.repository.MascotaRepository
import com.example.appadopciones.model.PublicacionModel
import com.example.appadopciones.model.MensajeError

class PublicarViewModel : ViewModel() {
    private val repositorio = MascotaRepository

    var publicacion: PublicacionModel by mutableStateOf( PublicacionModel() )
        private set

    var mensajesError: MensajeError by mutableStateOf( repositorio.getMensajesErrorInicial() )
        private set

    var publicacionExitosa by mutableStateOf(false)

    var uriFotoSeleccionada by mutableStateOf<Uri?>(null)
        private set

    fun onUriFotoSeleccionada(uri: Uri?) {
        uriFotoSeleccionada = uri
    }

    fun onNombreMascotaChange(nombre: String) {
        publicacion = publicacion.copy(nombreMascota = nombre)
    }

    fun onEspecieChange(especie: String) {
        publicacion = publicacion.copy(especie = especie)
    }

    fun onEdadChange(edad: String) {
        publicacion = publicacion.copy(edad = edad)
    }

    fun onNombreContactoChange(nombre: String) {
        publicacion = publicacion.copy(nombreContacto = nombre)
    }

    fun onTelefonoContactoChange(telefono: String) {
        publicacion = publicacion.copy(telefonoContacto = telefono)
    }

    fun verificarNombreMascota(): Boolean {
        val esValido = repositorio.validacionNoVacio(publicacion.nombreMascota)
        mensajesError = if (!esValido) {
            mensajesError.copy(errorNombreMascota = "El nombre es obligatorio")
        } else {
            mensajesError.copy(errorNombreMascota = "")
        }
        return esValido
    }

    fun verificarEspecie(): Boolean {
        val esValido = repositorio.validacionNoVacio(publicacion.especie)
        mensajesError = if (!esValido) {
            mensajesError.copy(errorEspecie = "Selecciona una especie")
        } else {
            mensajesError.copy(errorEspecie = "")
        }
        return esValido
    }

    fun verificarEdad(): Boolean {
        val esValido = repositorio.validacionNoVacio(publicacion.edad)
        mensajesError = if (!esValido) {
            mensajesError.copy(errorEdad = "La edad es obligatoria")
        } else {
            mensajesError.copy(errorEdad = "")
        }
        return esValido
    }

    fun verificarFoto(): Boolean {
        return uriFotoSeleccionada != null
    }


    fun verificarTelefonoContacto(): Boolean {
        val esValido = repositorio.validacionTelefono(publicacion.telefonoContacto)
        mensajesError = if (!esValido) {
            mensajesError.copy(errorTelefonoContacto = "Teléfono inválido (8-12 dígitos)")
        } else {
            mensajesError.copy(errorTelefonoContacto = "")
        }
        return esValido
    }
    fun resetearEstado() {
        publicacionExitosa = false
        }
    fun publicarMascota() {
        val nombreValido = verificarNombreMascota()
        val especieValida = verificarEspecie()
        val edadValida = verificarEdad()
        val telefonoValido = verificarTelefonoContacto()
        val fotoValida = verificarFoto()

        if (nombreValido && especieValida && edadValida && telefonoValido && fotoValida) {
            repositorio.agregarMascota(publicacion, uriFotoSeleccionada)
            publicacionExitosa = true
            publicacion = PublicacionModel()
            mensajesError = MensajeError()
            uriFotoSeleccionada = null
            publicacionExitosa = true
        }
    }
}