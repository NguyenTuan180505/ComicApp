package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import android.view.View;
import android.widget.Toast;

public class ManageComicsActivity extends AppCompatActivity implements ComicAdapter.OnItemClickListener {

    private RecyclerView recyclerComics;
    private FloatingActionButton fabAdd;
    private ComicAdapter comicAdapter;
    private ArrayList<Comic> comicList = new ArrayList<>();

    private ActivityResultLauncher<Intent> addComicLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_comics);

        // Initialize ActivityResultLauncher
        addComicLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Comic newComic = (Comic) result.getData().getSerializableExtra("newComic");
                            if (newComic != null) {
                                comicList.add(newComic);
                                comicAdapter.notifyDataSetChanged();
                                Toast.makeText(ManageComicsActivity.this, "Đã thêm truyện mới: " + newComic.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        recyclerComics = findViewById(R.id.recyclerComics);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerComics.setLayoutManager(new LinearLayoutManager(this));
        comicAdapter = new ComicAdapter(this, comicList);
        recyclerComics.setAdapter(comicAdapter);

        comicAdapter.setOnItemClickListener(this);

        // Add sample comics
        comicList.add(new Comic("Solo Leveling", 179, "image1"));
        comicList.add(new Comic("Naruto", 700, "image2"));
        comicList.add(new Comic("One Piece", 1000, "image3"));
        comicAdapter.notifyDataSetChanged();

        // Set FAB click listener
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageComicsActivity.this, AddComicActivity.class);
                addComicLauncher.launch(intent);
            }
        });
    }

    @Override
    public void onAddChapterClick(int position) {
        Toast.makeText(this, "Add chapter for: " + comicList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditChapterClick(int position) {
        Toast.makeText(this, "Edit chapter for: " + comicList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteChapterClick(int position) {
        Toast.makeText(this, "Delete chapter for: " + comicList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}