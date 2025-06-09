package com.aliba.flexiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Models.ProfileResponse;
import com.aliba.flexiapp.config.ApiConfig;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameText, emailText, levelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // View'leri bağla
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        levelText = findViewById(R.id.levelText);
        // Geri Dön Butonunu Tanımlama
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Bu aktiviteyi kapat.
            }
        });
        // Seviye Tespit Butonu
        ImageButton levelTestButton = findViewById(R.id.btnLevelTest);
        levelTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Seviye Tespit aktivitesini başlat
                Intent intent = new Intent(ProfileActivity.this, LevelTestActivity.class);
                startActivity(intent);
            }
        });
        // Kullanıcı profilini getir
        fetchUserProfile();
    }

    private void fetchUserProfile() {
        System.out.println("fetchUserProfile çağrıldı...");

        SharedPreferences sharedPreferences = getSharedPreferences("FlexiAppPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        if (userEmail == null) {
            System.out.println("Kullanıcı e-postası SharedPreferences'de bulunamadı!");
            Toast.makeText(ProfileActivity.this, "Kullanıcı e-postası bulunamadı!", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Kullanıcı e-postası bulundu: " + userEmail);

        OkHttpClient client = getHttpClient();

        String url = ApiConfig.PROFILE_ENDPOINT + "?email=" + userEmail;
        System.out.println("Oluşturulan URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                .addHeader("User-Agent", ApiConfig.USER_AGENT)
                .get()
                .build();

        System.out.println("Request oluşturuldu.");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("İstek başarısız oldu: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Sunucuya bağlanılamadı!", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Sunucudan yanıt alındı. HTTP Kodu: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
                    System.out.println("Yanıt Gövdesi: " + responseBody);

                    try {
                        Gson gson = new Gson();
                        ProfileResponse profileResponse = gson.fromJson(responseBody, ProfileResponse.class);

                        System.out.println("ProfileResponse: " + profileResponse);

                        runOnUiThread(() -> {
                            usernameText.setText(profileResponse.getName() + " " + profileResponse.getLastName());
                            emailText.setText("Email: " + profileResponse.getEmail());
                            levelText.setText("Seviye: " + profileResponse.getEnglishLevel());
                        });
                    } catch (Exception e) {
                        System.out.println("Yanıt işlenirken hata oluştu: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Yanıt işlenemedi!", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    System.out.println("HTTP Hatası: " + response.code());
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "HTTP Hatası: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }



    private OkHttpClient getHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
