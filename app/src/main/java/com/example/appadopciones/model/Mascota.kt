package com.example.appadopciones.model

data class Mascota(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: String,
    val urlImagen: String,
    val estaEnAdopcion: Boolean = true,
    val idUsuarioPublicador: Int
)