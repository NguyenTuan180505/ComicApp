// ChapterAdapterUser.java
package com.example.comicapp.data.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.api.UnlockChapterApi;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.ui.ActivityUser.ReadComicActivity;
import com.example.comicapp.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterAdapterUser extends RecyclerView.Adapter<ChapterAdapterUser.ViewHolder> {

    private final List<Chapter> chapterList;
    private final Context context;
    private final Story story;
    private final UnlockChapterApi unlockApi;

    private static final int UNLOCK_COST = 30; // Số điểm cần để mở khóa

    public ChapterAdapterUser(Context context, List<Chapter> chapters, Story story) {
        this.context = context;
        this.chapterList = chapters;
        this.story = story;
        this.unlockApi = RetrofitClient.getInstance().create(UnlockChapterApi.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);

        // Hiển thị tiêu đề chương
        String title = chapter.getTitle() != null && !chapter.getTitle().isEmpty()
                ? chapter.getTitle()
                : "Chương " + chapter.getChapterNumber();

        title += chapter.isLocked() ? " (Chưa mở)" : " (Đã mở)";
        holder.tvChapterName.setText(title);

        // Làm mờ nếu bị khóa (theo dữ liệu local)
        holder.itemView.setAlpha(chapter.isLocked() ? 0.5f : 1.0f);

        // Ngày upload
        holder.tvUploadDate.setText(formatDate(chapter.getCreatedAt()));
        holder.tvPageCount.setVisibility(View.GONE);

        // Xử lý click
        holder.itemView.setOnClickListener(v -> {
            if (!chapter.isLocked()) {
                // Local nói đã mở → tin tưởng và vào đọc luôn
                openChapter(chapter);
                return;
            }

            // Local nói khóa → kiểm tra thực tế từ server
            checkUnlockStatusAndHandle(chapter);
        });
    }

    private void checkUnlockStatusAndHandle(Chapter chapter) {
        String token = "Bearer " + SessionManager.getToken(context);

        unlockApi.isUnlocked(token, chapter.getId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isUnlocked = response.body();

                    if (isUnlocked) {
                        // Đã mở khóa trước đó → cập nhật UI và đọc luôn
                        chapter.setLocked(false);
                        notifyItemChanged(chapterList.indexOf(chapter));
                        Toast.makeText(context, "Chương đã được bạn mở khóa trước đó!", Toast.LENGTH_SHORT).show();
                        openChapter(chapter);
                    } else {
                        // Chưa mở → hỏi dùng điểm để mở
                        showUnlockConfirmDialog(chapter);
                    }
                } else {
                    Toast.makeText(context, "Không kiểm tra được trạng thái chương", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối khi kiểm tra chương", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUnlockConfirmDialog(Chapter chapter) {
        new AlertDialog.Builder(context)
                .setTitle("Mở khóa chương")
                .setMessage("Chương này chưa được mở khóa.\nBạn có muốn dùng " + UNLOCK_COST + " điểm để mở ngay không?")
                .setPositiveButton("Mở khóa (" + UNLOCK_COST + " điểm)", (dialog, which) -> {
                    performUnlock(chapter);
                })
                .setNegativeButton("Hủy", null)
                .setCancelable(true)
                .show();
    }

    private void performUnlock(Chapter chapter) {
        String token = "Bearer " + SessionManager.getToken(context);

        unlockApi.unlockChapter(token, chapter.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Mở khóa thành công!", Toast.LENGTH_LONG).show();

                    // Cập nhật trạng thái local
                    chapter.setLocked(false);
                    notifyItemChanged(chapterList.indexOf(chapter));

                    // Vào đọc chương ngay
                    openChapter(chapter);

                } else {
                    String msg = "Không thể mở khóa chương";
                    if (response.code() == 400 || response.code() == 403) {
                        msg = "Bạn không đủ điểm để mở khóa!";
                    } else if (response.code() == 409) {
                        msg = "Chương đã được mở khóa rồi";
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openChapter(Chapter chapter) {
        Intent intent = new Intent(context, ReadComicActivity.class);
        intent.putExtra("storyId", story.getId());
        intent.putExtra("chapterNumber", chapter.getChapterNumber());
        if (chapter.getId() != null) {
            intent.putExtra("chapterId", chapter.getId());
        }
        if (story != null && story.getTitle() != null) {
            intent.putExtra(ReadComicActivity.EXTRA_COMIC_TITLE, story.getTitle());
        }
        if (chapter.getTitle() != null) {
            intent.putExtra(ReadComicActivity.EXTRA_CHAPTER_TITLE, chapter.getTitle());
        }
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return chapterList != null ? chapterList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterName, tvPageCount, tvUploadDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterName = itemView.findViewById(R.id.tvChapterName);
            tvPageCount = itemView.findViewById(R.id.tvPageCount);
            tvUploadDate = itemView.findViewById(R.id.tvUploadDate);
        }
    }

    private String formatDate(String isoDate) {
        if (isoDate == null) return "Vừa xong";
        try {
            String[] parts = isoDate.split("T");
            String date = parts[0];
            String[] dateParts = date.split("-");
            return dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
        } catch (Exception e) {
            return "Vừa xong";
        }
    }
}