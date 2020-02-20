package dk.itu.moapd.threadsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_threads.*

class ThreadFragment : Fragment() {

    companion object {
        private const val CONT = "CONT"
    }

    private var cont = 0
    private var status = false
    private val fragment = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_threads, container, false)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CONT, cont)
    }

    private inner class Task : Runnable {

        override fun run() {

            while (status) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                progress_bar?.progress = cont
                progress_text?.post{
                    if (fragment.isVisible)
                        progress_text.text = String.format(
                            getString(R.string.progress_text), cont)
                }

                cont++
                cont %= 100
            }
        }
    }

}
