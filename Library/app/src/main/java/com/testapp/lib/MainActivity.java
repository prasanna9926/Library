package com.testapp.lib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private RecyclerView bookContainer, userContainer;
    private List<Book> bookList;
    private BookAdapter bookAdapter;
    private List<Users> userList;
    private UserAdapter userAdapter;

    private Button addBook, addMem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(MainActivity.this);
        if (isRecordsTableEmpty() || isMembersTableEmpty()){
            addFirstRecs();
            Toast.makeText(MainActivity.this,"Loading initial records...", Toast.LENGTH_LONG).show();
        }
        loadBooks();
        loadUsers();

        addBook = findViewById(R.id.insert_btn);
        addMem = findViewById(R.id.user_insert_btn);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("mode", "book");
                startActivity(intent);
            }
        });

        addMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("mode", "member");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
        loadUsers();
    }

    private void addFirstRecs() {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTimestamp = sdf.format(new Date(currentTimeMillis));

        dbHandler.addRecord("Running in the Family", "Michael Ondaatje","Running in the Family is a fictionalized memoir, written in post-modern style involving aspects of magic realism, by Michael Ondaatje. It deals with his return to his native island of Sri Lanka, also called Ceylon, in the late 1970s. It also deals with his family");
        dbHandler.addRecord("The Seven Moons of Maali Almeida","Shehan Karunatilaka","The Seven Moons of Maali Almeida is a 2022 novel by Sri Lankan author Shehan Karunatilaka. It won the 2022 Booker Prize, the announcement being made at a ceremony at the Roundhouse in London on 17 October 2022");
        dbHandler.addRecord("What Lies Between Us","Nayomi Munaweera","A young girl grows up carefree in the midst of her loving family in a sprawling old house in Sri Lanka. Her childhood is like any other until a cherished friendship is seen to have monstrous undertones and the consequences spell both the end of her childhood and that of her home. Ostracized by an unforgiving community, the girl and her mother seek safety by immigrating to America. Years later, when she falls in love with Daniel, it appears she has found her happily ever after.");
        dbHandler.addRecord("On Sal Mal Lane","Ru Freeman","Sri Lanka, 1979. The Herath family has just moved to Sal Mal Lane, a quiet street disturbed only by the cries of the children whose triumphs and tragedies sustain the families that live there. As the neighbors adapt to the newcomers in different ways, the children fill their days with cricket matches, romantic crushes, and small rivalries.");
        dbHandler.addRecord("The Road to Peradeniya","Sir Ivor Jennings","Autobiography of Sir William Ivor Jennings, 1903-1965, British constitutional lawyer and educationalist.");

        dbHandler.addMembers("Amal Perera",formattedTimestamp);
        dbHandler.addMembers("Kasun Kalhara",formattedTimestamp);
        dbHandler.addMembers("Amandi Sithara",formattedTimestamp);
        dbHandler.addMembers("Gangani Gamage",formattedTimestamp);
        dbHandler.addMembers("Dimantha Ashan",formattedTimestamp);
    }

    private void loadBooks() {

        bookContainer = findViewById(R.id.book_container);
        dbHandler = new DBHandler(this);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookContainer.setLayoutManager(layoutManager);
        bookContainer.setAdapter(bookAdapter);

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String[] projection = {"rec_id", "title", "author", "description"};
        Cursor cursor = db.query("records", projection, null, null, null, null, "rec_id DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int rec_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("rec_id")));
                String id = String.valueOf(rec_id);
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                Book book = new Book(id, title, author, desc);
                bookList.add(book);
            } while (cursor.moveToNext());

            cursor.close();
            bookAdapter.notifyDataSetChanged();
        }

        db.close();

    }

    private void loadUsers() {

        userContainer = findViewById(R.id.user_container);
        dbHandler = new DBHandler(this);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        userContainer.setLayoutManager(layoutManager);
        userContainer.setAdapter(userAdapter);

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String[] projection = {"mem_id", "name", "date"};
        Cursor cursor = db.query("members", projection, null, null, null, null, "mem_id DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int rec_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("mem_id")));
                String id = String.valueOf(rec_id);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                Users user = new Users(id, name, date);
                userList.add(user);
            } while (cursor.moveToNext());

            cursor.close();
            userAdapter.notifyDataSetChanged();
        }

        db.close();

    }

    public boolean isRecordsTableEmpty() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM records", null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        return true; // Assume empty if cursor is null
    }

    public boolean isMembersTableEmpty() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM members", null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        return true; // Assume empty if cursor is null
    }

}