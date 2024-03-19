package com.example.playlistmaker.search.model.data.repository

import com.example.playlistmaker.search.model.domain.SendTrackToPlayerUseCase
import com.example.playlistmaker.search.model.domain.Track

class SendTrackToPlayerProvider(private val opener: TrackSender) : SendTrackToPlayerUseCase {
    override fun sendToPlayer(trackToPlay: Track) = opener.sendTrack(trackToSend = trackToPlay)
}