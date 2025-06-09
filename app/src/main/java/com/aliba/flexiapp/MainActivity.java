package com.aliba.flexiapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.ImageButton;

import com.google.gson.Gson;

import Models.OperationResult;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button flashCardsButton = findViewById(R.id.button1);  // Flash Cards butonu
        flashCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Yeni activity'i başlat
                Intent intent = new Intent(MainActivity.this, FlashCardsActivity.class);
                startActivity(intent);
            }
        });

        // Fulfill Words butonu
        Button fulfillWordsButton = findViewById(R.id.button2);
        fulfillWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fulfill Words activity'i başlat
                Intent intent = new Intent(MainActivity.this, FulfillWordsActivity.class);
                startActivity(intent);
            }
        });

        Button multipleChoiceButton = findViewById(R.id.button3);
        multipleChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MultipleChoiceActivity.class);
                startActivity(intent);
            }
        });

        // Ask Ai Chat butonu
        Button aiChatButton = findViewById(R.id.button4);
        aiChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ask Ai Chat activity'i başlat
                Intent intent = new Intent(MainActivity.this, AskAiChat.class);
                startActivity(intent);
            }
        });
        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
