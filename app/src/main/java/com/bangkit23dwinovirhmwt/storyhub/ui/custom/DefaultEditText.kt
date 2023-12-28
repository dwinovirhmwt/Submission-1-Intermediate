package com.bangkit23dwinovirhmwt.storyhub.ui.custom

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.bangkit23dwinovirhmwt.storyhub.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DefaultEditText : TextInputLayout {

    private lateinit var editText: TextInputEditText

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        editText = TextInputEditText(this.context)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        addView(editText)

        // Disable hint animation
        isHintEnabled = false

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (s.toString()
                        .isEmpty()
                ) context?.getString(R.string.error_field_required) else null
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}