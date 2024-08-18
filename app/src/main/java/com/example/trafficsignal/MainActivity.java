package com.example.trafficsignal;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private View redLight, yellowLight, greenLight;
    private TextView countdownTimer;
    private Handler handler = new Handler();
    private int redLightDuration = 10; // Red light duration in seconds
    private int greenLightDuration = 5; // Green light duration in seconds
    private int currentLight = 0;
    private boolean isStopped = false; // Flag to track if the sequence is stopped

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redLight = findViewById(R.id.redLight);
        yellowLight = findViewById(R.id.yellowLight);
        greenLight = findViewById(R.id.greenLight);
        countdownTimer = findViewById(R.id.countdownTimer);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        startButton.setOnClickListener(view -> {
            isStopped = false;
            startTrafficSignal();
        });

        stopButton.setOnClickListener(view -> stopTrafficSignal());
    }

    private void startTrafficSignal() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isStopped) return;

                switch (currentLight) {
                    case 0: // Red Light
                        countdownTimer.setTextColor(Color.RED);
                        countdownTimer.setText(String.valueOf(redLightDuration));
                        redLight.setBackgroundColor(Color.RED);
                        yellowLight.setBackgroundColor(Color.DKGRAY);
                        greenLight.setBackgroundColor(Color.DKGRAY);
                        startRedLightCountdown();
                        break;
                    case 1: // Yellow Light
                        countdownTimer.setText("");
                        redLight.setBackgroundColor(Color.DKGRAY);
                        yellowLight.setBackgroundColor(Color.YELLOW);
                        greenLight.setBackgroundColor(Color.DKGRAY);
                        handler.postDelayed(this, 2000); // Yellow light for 2 seconds
                        currentLight = 2;
                        break;
                    case 2: // Green Light
                        countdownTimer.setTextColor(Color.GREEN);
                        countdownTimer.setText(String.valueOf(greenLightDuration));
                        redLight.setBackgroundColor(Color.DKGRAY);
                        yellowLight.setBackgroundColor(Color.DKGRAY);
                        greenLight.setBackgroundColor(Color.GREEN);
                        startGreenLightCountdown();
                        break;
                }
            }
        }, 0);
    }

    private void startRedLightCountdown() {
        new Thread(new Runnable() {
            public void run() {
                for (int i = redLightDuration; i >= 0; i--) {
                    if (isStopped) return;

                    final int finalI = i;
                    runOnUiThread(() -> countdownTimer.setText(String.valueOf(finalI)));
                    try {
                        Thread.sleep(1000); // 1 second delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentLight = 1;
                handler.postDelayed(() -> startTrafficSignal(), 0); // Start yellow light after countdown
            }
        }).start();
    }

    private void startGreenLightCountdown() {
        new Thread(new Runnable() {
            public void run() {
                for (int i = greenLightDuration; i >= 0; i--) {
                    if (isStopped) return;

                    final int finalI = i;
                    runOnUiThread(() -> countdownTimer.setText(String.valueOf(finalI)));
                    try {
                        Thread.sleep(1000); // 1 second delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentLight = 0;
                handler.postDelayed(() -> startTrafficSignal(), 0); // Loop back to red light
            }
        }).start();
    }

    private void stopTrafficSignal() {
        isStopped = true;
        handler.removeCallbacksAndMessages(null); // Stop all handler callbacks
        resetLights();
    }

    private void resetLights() {
        countdownTimer.setText("");
        redLight.setBackgroundColor(Color.DKGRAY);
        yellowLight.setBackgroundColor(Color.DKGRAY);
        greenLight.setBackgroundColor(Color.DKGRAY);
    }
}
