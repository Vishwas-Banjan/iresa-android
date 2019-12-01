package com.vbanjan.iresa.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vbanjan.iresa.Model.Store;
import com.vbanjan.iresa.R;


public class StoreDetailsFragment extends Fragment {
    private static final String TAG = "demo";
    String storeSecretKey;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView storeName, storeAddress;
    Button nextButton;
    private ProgressDialog progressDialog;
    NavController navController;

    public StoreDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storeName = view.findViewById(R.id.storeNameTextView);
        storeAddress = view.findViewById(R.id.storeAddressTextView);
        nextButton = view.findViewById(R.id.nextButton);
        navController = Navigation.findNavController(view);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting store details...");
        progressDialog.setCancelable(false);

        if (getArguments().getString("storeSecretKey") != null) {
            storeSecretKey = getArguments().getString("storeSecretKey");
            fetchStoreDetails();
            progressDialog.show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_store_details, container, false);
    }

    private void fetchStoreDetails() {
        if (storeSecretKey != null) {
            db.collection("stations")
                    .whereEqualTo("secretCode", storeSecretKey)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Store store = null;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    store = new Store(document.getString("name"),
                                            document.getString("address"), document.getString("state"),
                                            document.getString("zipCode"), document.getString("city"), document.getId());
                                }
                                if (store != null)
                                    showStoreDetails(store);
                            } else {
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                Toast.makeText(getContext(), "Oops! Something's not right", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Oops! Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showStoreDetails(final Store store) {
        storeName.setText(store.getStoreName());
        storeAddress.setText(store.getStoreStreet() + ", " + store.getStoreCity() + ", " + store.getStoreState() + ", " + store.getStoreZip());
        if (progressDialog.isShowing()) progressDialog.dismiss();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("storeDocID", store.getStoreDocumentID());
                navController.navigate(R.id.action_storeDetailsFragment_to_songsListFragment, bundle);
            }
        });

    }

}
