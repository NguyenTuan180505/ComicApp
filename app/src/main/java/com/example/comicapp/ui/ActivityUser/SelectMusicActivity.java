package com.example.comicapp.ui.ActivityUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.service.MusicService;

import java.util.HashMap;
import java.util.Map;

public class SelectMusicActivity extends AppCompatActivity {

    public static String currentSongName = "";
    public static boolean isSongPlaying = false;

    private Map<String, Integer> songResourceMap = new HashMap<>();
    private LinearLayout miniPlayer;
    private TextView tvCurrentSong;
    private ImageButton btnPlayPause;
    private ImageView imgMiniCover; // THÊM ẢNH MINI PLAYER

    // TẤT CẢ BÀI HÁT + ẢNH
    private final SongData[] binhyenSongs = {
            new SongData("Lofi Mưa", "Various Artists", R.drawable.img_trucnhan),
            new SongData("Tháng Năm Bình Yên", "Bích Phương", R.drawable.img_tuanhung),
            new SongData("Chờ Anh Về", "Minh Tuyết", R.drawable.img_trinhdinhquang),
            new SongData("Gió Nhẹ Thôi", "Hồ Quang Hiếu", R.drawable.img_sontung),
            new SongData("Miên Man", "Orange", R.drawable.img_trucnhan)
    };

    private final SongData[] buonSongs = {
            new SongData("Thất Tình", "Trịnh Đình Quang", R.drawable.img_trinhdinhquang),
            new SongData("Vì Người Không Xứng Đáng", "Duy Mạnh", R.drawable.img_sontung),
            new SongData("Một Mình", "Karik", R.drawable.img_trucnhan),
            new SongData("Nhớ Em", "Tuấn Hưng", R.drawable.img_tuanhung),
            new SongData("Trời Mưa Buồn", "Hồ Ngọc Hà", R.drawable.img_trinhdinhquang),
            new SongData("Cơn Gió Lạnh", "Mỹ Tâm", R.drawable.img_sontung),
            new SongData("Xa", "Đông Nhi", R.drawable.img_trucnhan)
    };

    private final SongData[] vuiSongs = {
            new SongData("Lạc Trôi", "Sơn Tùng M-TP", R.drawable.img_sontung),
            new SongData("Không Ai", "Orange", R.drawable.img_trucnhan),
            new SongData("Cười", "Hồ Quang Hiếu", R.drawable.img_tuanhung),
            new SongData("Ngày Hôm Nay Vui Quá", "Đen Vâu", R.drawable.img_trinhdinhquang),
            new SongData("Đi Đu Đưa Đi", "Bích Phương", R.drawable.img_sontung),
            new SongData("Bay", "AMEE", R.drawable.img_trucnhan),
            new SongData("Vở kịch của em", "Hương Tràm", R.drawable.img_tuanhung)
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_nhac);

        // Find views
        miniPlayer = findViewById(R.id.miniPlayer);
        tvCurrentSong = findViewById(R.id.tvCurrentSong);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        imgMiniCover = findViewById(R.id.imgMiniCover); // THÊM DÒNG NÀY
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        initSongMap();
        setupSuggestedSongs();

        // 2 cột đẹp như hình bạn chụp
        setupMoodWithGrid(R.id.btnBinhYen, R.id.listBinhYen, binhyenSongs);
        setupMoodWithGrid(R.id.btnBuon, R.id.listBuon, buonSongs);
        setupMoodWithGrid(R.id.btnVui, R.id.listVui, vuiSongs);

        // NÚT PLAY/PAUSE TRONG MINI PLAYER
        btnPlayPause.setOnClickListener(v -> {
            Intent intent = new Intent(this, MusicService.class);
            intent.setAction("TOGGLE_PLAY_PAUSE"); // Gửi action để service biết
            startService(intent);

            new Handler().postDelayed(() -> {
                isSongPlaying = MusicService.mediaPlayer != null && MusicService.mediaPlayer.isPlaying();
                btnPlayPause.setImageResource(isSongPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            }, 100);
        });
    }

    private void initSongMap() {
        songResourceMap.put("Thất Tình", R.raw.mc_thattinh);
        songResourceMap.put("Vì Người Không Xứng Đáng", R.raw.mc_vinguoikhongxungdang);
        songResourceMap.put("Lạc Trôi", R.raw.mc_lactroi);
        songResourceMap.put("Không Ai", R.raw.mc_damcuoichuot);

        songResourceMap.put("Lofi Mưa", R.raw.mc_lofimua);
        songResourceMap.put("Tháng Năm Bình Yên", R.raw.mc_thangnambinhyen);
        songResourceMap.put("Chờ Anh Về", R.raw.mc_choanhve);
        songResourceMap.put("Gió Nhẹ Thôi", R.raw.mc_gionhethoi);
        songResourceMap.put("Miên Man", R.raw.mc_mienman);
        songResourceMap.put("Một Mình", R.raw.mc_motminh);
        songResourceMap.put("Nhớ Em", R.raw.mc_nhoem);
        songResourceMap.put("Trời Mưa Buồn", R.raw.mc_troimuabuon);
        songResourceMap.put("Cơn Gió Lạnh", R.raw.mc_congiolanh);
        songResourceMap.put("Xa", R.raw.mc_xa);
        songResourceMap.put("Ngày Hôm Nay Vui Quá", R.raw.mc_ngayhomnayvuiqua);
        songResourceMap.put("Đi Đu Đưa Đi", R.raw.mc_diduduadi);
        songResourceMap.put("Cười", R.raw.mc_cuoi);
        songResourceMap.put("Bay", R.raw.mc_bay);
        songResourceMap.put("Vở kịch của em", R.raw.mc_vokichcuaem);
    }

