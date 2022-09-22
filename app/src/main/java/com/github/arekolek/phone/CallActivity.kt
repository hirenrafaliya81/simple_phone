package com.github.arekolek.phone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer

class CallActivity : AppCompatActivity() {

    private lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        number = intent?.data?.schemeSpecificPart ?: "Number not found"
    }

    override fun onStart() {
        super.onStart()

        findViewById<Button>(R.id.answer).setOnClickListener {
            OngoingCall.answer()
        }

        findViewById<Button>(R.id.hangup).setOnClickListener {
            OngoingCall.hangup()
        }

        OngoingCall.state.observe(this, Observer {
            updateUi(it)
        })

    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {

        if (state == Call.STATE_DISCONNECTED) {
            finish()
            return
        }

        findViewById<TextView>(R.id.callInfo).text =
            "${state.asString().toLowerCase().capitalize()}\n$number"

        findViewById<Button>(R.id.answer).isVisible = state == Call.STATE_RINGING
        findViewById<Button>(R.id.hangup).isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
    }

    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }
    }
}
