package com.github.arekolek.phone

import android.telecom.Call
import android.telecom.InCallService

class CallService : InCallService() {

    override fun onCallAdded(call: Call) {
        OngoingCall.call = call
        MainActivity.start(this, call)
    }

    override fun onCallRemoved(call: Call) {
        OngoingCall.call = null
    }
}