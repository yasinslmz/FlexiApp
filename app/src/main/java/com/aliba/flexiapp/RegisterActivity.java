package com.aliba.flexiapp;

import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameInput = findViewById(R.id.editTextFirstName);
        lastNameInput = findViewById(R.id.editTextLastName);
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (validateInputs(firstName, lastName, email, password)) {
                    sendRegisterRequest(firstName, lastName, email, password);
                }
            }
        });
    }

    private boolean validateInputs(String firstName, String lastName, String email, String password) {
        if (firstName.isEmpty()) {
            Toast.makeText(this, "Lütfen adınızı girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (lastName.isEmpty()) {
            Toast.makeText(this, "Lütfen soyadınızı girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Lütfen e-posta adresinizi girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Lütfen geçerli bir e-posta adresi girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Lütfen şifrenizi girin!", Toast.LENGTH_SHORT).show();
            return false;
        }

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

    private void sendRegisterRequest(String firstName, String lastName, String email, String password) {
        OkHttpClient client = getHttpClient();

        try {
            // JSON Gövdeyi Oluştur
            JSONObject jsonData = new JSONObject();
            jsonData.put("Name", firstName);
            jsonData.put("LastName", lastName);
            jsonData.put("Email", email);
            jsonData.put("Password", password);
            jsonData.put("EnglishLevel", "");

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
                    .url(ApiConfig.SIGNUP_ENDPOINT)
                    .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                    .addHeader("User-Agent", ApiConfig.USER_AGENT)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Sunucuya bağlanılamadı!", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);

                            Gson gson = new Gson();
                            OperationResult operationResult = gson.fromJson(responseBody, OperationResult.class);

                            if (operationResult.getID() != null && operationResult.getID() == 1) {
                                runOnUiThread(() -> {
                                    Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                                    finish(); // Aktiviteyi kapat
                                });
                            } else {
                                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, operationResult.getOperationMessage(), Toast.LENGTH_SHORT).show());
                            }

                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Yanıtı işleme hatası!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "HTTP Hatası: " + response.code(), Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "İstek oluşturma hatası!", Toast.LENGTH_SHORT).show();
        }
    }
}
