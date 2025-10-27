package com.example.appadopciones.model

data class MensajeError(
    var errorNombreMascota: String = "",
    var errorEspecie: String = "",
    var errorEdad: String = "",
    var errorNombreContacto: String = "",
    var errorTelefonoContacto: String = ""
)