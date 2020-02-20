package dk.itu.moapd.threadsapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_threads.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskFragment : Fragment() {

    companion object {
        private const val CONT = "CONT"
    }

    private var cont = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_threads, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        start_button.textOff = getString(R.string.download_button)
        start_button.setOnClickListener {
            Task().execute("https://goo.gl/X9uSQ9")
            val button = it as ToggleButton
            button.isChecked = false
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

    @SuppressLint("StaticFieldLeak")
    private inner class Task : AsyncTask<String, Int, Bitmap>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progress_bar.progress = 0

            progress_text.text = String.format(
                getString(R.string.progress_text), cont)

            image_view.setImageBitmap(null)
        }

        override fun doInBackground(vararg url: String?): Bitmap? {
            var bitmap: Bitmap? = null
            val inputStream: InputStream?

            try {
                inputStream = openHttpConnection(url[0])
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bitmap
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)

            progress_bar.progress = values[0]!!

            progress_text.text = String.format(
                getString(R.string.progress_text), cont)
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            super.onPostExecute(bitmap)
            image_view.setImageBitmap(bitmap)

            progress_bar.progress = 100

            progress_text.text = String.format(
                getString(R.string.progress_text), 100)
        }

        private fun openHttpConnection(urlString: String?): InputStream? {
            var inputStream: InputStream? = null
            val url = URL(urlString)
            val conn = url.openConnection()
                ?: throw IOException("Not an HTTP connection")

            try {
                val httpConn: HttpURLConnection = conn as HttpURLConnection
                httpConn.allowUserInteraction = false
                httpConn.instanceFollowRedirects = true
                httpConn.requestMethod = "GET"
                httpConn.connect()

                if (httpConn.responseCode == HttpURLConnection.HTTP_OK)
                    inputStream = httpConn.inputStream

            } catch (ex: Exception) {
                throw IOException("Error Connecting")
            }

            return inputStream
        }
    }
}
