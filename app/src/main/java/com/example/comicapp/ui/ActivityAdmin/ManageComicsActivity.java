package com.example.comicapp.ui.ActivityAdmin;

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

import com.example.comicapp.data.adapter.ComicAdapter;
import com.example.comicapp.ui.ActivityUser.HomeActivity;
import com.example.comicapp.data.model.Comic;
import com.example.comicapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;
import com.example.comicapp.ui.ActivityUser.LoginActivity;

import java.util.ArrayList;

public class ManageComicsActivity extends AppCompatActivity implements ComicAdapter.OnItemClickListener {

    private RecyclerView recyclerComics;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDeleteSelected;
    private MaterialButton btnLogoutAdmin;
    private MaterialButton btnToggleSelect;
    private ComicAdapter comicAdapter;
    private ArrayList<Comic> comicList = new ArrayList<>();

    private ActivityResultLauncher<Intent> addComicLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_comics);

        String role = getSharedPreferences("auth", MODE_PRIVATE).getString("role", "");
        if (!"admin".equals(role)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

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
        fabDeleteSelected = findViewById(R.id.fabDeleteSelected);
        btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);
        btnToggleSelect = findViewById(R.id.btnToggleSelect);

        if (fabDeleteSelected != null) {
            fabDeleteSelected.setVisibility(View.GONE);
        }

        // Sử dụng GridLayoutManager với spanCount = 3 để khớp XML
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

        btnToggleSelect.setOnClickListener(v -> {
            boolean enable = !v.isSelected();
            v.setSelected(enable);
            btnToggleSelect.setText(enable ? "Đang chọn" : "Chọn");
            comicAdapter.setSelectionMode(enable);
            if (fabDeleteSelected != null) {
                fabDeleteSelected.setVisibility(enable ? View.VISIBLE : View.GONE);
            }
        });

        fabDeleteSelected.setOnClickListener(v -> {
            ArrayList<Integer> selected = comicAdapter.getSelectedPositions();
            if (selected.isEmpty()) return;
            // Xóa từ vị trí lớn đến nhỏ để tránh lệch index
            selected.sort((a,b) -> Integer.compare(b,a));
            for (int pos : selected) {
                String title = comicList.get(pos).getTitle();
                getSharedPreferences("chapters", MODE_PRIVATE)
                        .edit()
                        .remove(title + "_chapter_count")
                        .apply();
                comicAdapter.removeAt(pos);
            }
            comicAdapter.setSelectionMode(false);
            btnToggleSelect.setSelected(false);
            btnToggleSelect.setText("Chọn");
            if (fabDeleteSelected != null) {
                fabDeleteSelected.setVisibility(View.GONE);
            }
        });

        btnLogoutAdmin.setOnClickListener(v -> {
            getSharedPreferences("auth", MODE_PRIVATE)
                    .edit()
                    .putBoolean("logged_in", false)
                    .remove("role")
                    .apply();
            Intent intent = new Intent(ManageComicsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
        });

    } // Kết thúc onCreate

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật số chap từ SharedPreferences sau khi chỉnh ở chi tiết
        for (int i = 0; i < comicList.size(); i++) {
            Comic c = comicList.get(i);
            int count = getSharedPreferences("chapters", MODE_PRIVATE)
                    .getInt(c.getTitle() + "_chapter_count", c.getChapters());
            c.setChapters(count);
        }
        comicAdapter.notifyDataSetChanged();
    }

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
