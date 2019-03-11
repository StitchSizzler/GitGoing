package com.example.cmput301w19t15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateRequest extends AppCompatActivity {

    private Button request;
    private Book newBook;
    private User owner;
    User loggedInUser = MainActivity.getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Bundle bundle = getIntent().getExtras();
        final String ownerId = (String) bundle.get("OWNERID");
        String author = (String) bundle.get("AUTHOR");
        String ownerEmail = (String) bundle.get("OWNEREMAIL");
        String isbn = (String) bundle.get("ISBN");
        final String title = (String) bundle.get("TITLE");
        String status = (String) bundle.get("STATUS");
        final String bookId = (String) bundle.get("BOOKID");


        TextView authorText = (TextView) findViewById(R.id.author);
        authorText.setText(author);
        TextView titleText = (TextView) findViewById(R.id.booktitle);
        titleText.setText(title);
        TextView isbnText = (TextView) findViewById(R.id.isbn);
        isbnText.setText(isbn);
        TextView ownerEmailText = (TextView) findViewById(R.id.owner);
        ownerEmailText.setText(ownerEmail);
        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText(status);
        request = (Button) findViewById(R.id.request_button);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("userID").addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey().equals(ownerId)) {
                                owner = (child.getValue(User.class));
                                ArrayList<Book> ownersBooks = owner.getMyBooks();
                                for (Book book : ownersBooks) {
                                    if (bookId.equals(book.getBookID())) {
                                        String borrowerID = loggedInUser.getUserID();
                                        newBook = new Book(book);
                                        newBook.setBorrowerID(borrowerID);
                                        break;
                                    }
                                }
                                break;
                            }
                        }


                        owner.addToRequestedBooks(newBook);
                        loggedInUser.addToMyRequestedBooks(newBook);
                        finish();


                    }

                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }
        });
    }

}