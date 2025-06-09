package com.aliba.flexiapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AskAiChat extends AppCompatActivity {

    private EditText userInput;
    private Button sendButton;
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private static final String BASE_URL = "https://api.openai.com/v1";
    private static final String API_KEY = "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";

    private String threadId;
    private String runId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_ai_chat);

        userInput = findViewById(R.id.editTextUserInput);
        sendButton = findViewById(R.id.buttonSend);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        // Mesaj Listesi
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        // İlk mesaj: Sanal Öğretmenle Konuşabilirsiniz
        addMessage("Sanal Öğretmenle Konuşabilirsiniz.", "system");

        // Thread oluşturma
        createThread();
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            // Ana sayfaya yönlendirme kodu
            finish(); // Geri dönmek için
        });
        sendButton.setOnClickListener(v -> {
            String question = userInput.getText().toString().trim();
            if (!question.isEmpty() && threadId != null) {
                addMessage(question, "user");
                userInput.setText("");

                // Mesaj gönder
                createMessage(question);
            } else {
                Log.e("Error", "Thread oluşturuluyor veya giriş boş!");
            }
        });
    }

    private void addMessage(String content, String sender) {
        messages.add(new Message(content, sender));
        messageAdapter.notifyItemInserted(messages.size() - 1);
        recyclerViewMessages.scrollToPosition(messages.size() - 1);
    }

    private OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .addHeader("OpenAI-Beta", "assistants=v2")
                            .build();
                    return chain.proceed(request);
                }).build();
    }

    private void createThread() {
        OkHttpClient client = getHttpClient();
        RequestBody body = RequestBody.create("{}", MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", "Thread oluşturma hatası: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        threadId = jsonResponse.getString("id");
                    } catch (Exception e) {
                        Log.e("Error", "Thread yanıtını işleme hatası: " + e.getMessage());
                    }
                } else {
                    Log.e("Error", "Thread oluşturma hatası: HTTP " + response.code());
                }
            }
        });
    }

    private void createMessage(String message) {
        OkHttpClient client = getHttpClient();

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("role", "user");
            jsonBody.put("content", message);

            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(BASE_URL + "/threads/" + threadId + "/messages")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Error", "Mesaj oluşturma hatası: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runAssistantRun();
                    } else {
                        Log.e("Error", "Mesaj oluşturma hatası: HTTP " + response.code());
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Error", "Mesaj oluşturma hatası: " + e.getMessage());
        }
    }

    private void runAssistantRun() {
        OkHttpClient client = getHttpClient();

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("assistant_id", "asst_2E78WzgYn9JPCkFzJeYdw3Qo");

            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(BASE_URL + "/threads/" + threadId + "/runs")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Error", "Run oluşturma hatası: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            runId = jsonResponse.getString("id");
                            checkRunStatus(runId, 0);
                        } catch (Exception e) {
                            Log.e("Error", "Run yanıtını işleme hatası: " + e.getMessage());
                        }
                    } else {
                        Log.e("Error", "Run oluşturma hatası: HTTP " + response.code());
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Error", "Run oluşturma hatası: " + e.getMessage());
        }
    }

    private void checkRunStatus(String runId, int retryCount) {
        if (retryCount > 5) {
            Log.e("Error", "Run durumu kontrol hatası: Zaman aşımı.");
            return;
        }

        OkHttpClient client = getHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads/" + threadId + "/runs/" + runId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", "Run durumu kontrol hatası: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String status = jsonResponse.getString("status");

                        if ("completed".equals(status)) {
                            fetchMessages();
                        } else {
                            new Handler(getMainLooper()).postDelayed(() -> checkRunStatus(runId, retryCount + 1), 700);
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Run durumu işleme hatası: " + e.getMessage());
                    }
                } else {
                    Log.e("Error", "Run durumu kontrol hatası: HTTP " + response.code());
                }
            }
        });
    }

    private void fetchMessages() {
        OkHttpClient client = getHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads/" + threadId + "/messages")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", "Mesaj alma hatası: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray data = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject message = data.getJSONObject(i);
                            if ("assistant".equals(message.getString("role"))) {
                                JSONArray contentArray = message.getJSONArray("content");
                                if (contentArray.length() > 0) {
                                    JSONObject content = contentArray.getJSONObject(0);
                                    JSONObject textObject = content.getJSONObject("text");
                                    String aiResponse = textObject.getString("value");
                                    runOnUiThread(() -> addMessage(aiResponse, "assistant"));
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Mesaj işleme hatası: " + e.getMessage());
                    }
                } else {
                    Log.e("Error", "Mesaj alma hatası: HTTP " + response.code());
                }
            }
        });
    }
}
