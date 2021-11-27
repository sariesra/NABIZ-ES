package com.example.nabiz_es;


import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.support.v4.content.res.ResourcesCompat;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;
import android.hardware.camera2.CameraDevice;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.support.v4.app.Fragment;
import android.os.HandlerThread;
import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
;import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class Fragment1 extends Fragment{

    protected CaptureRequest.Builder captureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSessions;
    private HandlerThread mBackgroundThread;
    protected CameraDevice cameraDevice;
    private Handler mBackgroundHandler;
    private TextureView textureView;
    private ProgressBar progressBar;
    private Size imageDimension;
    private String cameraId;
    TextView biliyormusunz;
    Date date_baslangic;
    Drawable drawable;
    Date date_bitis;
    TextView tv;
    TextView bpm;
    double[] reds;
    double diff;
    View view;

    private static final int REQUEST_CAMERA_PERMISSION  = 1;
    private static final String TAG                     = "CameraActivity";

    private int numCaptures                             = 0;
    private int mNumBeats                               = 0;
    Timer myTimer;
    private List<String> metinler                       = new ArrayList<>();

    int is_sayisi=4;
    int dead_yil;
    int dead_ay;
    int dead_gun;

    // String[][] isparcalari = new String[20][4];
    veri_kaynagi vk;


    @Nullable
    @Override
    public View  onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {

        vk                      =new veri_kaynagi(getActivity());
        vk.ac();//veritabanı baglantısı acık simdi baglancaz

        List<olcum> akıslar     = vk.listele();


        view                    = inflater.inflate(R.layout.layout_bir,container,false);

        textureView             = (TextureView) view.findViewById(R.id.texture);
        assert textureView     != null;

        textureView.setSurfaceTextureListener(textureListener);

        reds                    = new double[120];//120 frame 10 sn gibi
        tv                      = (TextView) view.findViewById(R.id.nabiz_deger_kutusu);
        bpm                     = (TextView)view.findViewById(R.id.bpm_textview);
        progressBar             = (ProgressBar)view. findViewById(R.id.progressBar);
        biliyormusunz           = (TextView)view.findViewById(R.id.textView_bilgi_meti);

        numCaptures             = 0;
        mNumBeats               = 0;
        metinler.add( this.getResources().getString(R.string.metin1) );
        metinler.add( this.getResources().getString(R.string.metin2) );
        metinler.add( this.getResources().getString(R.string.metin3) );
        metinler.add( this.getResources().getString(R.string.metin4) );
        metinler.add( this.getResources().getString(R.string.metin5) );

        Random rand             = new Random();
        int n                   = rand.nextInt(5);
        biliyormusunz.setText(metinler.get((int)n).toString());

        return  view;

    }


    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {


            Bitmap bmp          = textureView.getBitmap();
            int width           = bmp.getWidth();
            int height          = bmp.getHeight();
            int[] pixels        = new int[height * width];

            bmp.getPixels(pixels, 0, width, 0 , 0 , width , height );

            double sum             = 0;
            double sum_green          = 0;
            double sum_blue           = 0;

            for (int i = 0; i < height * width; i++) {

                double red         = (pixels[i] >> 16) & 0xFF;
                sum             = sum + red;

                double green        =  (pixels[i] >> 8) & 0xff;
                sum_green        = sum_green + green;

                double blue        =  (pixels[i]) & 0xff;
                sum_blue         = sum_blue + green;
            }


            double luminance = (sum * 0.2126f + sum_green * 0.7152f + sum_blue * 0.0722f) / 255;
            Log.d(TAG, "luminance: "+luminance);
            if (sum_blue+sum_green>sum)
            {
               TextView uyari= (TextView)view.findViewById(R.id.textView3);
               uyari.setText("Parmağınızı kamera merceğini kaplayacak şekilde yerleştirin.");
                numCaptures =0;
                mNumBeats   =0;
                tv.setText("X");
                bpm.setVisibility(View.INVISIBLE);

            }
            else
                {
                    TextView uyari= (TextView)view.findViewById(R.id.textView3);
                    uyari.setText("");
                   /* reds[numCaptures]   =sum;*/
                    reds[numCaptures]   =luminance;
                    if (numCaptures==0)
                    {
                        date_baslangic = new Date();
                        tv.setText("~");
                    }
                    if (numCaptures==119)
                    {
                        date_bitis  = new Date();
                        //1000 milliseconds = 1 second
                        //in milliseconds
                        diff            = date_bitis.getTime() - date_baslangic.getTime();

                        double toplam   = 0;

                        for (  int i=0;i<=119;i++) {toplam = reds[i]+toplam; }

                        //double ortalama  = toplam/120;

                        for (int j=1;j<=118;j++)
                        {
                            if ( ((reds[j]-reds[j-1])<0 &&(reds[j+1]-reds[j])>0)/* ||
                           ((reds[j]-reds[j-1])<0 &&(reds[j+1]-reds[j])>0)*/)
                                /**/
                                /*(reds[j]>ortalama && reds[j-1]<ortalama)||(reds[j]<ortalama && reds[j-1]>ortalama)*/
                                /* reds[j]>ortalama && reds[j-1]>ortalama && reds[j-2]>ortalama && reds[j-3]>ortalama)*/
                                /*(reds[j]>ortalama && reds[j-1]<ortalama)||(reds[j]<ortalama && reds[j-1]>ortalama)*/
                            {
                                mNumBeats++;
                               // Log.d(TAG, "reds[j-1]:"+reds[j-1]+"  reds[j]"+reds[j]+"  reds[j+1]:"+reds[j+1]);
                               /* Log.d(TAG, "(reds[j]-reds[j-1]):"+(reds[j]-reds[j-1])+" (reds[j+1]-reds[j]):"+(reds[j+1]-reds[j]));
                                Log.d(TAG, "mNumBeats "+mNumBeats);*/
                            }
                        }

                        double saniye      = diff/1000;
                        double carpım      = 60/saniye;

                        /*tv.setText("Nabiz:"+(mNumBeats*carpım/2)+" BPM");*/
                        tv.setText(Integer.toString((int)(mNumBeats*carpım/2)));

                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  hh:mm aaa");
                        String strDate = dateFormat.format(date);

                        int is_adi=Integer.parseInt(((TextView)view.findViewById(R.id.nabiz_deger_kutusu)).getText().toString());
                        olcum a=new olcum(is_sayisi,is_adi,strDate);
                        vk.akisolustur(a);
                        is_sayisi++;

                        bpm.setVisibility(View.VISIBLE);
                        double  nabiz=mNumBeats*carpım/2;
                        if ( nabiz<60 ||nabiz>100)
                        {
                            drawable    = ResourcesCompat.getDrawable(getResources(), R.drawable.progress_uyari, null);

                            Rect bounds = progressBar.getIndeterminateDrawable().getBounds();
                            progressBar.setIndeterminateDrawable(drawable);
                            progressBar.getIndeterminateDrawable().setBounds(bounds);


                        }else
                        {
                            drawable    = ResourcesCompat.getDrawable(getResources(), R.drawable.progress, null);

                            Rect bounds = progressBar.getIndeterminateDrawable().getBounds();
                            progressBar.setIndeterminateDrawable(drawable);
                            progressBar.getIndeterminateDrawable().setBounds(bounds);

                        }


                        /*progressBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });*/

                        numCaptures =0;
                        mNumBeats   =0;
                    }
                    numCaptures++;
                }


        }
    };


    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {

            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            if (cameraDevice != null)
                cameraDevice.close();
            cameraDevice = null;
        }
    };

    // onResume
    protected void startBackgroundThread() {
        mBackgroundThread   = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler  = new Handler(mBackgroundThread.getLooper());
    }
    // onPause
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture  = textureView.getSurfaceTexture();
            assert texture          != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface         = new Surface(texture);
            captureRequestBuilder   = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (null == cameraDevice) {
                        return;
                    }
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(getActivity(), "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    // arka kamerayi aciyor
    private void openCamera() {
        CameraManager manager                       = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId                                = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics   = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension                          = map.getOutputSizes(SurfaceTexture.class)[0];
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
    protected void updatePreview() {
        if (null == cameraDevice) {

        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(getActivity(), "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
               /* finish();*/
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
