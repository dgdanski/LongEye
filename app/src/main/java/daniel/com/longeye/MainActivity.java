package daniel.com.longeye;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

public class MainActivity extends AppCompatActivity {

    private Button runButton;
    public Camera camera = null;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;
    MediaPlayer mp;
    private static int cameraId = 0;
    public static CameraPreview mPreview;
    public static LinearLayout preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        startCamera();

        runButton = (Button) findViewById(R.id.aparat);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "This is my Toast message!",
//                        Toast.LENGTH_LONG).show();
                //openCamera();

                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void openCamera() {

/*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE_SECURE");
                startActivity(intent);
            }
        }, 5000);
*/
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE_SECURE");
        startActivity(intent);

        turnOnFlash();




    }

    // getting camera parameters
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
                Toast.makeText(getApplicationContext(), "This is my Toast message!", Toast.LENGTH_LONG).show();
            }
        }
    }
/*
    private void turnOnFlash() {
        getCamera();
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            //playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            //toggleButtonImage();
        }

    }
*/

    private void startCamera() {

        Log.e("Cam","Inside doInBackground");
        String msg = "";
        // Do we have a camera?
        try {
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            } else {
                cameraId = CAMERA_FACING_BACK;//A function that returns 0 for front camera
                if (cameraId < 0) {

                } else {

                    try {
                        //camera = Camera.open(cameraId);//opens front cam
                        camera = Camera.open(); //when I use this I can on/off the flashlight,since I am calling the back camera.
                        params = camera.getParameters();
                        Log.e("Cam","camera id " + cameraId);
                        Log.e("Cam","params " + params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        mPreview = new CameraPreview(getApplicationContext(), camera);
//                        mPreview = new CameraPreview(this, camera);
                        preview = (LinearLayout) findViewById(R.id.content_main);
                        preview.addView(mPreview);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("layout", preview); musze przekazac ten preview do activity
                        startActivity();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    private void turnOnFlash() {
        Log.v("Cam","Inside turnOnFlash");
        if (!isFlashOn) {
                if (camera == null || params == null) {
                Log.v("Cam","camera or param is null");
                return;
            }

            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            //toggleButtonImage();
        }

    }

    /*
     * Turning Off flash
     */
    private void turnOffFlash() {
        Log.v("Cam","Inside turnOffFlash");
        if (isFlashOn) {
            if (camera == null || params == null) {
                Log.v("Cam","camera or param is null");
                return;
            }
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            //toggleButtonImage();
        }
    }
}
