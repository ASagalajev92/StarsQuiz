package com.devhuba.starsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView vImageViewStar;
    private Button vButton0;
    private Button vButton1;
    private Button vButton2;
    private Button vButton3;

    private String url = "https://www.imdb.com/list/ls050274118/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vImageViewStar = findViewById(R.id.imageViewStar);
        vButton0 = (Button) findViewById(R.id.button_v0);
        vButton1 = (Button) findViewById(R.id.button_v1);
        vButton2 = (Button) findViewById(R.id.button_v2);
        vButton3 = (Button) findViewById(R.id.button_v3);

        getContent();
    }

    private void getContent() {
        DownloadContentTask contentTask = new DownloadContentTask();
        try {
            String content = contentTask.execute(url).get();
            String start = "<span class=\"global-sprite lister-sort-reverse descending\" data-sort=\"list_order:descending\" title=\"Ascending order\"></span>";
            String end = "<span class=\"lister-item-index unbold text-primary\">100. </span>";
            Pattern pattern = Pattern.compile(start + "(.*?)" + end);
            Matcher matcher = pattern.matcher(content);
            String splittedContent = "";
            while (matcher.find()) {
                splittedContent = matcher.group(1);
            }
            Pattern patternImg = Pattern.compile("src=\"(.*?)\"");
            Pattern patternName = Pattern.compile("<img alt=\"(.*?)\"");
            Matcher matcherImg = patternImg.matcher(splittedContent);
            Matcher matcherName = patternName.matcher(splittedContent);

            //Log.i("splitted", splittedContent);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class DownloadContentTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
