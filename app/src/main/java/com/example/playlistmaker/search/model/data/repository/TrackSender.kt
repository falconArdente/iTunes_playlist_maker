package com.example.playlistmaker.search.model.data.repository

import com.example.playlistmaker.search.model.domain.Track

interface TrackSender {
    fun sendTrack(trackToSend:Track)
}