package com.aliba.flexiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.aliba.flexiapp.config.ApiConfig;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import Models.OperationResult;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
//                Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish(); // LoginActivity'yi kapat
                if (validateInputs(email, password)) {
                    sendSignInRequest(email, password);
                }
            }
        });

        // LoginActivity.java içinde
        Button signUpButton = findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        // Email boşsa
        if (email.isEmpty()) {
            Toast.makeText(this, "Lütfen e-posta adresinizi girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Email geçerli değilse
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Lütfen geçerli bir e-posta adresi girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Şifre boşsa
        if (password.isEmpty()) {
            Toast.makeText(this, "Lütfen şifrenizi girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Şifre uzunluk kontrolü
        if (password.length() < 6) {
            Toast.makeText(this, "Şifreniz en az 6 karakter olmalı!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

    private void sendSignInRequest(String email, String password) {
        OkHttpClient client = getHttpClient();

        try {
            System.out.println("JSON Gövde oluşturuluyor...");

            // JSON Gövdeyi Oluştur
            JSONObject jsonData = new JSONObject();
            jsonData.put("email", email);
            jsonData.put("password", password);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonData);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("data", jsonArray);

            System.out.println("JSON Gövde oluşturuldu: " + jsonBody.toString());

            // RequestBody oluştur
            String payload = jsonBody.toString();
            RequestBody body = RequestBody.create(
                    payload,
                    MediaType.get("application/json; charset=utf-8")
            );

            // Payload'ı konsola yazdır
            System.out.println("Giden Request Payload (Body): " + payload);

            // İstek oluştur
            Request request = new Request.Builder()
                    .url(ApiConfig.SIGNIN_ENDPOINT)
                    .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                    .addHeader("User-Agent", ApiConfig.USER_AGENT)
                    .post(body)
                    .build();

            System.out.println("Request oluşturuldu: " + request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("İstek başarısız oldu: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Sunucuya bağlanılamadı!", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("Sunucudan yanıt alındı. HTTP Kodu: " + response.code());

                    if (response.isSuccessful()) {
                        try {
                            // Yanıt gövdesini al
                            String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
                            System.out.println("Yanıt Gövdesi: " + responseBody);

                            // Gson ile JSON'u modele dönüştür
                            Gson gson = new Gson();
                            OperationResult operationResult = gson.fromJson(responseBody, OperationResult.class);

                            System.out.println("OperationResult: " + operationResult);

                            // Başarı durumu kontrolü
                            if (operationResult.getID() != null && operationResult.getID() == 1) {
                                System.out.println("Kullanıcı başarılı şekilde giriş yaptı.");

                                // E-postayı SharedPreferences'e kaydet
                                SharedPreferences sharedPreferences = getSharedPreferences("FlexiAppPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_email", email);
                                editor.apply();

                                runOnUiThread(() -> {
                                    Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // LoginActivity'yi kapat
                                });
                            } else {
                                System.out.println("Kullanıcı bulunamadı veya giriş başarısız.");
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Böyle bir kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show());
                            }

                        } catch (Exception e) {
                            System.out.println("Yanıt işlenirken hata oluştu: " + e.getMessage());
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Yanıtı işleme hatası!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        System.out.println("HTTP Hatası: " + response.code());
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "HTTP Hatası: " + response.code(), Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("İstek oluşturulurken hata oluştu: " + e.getMessage());
            Toast.makeText(LoginActivity.this, "İstek oluşturma hatası!", Toast.LENGTH_SHORT).show();
        }
    }


}
