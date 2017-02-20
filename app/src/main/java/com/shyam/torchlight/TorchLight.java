package com.shyam.torchlight;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by shyam on 2/18/2017.
 */

public class TorchLight {
    private Camera camera;
    Camera.Parameters params;
    MediaPlayer mp;
    private boolean isFlashOn;
    Context context;
    public TorchLight(Context context)
    {
        this.context = context;
        getCamera();
    }
    public boolean isFlashOn() {
        return isFlashOn;
    }

    public void setFlashOn(boolean flashOn) {
        isFlashOn = flashOn;
    }

    /*
    * Turning On flash
    */
    public void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            // toggleButtonImage();
        }

    }

    /*
     * Turning Off flash
     */
    public void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            else{
                getCamera();
            }
            // play sound
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            // toggleButtonImage();
        }
    }

    /*
        * Get the camera
        */
    public void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                if (camera != null) {
                    camera.release();
                    camera = null;
                }
                Log.e("Camera Error", e.getMessage());
            }
        }
    }
    public void releaseCamera(){
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    /*
    * Playing sound
    * will play button toggle sound on flash on / off
    * */
    public void playSound(){
        if(isFlashOn){
            mp = MediaPlayer.create(context, R.raw.light_switch_off);
        }else{
            mp = MediaPlayer.create(context, R.raw.light_switch_on);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }
}
