package com.example.practica5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class joc extends AppCompatActivity {

    float x = 0, y = 0;
    public Handler HandlerIntroduirHores = new Handler();
    Handler perdreVida = new Handler();

    public ConstraintLayout constraintLayout;
    public TextView vidasText, puntsText;
    int puntuacio = 0, vidas = 5, leaveDelay = 10000, appearDelay = 1000;
    List<itemTocable> objetos = new ArrayList<itemTocable>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joc);

        setup();


        if (savedInstanceState != null) {
            vidas = savedInstanceState.getInt("vidas");
            puntuacio = savedInstanceState.getInt("punts");
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void setup() {

        vidasText = findViewById(R.id.vidas);
        vidasText.setText("" + vidas);
        puntsText = findViewById(R.id.punts);
        ImageView image = findViewById(R.id.move);

        constraintLayout = findViewById(R.id.constraintLayout);

        image.setOnTouchListener((View view, @SuppressLint("ClickableViewAccessibility") MotionEvent motionEvent) -> {

            switch (motionEvent.getActionMasked()) {

                case MotionEvent.ACTION_DOWN:
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float movedX, movedy;
                    movedX = motionEvent.getX();
                    movedy = motionEvent.getY();

                    float distanciax = movedX - x;
                    float distanciay = movedy - y;

                    ConstraintLayout constraintLayoutParent = findViewById(R.id.constraint_joc);

                    if (image.getX() + distanciax > 5 && image.getX() + distanciax < (constraintLayoutParent.getWidth() - (image.getWidth() + 5))) {

                        image.setX(image.getX() + distanciax);
                        image.setY(image.getY() + distanciay);

                    }

                    Rect rectYUTUP = new Rect((int)image.getX(), (int)image.getY(), (int)image.getX() + 100, (int)image.getY() + 100);

                    System.out.println(objetos);

                    itemTocable eliminar = new itemTocable(null, null);

                    for (itemTocable colision : objetos) {

                        if (Rect.intersects(colision.getRect(), rectYUTUP) && vidas > 0) {
                            System.out.println("TOCAT");
                            eliminar = colision;
                            puntsText.setText("" + ++puntuacio);

                            if (puntuacio % 3 == 0 && puntuacio > 0)
                                if (leaveDelay > 500)
                                    leaveDelay -= 250;

                            if (puntuacio % 25 == 0 && puntuacio > 0)
                                if (appearDelay > 250)
                                    appearDelay -= 250;


                        }
                    }

                    if (eliminar.getRect() != null && vidas > 0) {

                        LottieAnimationView animationView = findViewById(R.id.animationView);

                        animationView.setVisibility(View.VISIBLE);

                        animationView.setX(eliminar.getImageView().getX());
                        animationView.setY(eliminar.getImageView().getY());

                        animationView.playAnimation();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            animationView.setVisibility(View.INVISIBLE);
                        }, 700);

                        new Handler(Looper.getMainLooper()).post(() -> {
                            MediaPlayer matatopo;
                            matatopo = MediaPlayer.create(this, R.raw.matatopo);
                            matatopo.setVolume(1000, 1000);
                            matatopo.start();
                        });

                        constraintLayout.removeView(eliminar.getImageView());
                        objetos.remove(eliminar);
                    }

                    if (puntuacio >= 150) {
                        stopRepeatingTask();

                        SweetAlertDialog sDialog = new SweetAlertDialog(joc.this, SweetAlertDialog.SUCCESS_TYPE);
                        sDialog
                                .setTitleText("Has guanyat!")
                                .setContentText("Vols tornar a jugar?")
                                .setConfirmText("Si")
                                .setCancelText("No")
                                .setConfirmClickListener(l -> {
                                    startActivity(new Intent(this, joc.class));
                                    MainActivity.reproductor.start();
                                })
                                .setCancelClickListener(l -> {
                                    startActivity(new Intent(this, MainActivity.class));
                                    MainActivity.reproductor.start();
                                })
                                .setCancelable(false);
                        sDialog.show();
                    }

                    break;
            }

            return true;

        });

        startRepeatingTask();

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus(); //this function can change value of mInterval.
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                HandlerIntroduirHores.postDelayed(mStatusChecker, appearDelay);
            }
        }
    };

    private void updateStatus() {

        if (objetos.size() < 300) {
            Random rand = new Random();

            ImageView imagebyCode = new ImageView(joc.this);

            imagebyCode.setMaxWidth(50);
            imagebyCode.setBackground(getResources().getDrawable(R.drawable.topo2));
            imagebyCode.setX(rand.nextInt(((constraintLayout.getMeasuredWidth() - 100) - 100) + 1) + 100);
            imagebyCode.setY(rand.nextInt(((constraintLayout.getMeasuredHeight() - 200) - 200) + 1) + 200);

            constraintLayout.addView(imagebyCode);

            Rect rect = new Rect((int) imagebyCode.getX(), (int) imagebyCode.getY(), (int) imagebyCode.getX() + 200, (int) imagebyCode.getY() + 200);

            itemTocable itemTocable = new itemTocable(rect, imagebyCode);

            objetos.add(itemTocable);

            perdreVida.postDelayed(() -> perdreVida(itemTocable), leaveDelay);
        }

    }

    public void perdreVida(itemTocable itemTocable) {
        if (objetos.contains(itemTocable) && vidas > 0) {

            constraintLayout.removeView(itemTocable.getImageView());

            vidas--;
            vidasText.setText("" + vidas);

            if (vidas <= 0) {
                stopRepeatingTask();

                MainActivity.reproductor.pause();

                MediaPlayer reproductorFail;
                reproductorFail = MediaPlayer.create(this, R.raw.perder);
                reproductorFail.start();

                SweetAlertDialog sDialog = new SweetAlertDialog(joc.this, SweetAlertDialog.ERROR_TYPE);
                sDialog.setTitleText("Has perdut!")
                        .setContentText("Vols tornar a jugar?")
                        .setConfirmText("Si")
                        .setCancelText("No")
                        .setConfirmClickListener(l -> {
                            startActivity(new Intent(this, joc.class));
                            MainActivity.reproductor.start();
                        })
                        .setCancelClickListener(l -> {
                            startActivity(new Intent(this, MainActivity.class));
                            MainActivity.reproductor.start();
                        })
                        .setCancelable(false);
                sDialog.show();



            }
        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        HandlerIntroduirHores.removeCallbacks(mStatusChecker);
        perdreVida.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopRepeatingTask();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("vidas", vidas);
        outState.putInt("punts", puntuacio);
        super.onSaveInstanceState(outState);
    }

}