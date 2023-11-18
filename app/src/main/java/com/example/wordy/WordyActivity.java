package com.example.wordy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class WordyActivity extends AppCompatActivity {

    Button addWordButton;
    Button submit;
    Button clear;
    Button restart;
    Button mainPage;
    private final List<String> retrieveListFromFB = new ArrayList<>();
    private final List<Character> input = new ArrayList<>();
    private DatabaseReference mDatabase;
    GridLayout grid;

    Random rand = new Random();


    int row = 0;
    String d = "";

    TextView targetWordTV;
    String targetWord = "";
    char guessChar;

    boolean isInputLocked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addWordButton = findViewById(R.id.addButtonID);
        addWordButton.setOnClickListener(nextPage);
        submit = findViewById(R.id.submitID);
        submit.setOnClickListener(submitListener);
        grid = findViewById(R.id.light_grid);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("words");
        restart = findViewById(R.id.restartID);
        restart.setOnClickListener(restartTheListener);
        clear = findViewById(R.id.ClearID);
        clear.setOnClickListener(clearListener);
        mainPage = findViewById(R.id.titlePageID);
        mainPage.setOnClickListener(newMainPage);


        mDatabase.orderByChild("words").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                           @Override
                                                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                               Iterable<DataSnapshot> data = snapshot.getChildren();
                                                                               int lastElement = (int) snapshot.getChildrenCount();
                                                                               Iterator<DataSnapshot> it = data.iterator();
                                                                               for (int i = 0; i < lastElement - 1; i++) {
                                                                                   String nextValue = (String) it.next().getValue();
                                                                                   retrieveListFromFB.add(nextValue);
                                                                               }
                                                                               int random = rand.nextInt(retrieveListFromFB.size());
                                                                               String word = retrieveListFromFB.get(random);
                                                                               Toast.makeText(WordyActivity.this, "The word is: " + word, Toast.LENGTH_SHORT).show();
                                                                           }

                                                                           @Override
                                                                           public void onCancelled(@NonNull DatabaseError error) {

                                                                           }
                                                                       }
        );
    }


    private View.OnClickListener restartTheListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // restartGame();
            generateNewRandomWord();
        }

//        public void restartGame(){
//            generateNewRandomWord();
//        }

        private void generateNewRandomWord() {

            input.removeAll(input);
            for (int i = 0; i < 29; i++) {
                EditText text2 = (EditText) grid.getChildAt(i);

                text2.setText("");


                int random = rand.nextInt(retrieveListFromFB.size());
                targetWord = retrieveListFromFB.get(random);
                Toast.makeText(WordyActivity.this, "The new word is: " + targetWord, Toast.LENGTH_SHORT).show();

            }
        }
    };
    private View.OnClickListener nextPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(WordyActivity.this, CreateWord.class);
            startActivity(intent);
//


        }
    };

    private View.OnClickListener newMainPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(WordyActivity.this, MainPage.class);
            startActivity(intent);
//


        }
    };


    private View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            input.removeAll(input);
            for (int i = 0; i < 29; i++) {
                EditText text2 = (EditText) grid.getChildAt(i);

                text2.setText("");


            }
            Toast.makeText(WordyActivity.this, "Has Been cleared", Toast.LENGTH_SHORT).show();
            row++;

        }

    };


    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (row < 30) {
                input.clear();

                for (int i = 0; i < 5; i++) {
                    EditText text = (EditText) grid.getChildAt(i + row * 5);
                    if (text != null) {
                        text.setBackgroundColor(ContextCompat.getColor(WordyActivity.this, R.color.grey));
                    }
                }

                // Collect entered characters
                for (int j = 0; j < 5; j++) {
                    EditText text = (EditText) grid.getChildAt(j + row * 5);
                    String inputText = text.getText().toString();
                    if (!inputText.isEmpty()) {
                        char character = inputText.charAt(0);
                        input.add(character);
                    }
                }


                for (int k = 0; k < 5; k++) {
                    EditText text = (EditText) grid.getChildAt(k + row * 5);
                    String inputText = text.getText().toString();

                    // Check if the EditText is not empty
                    if (!inputText.isEmpty()) {
                        char character = inputText.charAt(0);

                        if (!targetWord.isEmpty() && targetWord.length() > k) {
                            char targetChar = targetWord.charAt(k);

                            if (character == targetChar) {
                                text.setBackgroundColor(ContextCompat.getColor(WordyActivity.this, R.color.green));
                            } else if (targetWord.contains(String.valueOf(character))) {
                                text.setBackgroundColor(ContextCompat.getColor(WordyActivity.this, R.color.yellow));
                            }
                        }
                    }
                }


                if (!targetWord.isEmpty() && targetWord.length() >= 5) {
                    String enteredWord = "";
                    for (int l = 0; l < 5; l++) {
                        EditText text = (EditText) grid.getChildAt(l + row * 5);
                        enteredWord += text.getText().toString().trim();
                    }

                    if (!enteredWord.isEmpty() && enteredWord.equals(targetWord)) {
                        for (int m = 0; m < 5; m++) {
                            EditText text = (EditText) grid.getChildAt(m + row * 5);
                            text.setBackgroundColor(ContextCompat.getColor(WordyActivity.this, R.color.green));
                        }
                    }
                }

                row++;
                if (!targetWord.isEmpty() && targetWord.length() >= 5) {
                    String enteredWord = "";
                    for (int j = 0; j < 5; j++) {
                        EditText text = (EditText) grid.getChildAt(j + row * 5);
                        enteredWord += text.getText().toString().trim();
                    }

                    if (!enteredWord.isEmpty() && enteredWord.equals(targetWord)) {

                        Toast.makeText(WordyActivity.this, "Congrats! You won the game!", Toast.LENGTH_SHORT).show();
                    }
                }


            } else {
                Toast.makeText(WordyActivity.this, "Maximum number of rows reached", Toast.LENGTH_SHORT).show();
                submit.setEnabled(false);
            }
        }
    };
}





