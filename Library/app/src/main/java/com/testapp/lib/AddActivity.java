package com.testapp.lib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private TextView titleView;
    EditText mainText,subText,subTexttwo;
    private Button addBtn;
    private DBHandler dbHandler;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHandler = new DBHandler(AddActivity.this);

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTimestamp = sdf.format(new Date(currentTimeMillis));

        mode = getIntent().getStringExtra("mode");
        //Toast.makeText(this, mode, Toast.LENGTH_LONG).show();

        titleView = findViewById(R.id.title_text);
        mainText = findViewById(R.id.sub_text);
        subText = findViewById(R.id.sub_text_2);
        subTexttwo = findViewById(R.id.sub_text_3);

        addBtn = findViewById(R.id.insert_btn);

        if (mode.equals("book")){
            titleView.setText("Add a Book to Library");
            mainText.setHint("Book Title");
            subText.setHint("Book Author");
            subTexttwo.setHint("Book Description");

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addBookRecord(mainText.getText().toString(), subText.getText().toString(), subTexttwo.getText().toString());
                }
            });


        }else if (mode.equals("member")){
            titleView.setText("Add a User");
            mainText.setHint("Name");

            subText.setVisibility(View.GONE);
            subText.setEnabled(false);
            subTexttwo.setVisibility(View.GONE);
            subTexttwo.setEnabled(false);

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMemberRecord(mainText.getText().toString());
                }
            });

        }else if (mode.equals("updatebook")){

            String book_id = getIntent().getStringExtra("id");
            String title = getIntent().getStringExtra("title");
            String author = getIntent().getStringExtra("author");
            String desc = getIntent().getStringExtra("desc");

            titleView.setText("Update a Book Record");

            mainText.setHint("Book Title");
            subText.setHint("Book Author");
            subTexttwo.setHint("Book Description");

            mainText.setText(title);
            subText.setText(author);
            subTexttwo.setText(desc);

            addBtn.setText("UPDATE BOOK");

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateBookRecord(book_id, mainText.getText().toString(), subText.getText().toString(), subTexttwo.getText().toString());
                }
            });

        }


    }

    private void updateBookRecord(String bookId, String title, String auth, String desc) {

        if (!title.isEmpty() || !auth.isEmpty() || !desc.isEmpty()){
            DBHandler dbHandler = new DBHandler(this);
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("author", auth);
            values.put("description", desc);

            String selection = "rec_id = ?";
            String[] selectionArgs = {bookId};

            int rowsAffected = db.update("records", values, selection, selectionArgs);

            if (rowsAffected > 0) {
                Toast.makeText(this, "Data updated!", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
            db.close();

        }else {
            Toast.makeText(AddActivity.this, "Empty fields", Toast.LENGTH_LONG).show();
        }

    }

    private void addBookRecord(String title, String auth, String desc) {

        if (!title.isEmpty() || !auth.isEmpty() || !desc.isEmpty()){
            dbHandler.addRecord(title,auth,desc);
            mainText.setText("");
            subText.setText("");
            subTexttwo.setText("");
            Toast.makeText(AddActivity.this, "Book added!", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(AddActivity.this, "Empty fields", Toast.LENGTH_LONG).show();
        }

    }

    private void addMemberRecord(String name) {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTimestamp = sdf.format(new Date(currentTimeMillis));

        if (!name.isEmpty()){
            dbHandler.addMembers(name,formattedTimestamp);
            mainText.setText("");
            Toast.makeText(AddActivity.this, "User added!", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(AddActivity.this, "Empty fields", Toast.LENGTH_LONG).show();
        }


    }
}