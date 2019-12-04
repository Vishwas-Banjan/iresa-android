package com.vbanjan.iresa.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vbanjan.iresa.Model.Artist;
import com.vbanjan.iresa.Model.Song;
import com.vbanjan.iresa.R;

import java.util.ArrayList;

public class SongListRecyclerViewAdapter extends RecyclerView.Adapter<SongListRecyclerViewAdapter.ViewHolder> {
    String TAG = "demo";
    ArrayList<Song> songArrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String storeID;
    SharedPreferences getPrefs;
    String uniqueID;

    public SongListRecyclerViewAdapter(ArrayList<Song> songList, String storeID, Context context) {
        this.songArrayList.addAll(songList);
        this.storeID = storeID;
        this.getPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        uniqueID = getPrefs.getString("uniqueID", null);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Song song = songArrayList.get(position);
        holder.songTitle.setText(song.getSongTitle());
        StringBuilder stringBuilder = new StringBuilder();
        for (Artist artist : song.getArtists()) {
            stringBuilder.append(artist.getName() + ", ");
        }
        holder.songArtists.setText(stringBuilder.substring(0, stringBuilder.length() - 2));
        Picasso.get().load(song.getImageURL())
                .into(holder.songArt, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }
                });

        if (song.getUpvotes().contains(uniqueID)) {
            enableHeart(holder.hearFill);
            disableHeart(holder.hearClear);
        } else {
            enableHeart(holder.hearClear);
            disableHeart(holder.hearFill);
        }

        holder.hearClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uniqueID != null) {
                    disableHeart(holder.hearClear);
                    enableHeart(holder.hearFill);
                    db.collection("stations")
                            .document(storeID)
                            .collection("songList")
                            .document(song.getDocumentID())
                            .update("upvotes", FieldValue.arrayUnion(uniqueID));
                }
            }
        });
        holder.hearFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uniqueID != null) {
                    disableHeart(holder.hearFill);
                    enableHeart(holder.hearClear);
                    db.collection("stations")
                            .document(storeID)
                            .collection("songList")
                            .document(song.getDocumentID())
                            .update("upvotes", FieldValue.arrayRemove(uniqueID));
                }
            }
        });
    }

    private void disableHeart(ImageView heart) {
        heart.setVisibility(View.INVISIBLE);
        heart.setEnabled(false);
    }

    private void enableHeart(ImageView heart) {
        heart.setVisibility(View.VISIBLE);
        heart.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, songArtists;
        ImageView songArt, hearClear, hearFill;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.nowPlayingSongTitleTextView);
            songArtists = itemView.findViewById(R.id.songArtistTextView);
            songArt = itemView.findViewById(R.id.nowPlayingSongArtImageView);
            hearClear = itemView.findViewById(R.id.heartClearImageView);
            hearFill = itemView.findViewById(R.id.heartFillImageView);
            progressBar = itemView.findViewById(R.id.songArtProgressBar);
        }
    }

}
