package com.vbanjan.iresa.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vbanjan.iresa.R;

import java.io.IOException;

public class ScanCodeFragment extends Fragment {

    private static final String TAG = "demo";
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    NavController navController;

    public ScanCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        surfaceView = view.findViewById(R.id.cameraSurfaceView);
        barcodeDetector = new BarcodeDetector.Builder(getActivity().getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();

        if (!barcodeDetector.isOperational()) {
            Log.d("demo", "onViewCreated: Barcode Detector not operational!");
            return;
        }

        cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();

        setCameraToSurfaceView();
        readBarcodeValue();

    }

    private void readBarcodeValue() {
        final Bundle bundle = new Bundle();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!bundle.get("barCodeValue").equals("") || bundle.get("barCodeValue") != null)
                            navController.navigate(R.id.action_scanCodeFragment_to_songsListFragment, bundle);
                    }
                });
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    bundle.putString("barCodeValue", qrCodes.valueAt(0).displayValue);
                    vibratePhone(); //Vibrate phone on detection
                    release();
                } else {
                    Log.d("demo", "receiveDetections: No Value detected");
                }
            }
        });
    }

    private void vibratePhone() {
        Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100); //Vibrate phone on detection
    }

    private void setCameraToSurfaceView() {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }
}
