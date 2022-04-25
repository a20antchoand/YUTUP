package com.example.practica5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.practica5.recyclerView.ListAdapterMusica;
import com.example.practica5.recyclerView.ListElementMusica;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setup();

    }

    private void setup() {

        List<ListElementMusica> listElements = new ArrayList<>();

        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            try {
                MediaPlayer aux = new MediaPlayer();
                aux = MediaPlayer.create(MainActivity2.this, field.getInt(field));
                listElements.add(new ListElementMusica(field.getName(), aux.getDuration(), field.getInt(field)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        ListAdapterMusica listAdapter = new ListAdapterMusica(listElements, this, this::reproduir);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

        findViewById(R.id.button).setOnClickListener(l -> pararMusica());

    }

    private void pararMusica() {

        MainActivity.reproductor.stop();

    }


    private void reproduir(ListElementMusica item) {

        if (MainActivity.reproductor.isPlaying()) {

            MainActivity.reproductor.stop();

            int idMusica = item.getId();

            MainActivity.reproductor = MediaPlayer.create(MainActivity2.this, idMusica);

            MainActivity.reproductor.start();
            MainActivity.reproductor.setLooping(true);

        } else {
            int idMusica = item.getId();

            MainActivity.reproductor = MediaPlayer.create(MainActivity2.this, idMusica);

            MainActivity.reproductor.start();
            MainActivity.reproductor.setLooping(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        startActivity(new Intent(this, MainActivity.class));

        return true;
    }
}