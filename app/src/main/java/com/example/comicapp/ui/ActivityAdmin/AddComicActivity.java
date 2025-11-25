package com.example.comicapp.ui.ActivityAdmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.data.model.Comic;
import com.example.comicapp.R;

public class AddComicActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTitle;
    private EditText editSummary;
    private Spinner spinnerGenre;
    private ImageView imageCover;
    private Button buttonSelectImage;
    private Button buttonSave;
    private Button buttonCancel;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comic);

        // Initialize views
        editTitle = findViewById(R.id.editTitle);
        editSummary = findViewById(R.id.editSummary);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        imageCover = findViewById(R.id.imageCover);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Setup genre spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adapter);

        // Set click listeners
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComic();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageCover.setImageURI(selectedImageUri);
        }
    }

    private void saveComic() {
        String title = editTitle.getText().toString().trim();
        String summary = editSummary.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề truyện", Toast.LENGTH_SHORT).show();
            return;
        }

        if (summary.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tóm tắt nội dung", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new comic
        Comic newComic = new Comic(title, 0, selectedImageUri != null ? selectedImageUri.toString() : "", summary, genre);

        // Return the new comic to ManageComicsActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newComic", newComic);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
