package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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
                            // Cần đảm bảo lớp Comic implement Serializable hoặc Parcelable
                            Comic newComic = (Comic) result.getData().getSerializableExtra("newComic");
                            if (newComic != null) {
                                comicList.add(newComic);
                                // Thay vì notifyDataSetChanged, nên dùng notifyItemInserted để hiệu quả hơn
                                comicAdapter.notifyItemInserted(comicList.size() - 1);
                                Toast.makeText(ManageComicsActivity.this, "Đã thêm truyện mới: " + newComic.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        recyclerComics = findViewById(R.id.recyclerComics);
        fabAdd = findViewById(R.id.fabAdd);

        // **SỬA LỖI 1: SỬ DỤNG GridLayoutManager VỚI spanCount = 3**
        // Điều này phù hợp với cài đặt app:spanCount="3" trong XML của bạn.
        recyclerComics.setLayoutManager(new GridLayoutManager(this, 3));
        comicAdapter = new ComicAdapter(this, comicList);
        recyclerComics.setAdapter(comicAdapter);

        comicAdapter.setOnItemClickListener(this);

        // Add sample comics
        comicList.add(new Comic("Solo Leveling", 179, "image1"));
        comicList.add(new Comic("Naruto", 700, "image2"));
        comicList.add(new Comic("One Piece", 1000, "image3"));
        comicList.add(new Comic("Jujutsu Kaisen", 139, "image4"));
        comicList.add(new Comic("Death Note", 200, "image5"));
        comicList.add(new Comic("Demon Slayer", 205, "image6"));
        comicList.add(new Comic("My Hero Academia", 400, "image7"));
        comicList.add(new Comic("Sakamoto", 350, "image8"));
        comicList.add(new Comic("Dragon Ball", 519, "image9"));
        comicAdapter.notifyDataSetChanged();

        // **SỬA LỖI 2: THÊM setOnClickListener VÀ XÓA CÁC PHƯƠNG THỨC LẠC LÕNG**
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thay thế `AddComicActivity.class` bằng tên Activity bạn dùng để thêm truyện
                Intent intent = new Intent(ManageComicsActivity.this, AddComicActivity.class);
                addComicLauncher.launch(intent);
            }
        });

    } // Kết thúc onCreate

    // **TRIỂN KHAI CÁC PHƯƠNG THỨC CỦA ComicAdapter.OnItemClickListener**
    @Override
    public void onAddChapterClick(int position) {
        // Xử lý logic khi nhấn nút 'Thêm chương' trên một truyện
        Toast.makeText(this, "Thêm chương cho: " + comicList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditChapterClick(int position) {
        // Xử lý logic khi nhấn nút 'Sửa chương' trên một truyện
        Toast.makeText(this, "Sửa thông tin truyện: " + comicList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteChapterClick(int position) {
        // Xử lý logic khi nhấn nút 'Xóa truyện'
        String title = comicList.get(position).getTitle();
        comicList.remove(position);
        comicAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Đã xóa truyện: " + title, Toast.LENGTH_SHORT).show();
    }
}