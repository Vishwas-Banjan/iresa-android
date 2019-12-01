package com.vbanjan.iresa.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    String storeID = null;
    private static final String TAG = "demo";
    ArrayList<Song> songList = new ArrayList<>();
    ProgressDialog progressDialog;
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
        storeID = getArguments().get("storeDocID").toString();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting list of songs...");
        progressDialog.setCancelable(false);

        firestoreListener();
        return inflater.inflate(R.layout.fragment_songs_list, container, false);
    }

    private void firestoreListener() {
        if (storeID != null) {
            progressDialog.show();
            db.collection("stations").document(storeID).collection("songList").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        if (doc.get("name") != null) {
                            ArrayList<HashMap<String, String>> artistsMap = (ArrayList<HashMap<String, String>>) doc.get("artists");
                            for (HashMap<String, String> artist : artistsMap) {
                                artistList.add(new Artist(artist.get("name")));
                            }
                            ArrayList<HashMap<String, String>> imagesList = (ArrayList<HashMap<String, String>>) doc.get("images");
                            ArrayList<HashMap<String, Long>> imagesSizeList = (ArrayList<HashMap<String, Long>>) doc.get("images");
                            Song song = new Song(artistList, doc.getId(),
                                    doc.getString("id"), doc.getString("name"),
                                    imagesList.get(0).get("url"), doc.getString("uri"),
                                    Long.valueOf(imagesSizeList.get(0).get("height")), Long.valueOf(imagesSizeList.get(0).get("width")));
                            songList.add(song);
                        }
                    }
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                }
            });
        }
    }
}
