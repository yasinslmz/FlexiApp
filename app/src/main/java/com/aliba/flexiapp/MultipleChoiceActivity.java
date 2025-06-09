package com.aliba.flexiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultipleChoiceActivity extends AppCompatActivity {

    private Spinner levelSpinner;
    private List<Word> words;
    private List<Word> filteredWords;
    private Word currentWord;

    private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD, btnNext;
    private List<Button> optionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);
        // Butonları tanımla
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            finish(); // Mevcut aktiviteyi kapatır ve bir önceki aktiviteye döner
        });
        // Butonları tanımla
        levelSpinner = findViewById(R.id.spinnerLevel);
        btnOptionA = findViewById(R.id.btnOptionA);
        btnOptionB = findViewById(R.id.btnOptionB);
        btnOptionC = findViewById(R.id.btnOptionC);
        btnOptionD = findViewById(R.id.btnOptionD);
        btnNext = findViewById(R.id.btnNext);

        // Butonları listeye ekle
        optionButtons = new ArrayList<>();
        optionButtons.add(btnOptionA);
        optionButtons.add(btnOptionB);
        optionButtons.add(btnOptionC);
        optionButtons.add(btnOptionD);

        btnNext.setVisibility(View.GONE);

        // JSON'dan kelimeleri yükleme
        words = loadWordsFromJson();

        // Spinner listener
        levelSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedLevel = parent.getItemAtPosition(position).toString();
                filterWordsByLevel(selectedLevel);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Butonlara tıklama işlemi
        for (Button button : optionButtons) {
            button.setOnClickListener(view -> checkAnswer(button));
        }

        // "Sonraki" butonuna tıklama
        btnNext.setOnClickListener(v -> loadNextQuestion());
    }
    private void checkAnswer(Button selectedButton) {
        String selectedAnswer = selectedButton.getText().toString();
        String correctAnswer = currentWord.getTurkish();

        // Kart üzerindeki TextView'i alıyoruz
        TextView tvQuestion = findViewById(R.id.tvQuestion);

        // SpannableStringBuilder ile İngilizce kelimenin altına doğru/yanlış ekle
        SpannableStringBuilder spannable = new SpannableStringBuilder();
        spannable.append(currentWord.getEnglish()); // İngilizce kelime
        spannable.append("\n"); // Yeni satır
        if (selectedAnswer.equals(correctAnswer)) {
            // Doğru cevap
            spannable.append("doğru");
            spannable.setSpan(new ForegroundColorSpan(Color.GREEN), spannable.length() - "Doğru".length(), spannable.length(), 0); // Yeşil renk
            selectedButton.setBackgroundResource(R.drawable.rounded_button_correct);
            selectedButton.setTextColor(Color.GREEN); // Doğru cevabın metin rengi
        } else {
            // Yanlış cevap
            spannable.append("yanlış");
            spannable.setSpan(new ForegroundColorSpan(Color.RED), spannable.length() - "Yanlış".length(), spannable.length(), 0); // Kırmızı renk
            selectedButton.setBackgroundResource(R.drawable.rounded_button_incorrect);
            selectedButton.setTextColor(Color.RED); // Yanlış cevabın metin rengi

            // Doğru cevabı bul ve yeşil yap
            for (Button button : optionButtons) {
                if (button.getText().toString().equals(correctAnswer)) {
                    button.setBackgroundResource(R.drawable.rounded_button_correct);
                    button.setTextColor(Color.GREEN); // Doğru cevabın metin rengi
                    break;
                }
            }
        }

        tvQuestion.setText(spannable); // TextView'e güncellenmiş metni ata
        // Diğer butonları devre dışı bırak
        for (Button button : optionButtons) {
            if (!button.getText().toString().equals(correctAnswer)) {
                button.setTextColor(Color.RED); // Diğer yanlış cevapların metin rengi
            }else{
                button.setTextColor(Color.GREEN);
            }
        }

        // "Sonraki" butonunu göster
        btnNext.setVisibility(View.VISIBLE);
        btnNext.invalidate(); // UI'yi güncelle
    }
    private void resetOptions() {
        for (Button button : optionButtons) {
            button.setBackgroundResource(R.drawable.rounded_button_default); // Varsayılan arka plan
            button.setTextColor(Color.WHITE); // Varsayılan metin rengi (örneğin beyaz)
            button.setEnabled(true); // Butonları tekrar etkin hale getir
        }
    }

    private void disableOptions() {
        for (Button button : optionButtons) {
           // button.setEnabled(false);
        }
    }


    private void filterWordsByLevel(String level) {
        filteredWords = new ArrayList<>();
        for (Word word : words) {
            if (word.getLevel().equals(level)) {
                filteredWords.add(word);
            }
        }

        // Seviye değiştirildiğinde yeni bir soru yükle
        if (!filteredWords.isEmpty()) {
            loadNextQuestion();
        } else {
            Toast.makeText(this, "Seçilen seviyede kelime bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNextQuestion() {
        if (filteredWords == null || filteredWords.isEmpty()) {
            Toast.makeText(this, "Seçilen seviyede kelime bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }

        resetOptions(); // Butonları sıfırla

        // Rastgele bir doğru cevap seç
        Random random = new Random();
        currentWord = filteredWords.get(random.nextInt(filteredWords.size()));

        // Kart üzerindeki kelimeyi güncelle
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestion.setText(currentWord.getEnglish()); // İngilizce kelimeyi göster

        // Doğru cevabın dışında 3 rastgele kelime seç
        List<Word> options = new ArrayList<>(filteredWords);
        options.remove(currentWord);
        Collections.shuffle(options);

        List<String> answers = new ArrayList<>();
        answers.add(currentWord.getTurkish()); // Doğru cevabı ekle
        for (int i = 0; i < 3 && i < options.size(); i++) {
            answers.add(options.get(i).getTurkish());
        }

        // Seçenekleri karıştır
        Collections.shuffle(answers);

        // Butonlara seçenekleri ata
        for (int i = 0; i < optionButtons.size(); i++) {
            optionButtons.get(i).setText(answers.get(i));
        }

        // "Sonraki" butonunu gizle
        btnNext.setVisibility(View.GONE);
    }


    private List<Word> loadWordsFromJson() {
        List<Word> wordList = new ArrayList<>();
        String jsonStr = loadJsonFromAssets("words.json");

        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String english = jsonObject.getString("english");
                    String turkish = jsonObject.getString("turkish");
                    String level = jsonObject.getString("level");
                    wordList.add(new Word(english, turkish, level)); // Listeye kelimeleri ekle
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return wordList;
    }

    private String loadJsonFromAssets(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
