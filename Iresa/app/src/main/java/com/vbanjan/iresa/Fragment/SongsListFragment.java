package com.vbanjan.iresa.Fragment;


import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vbanjan.iresa.Model.Artist;
import com.vbanjan.iresa.Model.Song;
import com.vbanjan.iresa.Adapter.SongListRecyclerViewAdapter;
import com.vbanjan.iresa.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SongsListFragment extends Fragment {

    String storeID = null;
    private static final String TAG = "demo";
    ArrayList<Song> songList = new ArrayList<>();
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ImageView nowPlayingSongArt;
    TextView nowPlayingSongTitle, nowPlayingSongArtists;
    private ProgressBar nowPlayingProgressBar;

    public SongsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.songListRecyclerView);
        nowPlayingSongArt = view.findViewById(R.id.nowPlayingSongArtImageView);
        nowPlayingSongTitle = view.findViewById(R.id.nowPlayingSongTitleTextView);
        nowPlayingSongArtists = view.findViewById(R.id.nowPlayingSongArtistsTextView);
        nowPlayingProgressBar = view.findViewById(R.id.nowPlayingProgressBar);

        songListListener();
        nowPlayingListener();
    }

    public void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SongListRecyclerViewAdapter(songList, storeID, getContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storeID = getArguments().get("storeDocID").toString();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting list of songs...");
        progressDialog.setCancelable(false);
        return inflater.inflate(R.layout.fragment_songs_list, container, false);
    }

    private void songListListener() {
        if (storeID != null) {
            progressDialog.show();
            db.collection("stations").document(storeID).collection("songList")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                Log.w(TAG, "Listen failed.", e);
                                Toast.makeText(getContext(), "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    //To avoid fetching the list and refreshing when Up-Voted or document modified
                                    case ADDED:
                                    case REMOVED:
                                        songList.clear();
                                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                            ArrayList<Artist> artistList = new ArrayList<>();
                                            ArrayList<String> upvoteList = new ArrayList<>();
                                            if (doc.get("name") != null) {
                                                ArrayList<HashMap<String, String>> artistsMap = (ArrayList<HashMap<String, String>>) doc.get("artists");
                                                for (HashMap<String, String> artist : artistsMap) {
                                                    artistList.add(new Artist(artist.get("name")));
                                                }
                                                if (doc.get("upvotes") != null) {
                                                    upvoteList.addAll((ArrayList<String>) doc.get("upvotes"));
                                                }
                                                ArrayList<HashMap<String, String>> imagesList = (ArrayList<HashMap<String, String>>) doc.get("images");
                                                ArrayList<HashMap<String, Long>> imagesSizeList = (ArrayList<HashMap<String, Long>>) doc.get("images");
                                                Song song = new Song(artistList, doc.getId(),
                                                        doc.getString("id"), doc.getString("name"),
                                                        imagesList.get(0).get("url"), doc.getString("uri"),
                                                        Long.valueOf(imagesSizeList.get(0).get("height")), Long.valueOf(imagesSizeList.get(0).get("width")),
                                                        upvoteList);
                                                songList.add(song);
                                            }
                                        }
                                        setUpRecyclerView();
                                        if (progressDialog.isShowing()) progressDialog.dismiss();
                                        break;
                                }
                            }

                        }
                    });
        }
    }

    private void nowPlayingListener() {
        if (storeID != null) {
            db.collection("stations").document(storeID).collection("queue")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                Log.w(TAG, "Listen failed.", e);
                                Toast.makeText(getContext(), "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                ArrayList<Artist> artistList = new ArrayList<>();
                                ArrayList<String> upvoteList = new ArrayList<>();
                                if (doc.get("name") != null) {
                                    ArrayList<HashMap<String, String>> artistsMap = (ArrayList<HashMap<String, String>>) doc.get("artists");
                                    for (HashMap<String, String> artist : artistsMap) {
                                        artistList.add(new Artist(artist.get("name")));
                                    }
                                    if (doc.get("upvotes") != null) {
                                        upvoteList.addAll((ArrayList<String>) doc.get("upvotes"));
                                    }
                                    ArrayList<HashMap<String, String>> imagesList = (ArrayList<HashMap<String, String>>) doc.get("images");
                                    ArrayList<HashMap<String, Long>> imagesSizeList = (ArrayList<HashMap<String, Long>>) doc.get("images");
                                    Song song = new Song(artistList, doc.getId(),
                                            doc.getString("id"), doc.getString("name"),
                                            imagesList.get(0).get("url"), doc.getString("uri"),
                                            Long.valueOf(imagesSizeList.get(0).get("height")), Long.valueOf(imagesSizeList.get(0).get("width")),
                                            upvoteList);
                                    setUpNowPlaying(song);
                                }
                            }
                        }
                    });
        }
    }

    private void setUpNowPlaying(Song song) {
        nowPlayingSongTitle.setText(song.getSongTitle());
        StringBuilder stringBuilder = new StringBuilder();
        for (Artist artist : song.getArtists()) {
            stringBuilder.append(artist.getName() + ", ");
        }
        nowPlayingSongArtists.setText(stringBuilder.substring(0, stringBuilder.length() - 2));
        Picasso.get().load(song.getImageURL())
                .into(nowPlayingSongArt, new Callback() {
                    @Override
                    public void onSuccess() {
                        nowPlayingProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        nowPlayingProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}