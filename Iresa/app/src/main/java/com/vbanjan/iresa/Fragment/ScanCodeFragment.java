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
import android.widget.Toast;

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
    private onScanCodeFragmentListener mListener;

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
        surfaceView = view.findViewById(R.id.cameraSurfaceView);
        navController = Navigation.findNavController(view);
        barcodeDetector = new BarcodeDetector.Builder(getActivity().getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();

        if (!barcodeDetector.isOperational()) {
            Log.d("demo", "onViewCreated: Barcode Detector not operational!");
            Toast.makeText(getContext(), "Oops! Something went wrong!", Toast.LENGTH_SHORT).show();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!bundle.get("storeSecretKey").equals("") || bundle.get("storeSecretKey") != null) {
                            if (navController.getCurrentDestination().getId() == R.id.scanCodeFragment) {
                                mListener.navigateScanCodeToStoreDetail(bundle);
                            }
                        } else {
                            Log.d(TAG, "run: Bundle Empty");
                        }
                    }
                });
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    Log.d(TAG, "receiveDetections: " + qrCodes.valueAt(0).displayValue);
                    bundle.putString("storeSecretKey", qrCodes.valueAt(0).displayValue);
                    vibratePhone(); //Vibrate phone on detection
                    release();
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onScanCodeFragmentListener) {
            mListener = (onScanCodeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onScanCodeFragmentListener {
        void navigateScanCodeToStoreDetail(Bundle bundle);
    }
}
