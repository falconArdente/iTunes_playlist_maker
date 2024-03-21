package com.example.playlistmaker.player.model.repository

import com.example.playlistmaker.player.model.domain.GetTrackToPlayUseCase
import com.example.playlistmaker.search.model.domain.Track

class GetTrackToPlayUseCaseImpl(private val repository: GetTrackToPlayRepository) :
    GetTrackToPlayUseCase {
    override fun getTrackToPlay(): Track = repository.getTrack()
}