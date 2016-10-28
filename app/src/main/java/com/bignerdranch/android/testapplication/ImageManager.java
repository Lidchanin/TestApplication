package com.bignerdranch.android.testapplication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Lidchanin on 28.10.2016.
 */

public class ImageManager {
    private final static String TAG = "ImageManager";

    private ImageManager () {}

    public static void fetchImage(final String iUrl, final ImageView iView) {
        if ( iUrl == null || iView == null )
            return;

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                final Bitmap image = (Bitmap) message.obj;
                iView.setImageBitmap(image);
            }
        };

        final Thread thread = new Thread() {
            @Override
            public void run() {
                final Bitmap image = downloadImage(iUrl);
                if ( image != null ) {
                    Log.v(TAG, "Got image by URL: " + iUrl);
                    final Message message = handler.obtainMessage(1, image);
                    handler.sendMessage(message);
                }
            }
        };
        iView.setImageResource(R.drawable.icon);
        thread.setPriority(3);
        thread.start();
    }

    private static Bitmap downloadImage(String iUrl) {
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream buf_stream = null;
        try {
            Log.v(TAG, "Starting loading image by URL: " + iUrl);
            httpURLConnection = (HttpURLConnection) new URL(iUrl).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.connect();
            buf_stream = new BufferedInputStream(httpURLConnection.getInputStream(), 8192);
            bitmap = BitmapFactory.decodeStream(buf_stream);
            buf_stream.close();
            httpURLConnection.disconnect();
            buf_stream = null;
            httpURLConnection = null;
        } catch (MalformedURLException ex) {
            Log.e(TAG, "Url parsing was failed: " + iUrl);
        } catch (IOException ex) {
            Log.d(TAG, iUrl + " does not exists");
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "Out of memory!!!");
            return null;
        } finally {
            if ( buf_stream != null )
                try {
                    buf_stream.close();
                } catch (IOException ignored) {}
            if ( httpURLConnection != null )
                httpURLConnection.disconnect();
        }
        return bitmap;
    }
}