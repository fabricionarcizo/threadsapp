package dk.itu.moapd.threadsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class AsyncTaskFragment extends Fragment {

    private static final String CONT = "CONT";

    private byte cont;

    private ProgressBar mProgressBar;
    private TextView mProgressText;
    private ToggleButton mStartButton;
    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_threads, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setProgress(0);

        mStartButton = view.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Task().execute("https://goo.gl/X9uSQ9");
                ToggleButton button = (ToggleButton) view;
                button.setChecked(false);
            }
        });

        if (savedInstanceState != null)
            cont = savedInstanceState.getByte(CONT, (byte) 0);

        String text = String.format(
                getString(R.string.progressText), (int) cont);

        mProgressText = view.findViewById(R.id.progressText);
        mProgressText.setText(text);

        mImageView = view.findViewById(R.id.imageView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStartButton.setTextOff(
                getResources().getString(R.string.downloadButton));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByte(CONT, cont);
    }

    private class Task extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setProgress(0);

            String text = String.format(
                    getString(R.string.progressText), 0);
            mProgressText.setText(text);

            mImageView.setImageBitmap(null);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bitmap = null;
            InputStream in;

            try {
                in = OpenHttpConnection(url[0]);
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);

            String text = String.format(
                    getString(R.string.progressText), (int) cont);
            mProgressText.setText(text);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageView.setImageBitmap(bitmap);

            mProgressBar.setProgress(100);

            String text = String.format(
                    getString(R.string.progressText), 100);
            mProgressText.setText(text);
        }

        private InputStream OpenHttpConnection(String urlString)
                throws IOException {

            InputStream in = null;
            int response;

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK)
                    in = httpConn.getInputStream();

            } catch (Exception ex) {
                throw new IOException("Error Connecting");
            }

            return in;
        }
    }

}
