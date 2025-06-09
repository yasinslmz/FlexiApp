package com.aliba.flexiapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.aliba.flexiapp.config.ApiConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.SharedPreferences;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class LevelTestActivity extends AppCompatActivity {

    private List<Word> b1Words, b2Words, c1Words, currentWords;
    private Word currentWord;
    private int correctAnswers = 0, currentQuestionIndex = 0;
    private String userLevel = "B1";

    private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD;
    private TextView tvQuestion, tvUserLevel;
    private List<Button> optionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_test);

        // Geri Dön Butonunu Tanımlama
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(LevelTestActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Bu aktiviteyi kapat.
        });

        btnOptionA = findViewById(R.id.btnOptionA);
        btnOptionB = findViewById(R.id.btnOptionB);
        btnOptionC = findViewById(R.id.btnOptionC);
        btnOptionD = findViewById(R.id.btnOptionD);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvUserLevel = findViewById(R.id.tvUserLevel); // Kullanıcının seviyesini göstermek için TextView

        optionButtons = new ArrayList<>();
        optionButtons.add(btnOptionA);
        optionButtons.add(btnOptionB);
        optionButtons.add(btnOptionC);
        optionButtons.add(btnOptionD);

        // JSON'dan kelimeleri yükle
        loadWordsFromJson();

        // Testi başlat
        startLevelTest();

        // Butonlara tıklama işlemi
        for (Button button : optionButtons) {
            button.setOnClickListener(view -> checkAnswer(button));
        }
    }

    private void startLevelTest() {
        if (userLevel.equals("B1")) {
            currentWords = getRandomWords(b1Words, 20);
        } else if (userLevel.equals("B2")) {
            currentWords = getRandomWords(b2Words, 20);
        } else if (userLevel.equals("C1")) {
            currentWords = getRandomWords(c1Words, 20);
        }

        correctAnswers = 0;
        currentQuestionIndex = 0;

        tvUserLevel.setText("Seviyenizi Ölçelim"); // Başlangıç metni
        loadNextQuestion();
    }


    private void checkAnswer(Button selectedButton) {
        String selectedAnswer = selectedButton.getText().toString();
        String correctAnswer = currentWord.getTurkish();

        if (selectedAnswer.equals(correctAnswer)) {
            correctAnswers++;
            selectedButton.setBackgroundColor(Color.GREEN);
        } else {
            selectedButton.setBackgroundColor(Color.RED);
            for (Button button : optionButtons) {
                if (button.getText().toString().equals(correctAnswer)) {
                    button.setBackgroundColor(Color.GREEN);
                }
            }
        }

        disableOptions();

        // Bir sonraki soruya geç
        currentQuestionIndex++;
        if (currentQuestionIndex < 20) {
            loadNextQuestion();
        } else {
            determineLevel();
        }
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
    private void sendSaveLevelRequest(String email, String englishLevel) {
        OkHttpClient client = getHttpClient();

        try {
            JSONObject jsonData = new JSONObject();
            jsonData.put("email", email);
            jsonData.put("englishLevel", englishLevel);

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
                    .url(ApiConfig.SAVE_LEVEL_ENDPOINT)
                    .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                    .addHeader("User-Agent", ApiConfig.USER_AGENT)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(LevelTestActivity.this, "Seviye kaydedilemedi!", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> Toast.makeText(LevelTestActivity.this, "Seviyeniz başarıyla kaydedildi!", Toast.LENGTH_SHORT).show());
                    } else {
                        runOnUiThread(() -> Toast.makeText(LevelTestActivity.this, "HTTP Hatası: " + response.code(), Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(LevelTestActivity.this, "İstek oluşturma hatası!", Toast.LENGTH_SHORT).show());
        }
    }

    private void determineLevel() {
        if (correctAnswers >= 15) {
            if (userLevel.equals("B1")) {
                userLevel = "B2";
                startLevelTest();
            } else if (userLevel.equals("B2")) {
                userLevel = "C1";
                startLevelTest();
            } else {
                tvUserLevel.setText("Seviyeniz: C1'den yüksek");
                Toast.makeText(this, "Tebrikler! Seviyeniz C1'den yüksek.", Toast.LENGTH_LONG).show();
                disableAllOptions(); // Butonları devre dışı bırak ve seviyeyi kaydet
            }
        } else {
            tvUserLevel.setText("Seviyeniz: " + userLevel);
            Toast.makeText(this, "Seviyeniz: " + userLevel, Toast.LENGTH_LONG).show();
            disableAllOptions(); // Butonları devre dışı bırak ve seviyeyi kaydet
        }
    }




    private void loadNextQuestion() {
        resetOptions();

        currentWord = currentWords.get(currentQuestionIndex);
        tvQuestion.setText(currentWord.getEnglish());

        // Doğru cevabın dışında 3 rastgele kelime seç
        List<Word> options = new ArrayList<>(currentWords);
        options.remove(currentWord);
        Collections.shuffle(options);

        List<String> answers = new ArrayList<>();
        answers.add(currentWord.getTurkish());
        for (int i = 0; i < 3 && i < options.size(); i++) {
            answers.add(options.get(i).getTurkish());
        }

        Collections.shuffle(answers);

        for (int i = 0; i < optionButtons.size(); i++) {
            optionButtons.get(i).setText(answers.get(i));
        }
    }

    private List<Word> getRandomWords(List<Word> words, int count) {
        List<Word> shuffled = new ArrayList<>(words);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    private void resetOptions() {
        for (Button button : optionButtons) {
            button.setBackgroundColor(Color.WHITE);
            button.setEnabled(true);
        }
    }

    private void disableOptions() {
//        for (Button button : optionButtons) {
//            button.setEnabled(false);
//        }
    }

    private void disableAllOptions() {
        for (Button button : optionButtons) {
            button.setBackgroundColor(Color.GRAY); // Butonları gri yap
            button.setEnabled(false); // Butonları devre dışı bırak
        }

        // Kullanıcı e-postasını al
        SharedPreferences sharedPreferences = getSharedPreferences("FlexiAppPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", null);

        if (email != null) {
            sendSaveLevelRequest(email, userLevel); // Seviyeyi kaydet
        } else {
            Toast.makeText(this, "Kullanıcı e-postası bulunamadı!", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadWordsFromJson() {
        b1Words = new ArrayList<>();
        b2Words = new ArrayList<>();
        c1Words = new ArrayList<>();

        String jsonStr = loadJsonFromAssets("words.json");
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String english = jsonObject.getString("english");
                    String turkish = jsonObject.getString("turkish");
                    String level = jsonObject.getString("level");

                    Word word = new Word(english, turkish, level);
                    if (level.equals("B1")) {
                        b1Words.add(word);
                    } else if (level.equals("B2")) {
                        b2Words.add(word);
                    } else if (level.equals("C1")) {
                        c1Words.add(word);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String loadJsonFromAssets(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
