package com.aliba.flexiapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FulfillWordsActivity extends AppCompatActivity {

    private TextView maskedWord;
    private TextView turkishMeaning;
    private EditText guessInput;
    private Spinner levelSpinner;
    private List<Word> words;
    private List<Word> filteredWords;
    private Word currentWord;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulfill_words);

        maskedWord = findViewById(R.id.masked_word);
        turkishMeaning = findViewById(R.id.turkish_meaning);
        guessInput = findViewById(R.id.guess_input);
        levelSpinner = findViewById(R.id.spinnerLevel);
        Button btnNext = findViewById(R.id.btnNext);

        // Anasayfa butonu
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FulfillWordsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Bu aktiviteyi kapat
            }
        });

        // Seviye Spinner'ını ayarlama
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);

        // JSON'dan kelimeleri yükle
        words = loadWordsFromJson();

        // Spinner seçildiğinde seviyeye göre kelimeleri filtreleme
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLevel = (String) parentView.getItemAtPosition(position);
                filterWordsByLevel(selectedLevel);
                displayCurrentWord();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Seçim yapılmadığında herhangi bir işlem yapma
            }
        });

        // Sonraki buton olay dinleyicisi
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRandomWord(); // Rastgele bir kelime göster
            }
        });

        // Tahmin girdisi için TextWatcher ekleyerek kontrol işlemini otomatik yapalım
        guessInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Boş
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kullanıcı girdisini her değiştiğinde kontrol et
                checkGuess(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Boş
            }
        });
    }
    // Rastgele bir kelime gösteren metod
    private void displayRandomWord() {
        Random random = new Random();
        currentIndex = random.nextInt(filteredWords.size()); // Rastgele bir indeks seç
        displayCurrentWord(); // Yeni rastgele kelimeyi göster
    }
    private void displayCurrentWord() {
        currentWord = filteredWords.get(currentIndex);
        turkishMeaning.setText(currentWord.getTurkish());
        maskedWord.setText(generateMaskedWord(currentWord.getEnglish()));
        guessInput.setText(""); // Tahmin alanını temizle
    }

    private String generateMaskedWord(String word) {
        Random random = new Random();
        StringBuilder masked = new StringBuilder();
        int visibleCount = 0;

        // Her kelimede en az bir harf ve en fazla kelimenin yarısı kadar harf gösterilsin
        int minVisibleLetters = 1;
        int maxVisibleLetters = Math.max(1, word.length() / 2);

        for (int i = 0; i < word.length(); i++) {
            if (visibleCount < maxVisibleLetters && (random.nextBoolean() || visibleCount < minVisibleLetters)) {
                masked.append(word.charAt(i));
                visibleCount++;
            } else {
                masked.append("_");
            }
            masked.append(" ");
        }

        return masked.toString().trim();
    }

    // Kullanıcı tahminini kontrol eden metot
    private void checkGuess(String guess) {
        if (guess.equalsIgnoreCase(currentWord.getEnglish())) {
            maskedWord.setText(currentWord.getEnglish()); // Kelimeyi tam olarak göster
        }
    }

    // Seçilen seviyeye göre kelimeleri filtreleyen metot
    private void filterWordsByLevel(String level) {
        filteredWords = new ArrayList<>();
        for (Word word : words) {
            if (word.getLevel().equals(level)) {
                filteredWords.add(word);
            }
        }
        if (filteredWords.isEmpty()) {
            filteredWords.add(new Word("No Words", "No Translation", level));
        }
        currentIndex = 0;
    }

    // JSON'dan kelimeleri yükleme metodu
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

    // JSON dosyasını assets klasöründen yükleme metodu
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
