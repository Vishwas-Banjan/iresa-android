package com.vbanjan.iresa.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vbanjan.iresa.Model.Artist;
import com.vbanjan.iresa.Model.Song;
import com.vbanjan.iresa.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SongsListFragment extends Fragment {

    String storeID;
    private static final String TAG = "demo";
    ArrayList<Song> songList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SongsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storeID = getArguments().get("barCodeValue").toString();
        Log.d("demo", "onCreateView: " + storeID);
        firestoreListener();
        return inflater.inflate(R.layout.fragment_songs_list, container, false);
    }

    private void firestoreListener() {
        db.collection("stations").document("qqwI4d1wxesYV64Jmwur").collection("songList").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ArrayList<Artist> artistList = new ArrayList<>();
                    if (doc.get("name") != null) {
                        ArrayList<HashMap<String, String>> artistsMap = (ArrayList<HashMap<String, String>>) doc.get("artists");
                        for (HashMap<String, String> artist : artistsMap) {
                            artistList.add(new Artist(artist.get("name")));
                        }
                        Song song = new Song();
                        song.setArtists(artistList);
                        song.setDocumentID(doc.getId());
                        song.setSongID(doc.getString("id"));
                        song.setSongTitle(doc.getString("name"));
                        ArrayList<HashMap<String, String>> imagesList = (ArrayList<HashMap<String, String>>) doc.get("images");
                        ArrayList<HashMap<String, Long>> imagesSizeList = (ArrayList<HashMap<String, Long>>) doc.get("images");
                        song.setImageURL(imagesList.get(0).get("url"));
                        song.setImageHeight(Long.valueOf(imagesSizeList.get(0).get("height")));
                        song.setImageWidth(Long.valueOf(imagesSizeList.get(0).get("width")));
                        song.setSongURI(doc.getString("uri"));
                        songList.add(song);
                    }
                }
            }
        });
    }


}
