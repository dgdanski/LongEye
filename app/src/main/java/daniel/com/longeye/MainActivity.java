package daniel.com.longeye;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Build;
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

    private static int cameraId = 0;
    public static CameraPreview mPreview;
    public static LinearLayout preview;
    public static Button buttonPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("API","wersja " + Build.VERSION.SDK_INT);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        latający guzik
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

//        uruchamiam kamerę
        startCamera();

        /*
        runButton = (Button) findViewById(R.id.aparat);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
        */
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    /*
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
    */


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
                //Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
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

        try {
//            sprawdzenie czy urządzenie posiada kamerę
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            } else {
//                odtąd obsługujemy kamerę z tyłu

                cameraId = CAMERA_FACING_BACK;//A function that returns 0 for front camera
                if (cameraId < 0) {
                    Toast.makeText(getApplicationContext(), "id kamery < 0", Toast.LENGTH_LONG).show();
                } else {

                    //samo uruchomienie kamery
                    try {
                        Log.e("Cam","wytropilem");
                        //camera = Camera.open(cameraId);//opens front cam
                        camera = Camera.open(); //when I use this I can on/off the flashlight,since I am calling the back camera.
                        Log.e("Cam","tropienie bledu");
//                        Returns the current settings for this Camera service. If modifications
//                        are made to the returned Parameters, they must be passed to setParameters(Camera.Parameters) to take effect.
                        params = camera.getParameters();
                        camera.setDisplayOrientation(90);

                        Log.e("Cam","camera id " + cameraId);
                        Log.e("Cam","params " + params);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //podanie obrazu z kamery na wyswietlacz
                    try {
                        View view;
                        Button button;
                        mPreview = new CameraPreview(this, camera);
                        preview = (LinearLayout) findViewById(R.id.main);


                        preview.addView(mPreview);

                        /*
                        mPreview = new CameraPreview(getApplicationContext(), camera);
//                        mPreview = new CameraPreview(this, camera);
                        preview = (LinearLayout) findViewById(R.id.content_main);
                        preview.addView(mPreview);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

//                        musze jakos przekazac preview do activity
                        intent.putExtra("layout", R.layout.content_main);

                        startActivity(intent);
                        */
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("CATCH","camera.open nie dziala");
                    }
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            Log.e("CATCH","urzadzenie nie posiada kamery");
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
//            camera.stopPreview();
            isFlashOn = false;
            //startCamera();

        }
    }

    public void akcja(View v){
//        Toast.makeText(getApplicationContext(), "This is my Toast message!", Toast.LENGTH_LONG).show();
        if (isFlashOn) {
            turnOffFlash();
        } else {
            turnOnFlash();
        }
    }
}
