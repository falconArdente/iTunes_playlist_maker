package com.example.playlistmaker.media.model.repository

fun interface ShareTextRepository {
    fun execute(text: String)
}