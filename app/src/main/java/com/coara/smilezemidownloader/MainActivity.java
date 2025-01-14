package com.coara.smilezemidownloader;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private Spinner deviceSpinner;
    private ListView firmwareListView;
    private TextView firmwareDetailsTextView;
    private Button downloadButton;
    private ArrayList<String> firmwareFiles;
    private String downloadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceSpinner = findViewById(R.id.deviceSpinner);
        firmwareListView = findViewById(R.id.firmwareListView);
        firmwareDetailsTextView = findViewById(R.id.detailsTextView);
        downloadButton = findViewById(R.id.downloadButton);

        String[] devices = {"ft5x_ts", "ad971jt", "ad971jt2", "ad971jt3", "ad971jt4", "ad970jt", "ad970jt2", "ad970jt3", "szj_js101", "szj_js201", "szj_js202", "szj_js203"};
        ArrayAdapter<String> deviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, devices);
        deviceSpinner.setAdapter(deviceAdapter);

        deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDevice = (String) parentView.getItemAtPosition(position);
                fetchFirmwareData(selectedDevice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
              
            }
        });

        firmwareListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedFirmware = firmwareFiles.get(position);
            fetchFirmwareDetails(selectedFirmware);
        });

        downloadButton.setOnClickListener(v -> downloadFirmware());
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
            }
        }
    }

    private void fetchFirmwareData(String device) {
        new Thread(() -> {
            try {

                String urlString = "https://smile-zemi.jp/client/platforms/v2/" + device + "/stable/updateInfo.xml?s=09";
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, null);

                int eventType = parser.getEventType();
                String currentTag = "";
                ArrayList<String> firmwareList = new ArrayList<>();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            currentTag = name;
                            break;
                        case XmlPullParser.TEXT:
                            if ("fileName".equals(currentTag)) {
                                firmwareList.add(parser.getText());
                            }
                            break;
                    }
                    eventType = parser.next();
                }

                runOnUiThread(() -> {
                    firmwareFiles = firmwareList;
                    ArrayAdapter<String> firmwareAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, firmwareFiles);
                    firmwareListView.setAdapter(firmwareAdapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "データの取得に失敗しました", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void fetchFirmwareDetails(String firmware) {

        firmwareDetailsTextView.setText("選択したファームウェア: " + firmware);
        downloadUrl = "http://d15ze2y2hx4s9h.cloudfront.net/platforms/v2/" + deviceSpinner.getSelectedItem().toString() + "/com.justsystems.smilehs.platform/stable/" + firmware;
        downloadButton.setEnabled(true);
    }

    private void downloadFirmware() {
        if (!downloadUrl.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
            startActivity(browserIntent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "パーミッションが許可されていません。", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
