package dk.itu.moapd.threadsapp

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_threads.*

class HandlerFragment : Fragment() {

    companion object {
        private const val CONT = "CONT"
    }

    private var cont = 0
    private var status = false
    private val fragment = this

    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_threads, container, false)

        handler = Handler()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        reset_button.setOnClickListener {
            cont = 0
        }

        start_button.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                status = true
                Thread(Task()).start()
            } else
                status = false

            reset_button.isEnabled = isChecked
        }

        if (savedInstanceState != null)
            cont = savedInstanceState.getInt(CONT, 0)

        progress_text.text = String.format(
            getString(R.string.progress_text), cont)
    }

    private inner class Task : Runnable {

        override fun run() {

            while (status) {
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                handler.post {
                    if (fragment.isVisible)
                        progress_text?.text = String.format(
                            getString(R.string.progress_text), cont)
                    progress_bar?.progress = cont++
                    cont %= 100
                }
            }
        }
    }

}
