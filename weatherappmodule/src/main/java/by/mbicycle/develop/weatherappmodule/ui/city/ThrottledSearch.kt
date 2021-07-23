package by.mbicycle.develop.weatherappmodule.ui.city

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*
import kotlin.concurrent.timerTask

class ThrottledSearch(private val delegate: Delegate, private val milliseconds: Long) {
    private var timer: Timer? = null

    interface Delegate {
        fun onThrottledSearch(searchTerm: String)
    }

    private fun onTextChanged(charSequence: CharSequence?) {
        timer = Timer()
        timer?.schedule(timerTask {
            delegate.onThrottledSearch(charSequence.toString())
        }, milliseconds)
    }

    fun attachTo(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer?.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                onTextChanged(s)
            }
        })
    }
}