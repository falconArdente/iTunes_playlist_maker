package com.example.playlistmaker.search.model.domain

interface SendTrackToPlayerUseCase {
    fun sendToPlayer(trackToPlay:Track)
}