package com.example.playlistmaker.media.model.domain.repository

import com.example.playlistmaker.media.model.domain.FavoriteTracksInteractor
import com.example.playlistmaker.search.model.domain.Track

class FavoriteTracksIteractorImpl(val repository: FavoriteTracksRepository): FavoriteTracksInteractor {
    override fun provideTracks(): List<Track> = repository.getTracks()
}