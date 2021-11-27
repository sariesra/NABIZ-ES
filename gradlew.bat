package com.example.es;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;



import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CameraActivity extends AppCompatActivity {
    public String usermail1;

    private static final String TAG = "CameraActivity";
    private TextureView textureView; //TextureView to deploy camera data
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    // Thread handler member variables
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    //Heart rate detector member variables
    public static int hrtratebpm;
    private int mCurrentRollingAverage;
    private int mLastRollingAverage;
    private int mLastLastRollingAverage;
    private long [] mTimeArray;
    private int numCaptures = 0;
    private int mNumBeats = 0;

    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
