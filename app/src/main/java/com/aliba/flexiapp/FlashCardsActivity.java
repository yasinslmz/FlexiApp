package com.aliba.flexiapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlashCardsActivity extends AppCompatActivity {

    private FrameLayout flashcard;
    private TextView frontSide;
    private TextView backSide;
    private boolean showingBack = false; // Başlangıçta ön yüz gösteriliyor.

    private List<Word> words; // Kelimeleri tutacak liste
    private List<Word> filteredWords; // Kelimeleri tutacak liste
    private int currentIndex = 0; // Mevcut gösterilen kelime indeksi

    private Spinner levelSpinner; // Seviye Spinner'ı
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        // Geri Dön Butonunu Tanımlama
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlashCardsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Bu aktiviteyi kapat.
            }
        });

        // Pencere Kenar Boşluklarını Ayarlama
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Seviye Spinner'ını Tanımlama
        levelSpinner = findViewById(R.id.spinnerLevel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLevel = (String) parentView.getItemAtPosition(position);
                filterWordsByLevel(selectedLevel); // Seçilen seviyeye göre filtreleme
                currentIndex = 0; // Yeni seviyede ilk kelimeden başlamak için currentIndex'i sıfırla
                displayCurrentWord(); // Yeni kelimeyi göster
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Default davranış
            }
        });
        // Flashcard ve İçeriklerini Tanımlama
        flashcard = findViewById(R.id.flashcard);
        frontSide = findViewById(R.id.front_side);
        backSide = findViewById(R.id.back_side);

        // Kelimeleri JSON'dan Yükleme
        words = loadWordsFromJson();

        // Flashcard Tıklama Olayı (ön/arka yüzü çevirme)
        flashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingBack) {
                    showFront();
                } else {
                    showBack();
                }
            }
        });

        // Sonraki kelime butonu
        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRandomWord(); // Rastgele bir kelime göster
            }
        });

    }
    private void filterWordsByLevel(String level) {
        List<Word> levelSpecifiedWords = new ArrayList<>();
        for (Word word : words) {
            if (word.getLevel().equals(level)) {
                levelSpecifiedWords.add(word);
            }
        }
        this.filteredWords = levelSpecifiedWords; // Filtrelenmiş kelimeleri kullan
    }
    // Rastgele kelime gösteren metod
    private void displayRandomWord() {
        Random random = new Random();
        currentIndex = random.nextInt(filteredWords.size()); // Rastgele bir indeks seç
        displayCurrentWord(); // Yeni rastgele kelimeyi göster
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
        else {
            System.out.println("boş");
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

    // Mevcut kelimeyi gösteren metod
    private void displayCurrentWord() {
        Word currentWord = filteredWords.get(currentIndex);
        frontSide.setText(currentWord.getEnglish()); // Ön yüze İngilizce kelimeyi yaz
        backSide.setText(currentWord.getTurkish()); // Arka yüze Türkçe karşılığını yaz
        showFront(); // İlk olarak ön yüz gösterilecek
    }

    // Ön yüzü gösteren fonksiyon
    private void showFront() {
        frontSide.setVisibility(View.VISIBLE);
        backSide.setVisibility(View.GONE);
        showingBack = false;
    }

    // Arka yüzü gösteren fonksiyon
    private void showBack() {
        frontSide.setVisibility(View.GONE);
        backSide.setVisibility(View.VISIBLE);
        showingBack = true;
    }
}
