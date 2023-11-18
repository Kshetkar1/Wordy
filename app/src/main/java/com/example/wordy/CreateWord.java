package com.example.wordy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateWord extends AppCompatActivity {

    Button cancel;
    Button addWord;

    Button clearTheDataBase;

    EditText editTextTextET;
    TextView editTextTextTV, enterword;
    private final List<String> list = new ArrayList<>();

    private DatabaseReference mDatabase;
    String input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cancel = findViewById(R.id.BackID);
        cancel.setOnClickListener(cancelListener);
        addWord = findViewById(R.id.addWord2);
        addWord.setOnClickListener(submitListener);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("words");
        editTextTextET = findViewById(R.id.editTextTextET);
        enterword = findViewById(R.id.QuestionID);
        enterword.setOnClickListener(submitListener);
        clearTheDataBase = findViewById(R.id.clearFireBaseID);
        clearTheDataBase.setOnClickListener(clearTheData);


    }


    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(CreateWord.this, WordyActivity.class);
            startActivity(intent);
        }
    };


    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
             input = editTextTextET.getText().toString().toUpperCase();

            Pattern pattern = Pattern.compile("^[A-Z]+$");
            Matcher matcher = pattern.matcher(input);
            if (input.length() > 5 || input.length() < 5) {
                Toast.makeText(CreateWord.this, "Word needs to be exactly 5 letters", Toast.LENGTH_SHORT).show();
                enterword.setTextColor(getResources().getColor(R.color.purple_200));
            } else if (input.isEmpty()) {
                Toast.makeText(CreateWord.this, "it is empty! you need to add a word", Toast.LENGTH_SHORT).show();
                enterword.setTextColor(getResources().getColor(R.color.purple_200));
            } else if (!matcher.find() == true) {
                Toast.makeText(CreateWord.this, "There are some inputs that are not letters, change it so it has letters from the alphabet", Toast.LENGTH_SHORT).show();
                enterword.setTextColor(getResources().getColor(R.color.purple_200));
            }

            else {


                mDatabase.orderByChild("words").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       Iterable<DataSnapshot> data = snapshot.getChildren();
                       int lastElement = (int) snapshot.getChildrenCount();
                       Iterator<DataSnapshot> it = data.iterator();
                       for(int i=0; i < lastElement -1; i++){
                           String nextValue = (String) it.next().getValue();
                          list.add(nextValue);


                       }
                        if(!list.contains(input)) {
                            mDatabase.child(mDatabase.push().getKey()).setValue(input);
                            Toast.makeText(CreateWord.this, "Word has been added to the firebase", Toast.LENGTH_SHORT).show();

                            enterword.setTextColor(getResources().getColor(R.color.black));
                        }
                        else{
                            Toast.makeText(CreateWord.this, "already been added choose another word", Toast.LENGTH_SHORT).show();

                            enterword.setTextColor(getResources().getColor(R.color.purple_200));
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
                );


            }
        }
    };


    private View.OnClickListener clearTheData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClearTheDataBase(v);
        }
    };

        public void ClearTheDataBase(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("words");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase.removeValue();
                Toast.makeText(CreateWord.this, "Database cleared", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateWord.this, "Error in Database being cleared", Toast.LENGTH_SHORT).show();

            }
        });
    }





}


