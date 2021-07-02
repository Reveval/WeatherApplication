package by.mbicycle.develop.weatherappmodule.ui.city

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*
import kotlin.concurrent.timerTask

class ThrottledSearch(
    delegate: Delegate,
    milliseconds: Long
) {
    interface Delegate {
        fun onThrottledSearch(searchTerm: String)
    }

    private val mDelegate = delegate
    private val mDelayMilliseconds = milliseconds
    private var mTimer: Timer? = null
    private var mSearchTerm = ""

    fun onTextChanged(charSequence: CharSequence?) {
        mTimer?.cancel()

        mTimer = Timer()
        mSearchTerm = charSequence.toString()

        mTimer?.schedule(timerTask {
            mDelegate.onThrottledSearch(mSearchTerm)
        }, mDelayMilliseconds)
    }

    fun attachTo(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}