package com.agroapp.proyecto_esmeralda.views.inicio_view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.ServicioSegundoPlano;
import com.agroapp.proyecto_esmeralda.views.manejo_subastas_view.Inicio_Subasta;
import com.agroapp.proyecto_esmeralda.views.manejo_usuarios_view.User_Register;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    public static final String KEY = "1Hbfh667adfDEJ78";
    public static final String MY_PREFS_NAME = "preferences";
    ImageView img_registrarse, img_ingresar, img_result;
    ImageView img_g, img_a, img_n, img_d, img_e, img_r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("empleado", null);
        String password = prefs.getString("password", null);
        if (name != null && password != null) {
            Intent manejo = new Intent(MainActivity.this, Inicio_Ganaderapp.class);
            startActivity(manejo);
        }
        img_registrarse = findViewById(R.id.btn_registrarse);
        img_result = findViewById(R.id.img_ganaderapp);

        img_ingresar = findViewById(R.id.btn_ingrrsar_login);
        img_result = findViewById(R.id.img_ganaderapp);
        img_g = findViewById(R.id.img_g);
        img_a = findViewById(R.id.img_a);
        img_n = findViewById(R.id.img_n);
        img_d = findViewById(R.id.img_d);
        img_e = findViewById(R.id.img_e);
        img_r = findViewById(R.id.img_r);


        long now = System.currentTimeMillis();
        try {
            if (now - getFirstInstallTime(getApplicationContext()) >
                    TimeUnit.DAYS.toMillis(200)) {
                sendPoisonPill();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Animation animation_ganader = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
        img_result.startAnimation(animation_ganader);
        Animation animation_g = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        img_g.startAnimation(animation_g);
        Animation animation_a = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
        img_a.startAnimation(animation_a);
        Animation animation_n = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        img_n.startAnimation(animation_n);
        Animation animation_d = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
        img_d.startAnimation(animation_d);
        Animation animation_e = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_contrario);
        img_e.startAnimation(animation_e);
        Animation animation_r = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        img_r.startAnimation(animation_r);
        img_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("registro");
            }
        });

        img_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("login");
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            b = true;
        } else {
            b = false;
        }

        return b;
    }


    private void sendPoisonPill() {

        Toast.makeText(MainActivity.this, "la aplicacion Expiro Adquiere un paquete", Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "COMUNICATE CON EL PROVEEDOR PARA ACTUALIZAR", Toast.LENGTH_SHORT).show();
        finish();
        System.exit(0);
    }

    private static long getFirstInstallTime(Context context) throws
            PackageManager.NameNotFoundException {

        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);

        return info.firstInstallTime;
    }

    private void show(String where) {
        if (where.equals("login")) {
            Intent manejo = new Intent(MainActivity.this, Login_view.class);
            startActivity(manejo);

        } else {
            Intent manejo = new Intent(MainActivity.this, User_Register.class);
            startActivity(manejo);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(MainActivity.this, ServicioSegundoPlano.class));

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
        if (id == R.id.action_SALIR_main) {
            System.exit(0);
        }


        return super.onOptionsItemSelected(item);
    }


}
