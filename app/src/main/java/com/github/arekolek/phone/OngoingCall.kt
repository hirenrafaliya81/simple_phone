package com.github.arekolek.phone

import android.telecom.Call
import android.telecom.VideoProfile
import androidx.lifecycle.MutableLiveData

object OngoingCall {

    val state = MutableLiveData<Int>()

    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {
            state.postValue(newState)
        }
    }

    var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                it.registerCallback(callback)
                state.postValue(it.state)
            }
            field = value
        }

    fun answer() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun hangup() {
        call!!.disconnect()
    }
}
