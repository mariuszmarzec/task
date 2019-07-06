package com.marzec.common

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

open class OnTextChangeInteractionListener(
        private val listener: OnTextChangeListener)
    : OnTextChangeListener(), View.OnTouchListener {

    private var touched = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        touched = true
        return false
    }

    override fun onAfterTextChange(text: String) {
        if (touched) {
            listener.onAfterTextChange(text)
            touched = !touched
        }
    }
}