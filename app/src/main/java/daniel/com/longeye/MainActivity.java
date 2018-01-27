package daniel.com.longeye;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

public class MainActivity extends AppCompatActivity {

    private Camera camera = null;
    private Camera.Parameters parameters = null;
    private int cameraId;
    private boolean isFlashOn;
    private CameraPreview cameraPreview = null;

    private LinearLayout linearLayout = null;

    private View decorView;
    private int uiOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        //hide the status bar
        uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //does device has a camera?
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("IF", "Device has no camera");
        }else {
            //use back camera
            cameraId = CAMERA_FACING_BACK;

            useCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onStop() {
        super.onStop();
        turnOffFlash();
    }

    private void useCamera() {

        turnOnCamera();
        passViewOnScreen();
    }

    private void turnOnCamera(){
        try {

            //in case of problem with light switching - try to remove cameraId
            camera = Camera.open(cameraId);

            parameters = camera.getParameters();
            camera.setDisplayOrientation(90);

            turnOnAutoFocus();

            Log.e("Cam", "camera id " + cameraId);
            Log.e("Cam", "params " + parameters);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CATCH", "Camera running failed");
        }
    }

    private void turnOnAutoFocus(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 250ms
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(parameters);
            }
        }, 250);
    }

    private void passViewOnScreen(){
        try {

            cameraPreview = new CameraPreview(this, camera);
            linearLayout = (LinearLayout) findViewById(R.id.main);
            linearLayout.addView(cameraPreview);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CATCH", "Passing view from camera failed");
        }
    }

    public void ruleFlash(View v) {
        if (isFlashOn) {
            turnOffFlash();
        } else {
            turnOnFlash();
        }
    }

    private void turnOnFlash() {

        if (!isFlashOn) {
            if (camera == null || parameters == null) {
                Log.e("Cam", "camera or parameters is null");
                return;
            }

            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    private void turnOffFlash() {

        if (isFlashOn) {
            if (camera == null || parameters == null) {
                Log.e("Cam", "camera or param is null");
                return;
            }
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            isFlashOn = false;
        }
    }
}
