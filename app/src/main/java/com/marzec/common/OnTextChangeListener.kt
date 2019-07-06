package com.marzec.common

import android.text.Editable
import android.text.TextWatcher

abstract class OnTextChangeListener : TextWatcher {

    abstract fun onAfterTextChange(text: String)

    final override fun afterTextChanged(editable: Editable?) {
        onAfterTextChange(editable?.toString().orEmpty())
    }

    final override fun beforeTextChanged(editable: CharSequence?, start: Int, before: Int, count: Int) = Unit

    final override fun onTextChanged(editable: CharSequence?, start: Int, before: Int, count: Int) = Unit
}