    private void setupSuggestedSongs() {
        RecyclerView rv = findViewById(R.id.rvSuggested);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        SongData[] goiY = {
                new SongData("Lạc Trôi", "Sơn Tùng M-TP", R.drawable.img_sontung),
                new SongData("Thất Tình", "Trịnh Đình Quang", R.drawable.img_trinhdinhquang),
                new SongData("Lofi Mưa", "Various", R.drawable.img_trucnhan),
                new SongData("Cười", "Hồ Quang Hiếu", R.drawable.img_tuanhung),
                new SongData("Không Ai", "Orange", R.drawable.img_sontung)
        };

        rv.setAdapter(new SuggestedAdapter(goiY));
    }

    private void setupMoodWithGrid(int btnId, int containerId, SongData[] songs) {
        Button btn = findViewById(btnId);
        LinearLayout container = findViewById(containerId);

        btn.setOnClickListener(v -> {
            if (container.getVisibility() == View.GONE) {
                container.removeAllViews();

                RecyclerView rv = new RecyclerView(this);
                rv.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                rv.setPadding(8, 16, 8, 32);
                rv.setClipToPadding(false);
                rv.setLayoutManager(new GridLayoutManager(this, 2));
                rv.setAdapter(new MoodGridAdapter(songs));

                container.addView(rv);
                container.setVisibility(View.VISIBLE);
            } else {
                container.setVisibility(View.GONE);
            }
        });
    }

    // HÀM CHÍNH – KHI BẤM VÀO BÀI HÁT
    private void playSong(String title) {
        Integer resId = songResourceMap.get(title);
        if (resId == null) return;

        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("resId", resId);
        startService(intent);

        currentSongName = title;
        isSongPlaying = true;

        // Cập nhật tên bài hát
        tvCurrentSong.setText(title);

        // TÌM ẢNH ĐÚNG CỦA BÀI HÁT ĐÓ
        int coverRes = R.drawable.img_sontung; // mặc định
        for (SongData s : binhyenSongs) if (s.title.equals(title)) { coverRes = s.coverRes; break; }
        for (SongData s : buonSongs)     if (s.title.equals(title)) { coverRes = s.coverRes; break; }
        for (SongData s : vuiSongs)      if (s.title.equals(title)) { coverRes = s.coverRes; break; }

        imgMiniCover.setImageResource(coverRes);

        // Hiện mini player + đổi nút thành pause
        miniPlayer.setVisibility(View.VISIBLE);
        btnPlayPause.setImageResource(R.drawable.ic_pause);
    }

    // Adapter gợi ý ngang
    class SuggestedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final SongData[] data;
        SuggestedAdapter(SongData[] d) { this.data = d; }
        @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p, int vt) {
            View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_song_suggestion, p, false);
            return new RecyclerView.ViewHolder(v) {};
        }
        @Override public void onBindViewHolder(RecyclerView.ViewHolder h, int pos) {
            SongData s = data[pos];
            ((ImageView) h.itemView.findViewById(R.id.imgSongCover)).setImageResource(s.coverRes);
            ((TextView) h.itemView.findViewById(R.id.tvSongTitle)).setText(s.title);
            h.itemView.setOnClickListener(v -> playSong(s.title));
        }
        @Override public int getItemCount() { return data.length; }
    }

    // Adapter mood 2 cột
    class MoodGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final SongData[] data;
        MoodGridAdapter(SongData[] d) { this.data = d; }
        @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p, int vt) {
            View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_song_suggestion, p, false);
            return new RecyclerView.ViewHolder(v) {};
        }
        @Override public void onBindViewHolder(RecyclerView.ViewHolder h, int pos) {
            SongData s = data[pos];
            ((ImageView) h.itemView.findViewById(R.id.imgSongCover)).setImageResource(s.coverRes);
            ((TextView) h.itemView.findViewById(R.id.tvSongTitle)).setText(s.title);
            h.itemView.setOnClickListener(v -> playSong(s.title));
        }
        @Override public int getItemCount() { return data.length; }
    }

    static class SongData {
        String title, artist;
        int coverRes;
        SongData(String t, String a, int c) {
            title = t; artist = a; coverRes = c;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!currentSongName.isEmpty()) {
            tvCurrentSong.setText(currentSongName);
            imgMiniCover.setImageResource(getCoverFromTitle(currentSongName));
            btnPlayPause.setImageResource(isSongPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            miniPlayer.setVisibility(View.VISIBLE);
        }
    }

    // Helper tìm ảnh theo tên bài
    private int getCoverFromTitle(String title) {
        for (SongData s : binhyenSongs) if (s.title.equals(title)) return s.coverRes;
        for (SongData s : buonSongs)     if (s.title.equals(title)) return s.coverRes;
        for (SongData s : vuiSongs)      if (s.title.equals(title)) return s.coverRes;
        return R.drawable.img_sontung;
    }
}