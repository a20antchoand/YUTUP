package com.example.practica5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class joc extends AppCompatActivity {

    float x = 0, y = 0;
    public Handler HandlerIntroduirHores = new Handler();

    public ConstraintLayout constraintLayout;
    public TextView vidasText, puntsText;
    int puntuacio = 0, vidas = 5, leaveDelay = 10000, appearDelay = 1000;
    List<itemTocable> objetos = new ArrayList<itemTocable>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joc);

        MainActivity.reproductor.pause();

        setup();
    }

    public void setup() {

        vidasText = findViewById(R.id.vidas);
        vidasText.setText("" + vidas);
        puntsText = findViewById(R.id.punts);
        ImageView image = findViewById(R.id.move);

        constraintLayout = findViewById(R.id.constraintLayout);

        image.setOnTouchListener((View.OnTouchListener) (view, motionEvent) -> {

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


                    image.setX(image.getX() + distanciax);
                    image.setY(image.getY() + distanciay);


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

                            System.out.println("LEAVE TIME: " + leaveDelay);
                            System.out.println("APPEAR TIME: " + appearDelay);

                        }
                    }

                    if (eliminar.getRect() != null && vidas > 0) {
                        constraintLayout.removeView(eliminar.getImageView());
                        objetos.remove(eliminar);
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
            imagebyCode.setY(rand.nextInt(((constraintLayout.getMeasuredHeight() - 100) - 100) + 1) + 100);

            constraintLayout.addView(imagebyCode);

            Rect rect = new Rect((int) imagebyCode.getX(), (int) imagebyCode.getY(), (int) imagebyCode.getX() + 200, (int) imagebyCode.getY() + 200);

            itemTocable itemTocable = new itemTocable(rect, imagebyCode);

            objetos.add(itemTocable);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                perdreVida(itemTocable);
            }, leaveDelay);
        }

    }

    public void perdreVida(itemTocable itemTocable) {
        if (objetos.contains(itemTocable) && vidas > 0) {

            constraintLayout.removeView(itemTocable.getImageView());

            vidas--;
            vidasText.setText("" + vidas);

            if (vidas <= 0) {
                stopRepeatingTask();

                new SweetAlertDialog(joc.this, SweetAlertDialog.BUTTON_POSITIVE)
                        .setTitleText("Has perdut!")
                        .setContentText("Vols tornar a jugar?")
                        .setConfirmText("Si")
                        .setCancelText("No")
                        .setConfirmClickListener(l -> startActivity(new Intent(this, joc.class)))
                        .setCancelClickListener(l -> startActivity(new Intent(this, MainActivity.class)))
                        .show();


            }
        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        HandlerIntroduirHores.removeCallbacks(mStatusChecker);
    }


}