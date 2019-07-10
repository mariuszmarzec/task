package com.marzec.common

import android.text.Editable
import android.text.TextWatcher

abstract class OnTextChangeListener : TextWatcher {

    override fun afterTextChanged(editable: Editable?) = Unit

    override fun beforeTextChanged(editable: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(editable: CharSequence?, start: Int, before: Int, count: Int) = Unit
}