package com.agroapp.proyecto_esmeralda.views.manejo_subastas_view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Adapter_List_Subastas;
import com.agroapp.proyecto_esmeralda.modelos.Subasta_Model;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animales_produ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Lista_Subatas_usuarios extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST = 010;
    private static final int PICK_AUDIO_REQUEST = 020;
    EditText edt_fecha_parto,edt_result,edt_genero,edt_numero_cria,edt_obs,edt_nombre_padre,edt_cant_droga,edt_droga,edt_numero_parto;
    SharedPreferences preferences;
    String fecha,n_finca,n_animal;
    private StorageReference storageReference;
    StorageReference videoRef;
    ArrayList<Subasta_Model> list_sub;
    Context contextt;

    String id,sub_ubicacion,sub_n_anml,sub_fecha_inicio;
    int numero_hijo= 0 ,n_hijo,sub_cant_anml;
    View view;
    FloatingActionButton fab_editar,fab_delete;
    RecyclerView recyclerView;
    Subasta_Model ficha_subasta;
    Adapter_List_Subastas adapter_list_subastas;
    private int PICK_VIDEO = 100;
    LinearLayoutManager layoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    Uri videoUri;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView searchView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_subastas);
        Toolbar toolbar = findViewById(R.id.toolbar_detalle_parto);
        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(contextt);
        fab_editar = findViewById(R.id.fab_parto_editar);
        fab_delete = findViewById(R.id.fab_parto_eliminar);

    }

    private void filtrar(String toString) {
        ArrayList<Subasta_Model> filtrar_lista = new ArrayList<>();
        for (Subasta_Model subasta  : list_sub){
            if (subasta.getFecha_inicio().toLowerCase().contains(toString.toLowerCase())){
                filtrar_lista.add(subasta);
            }

        }
        adapter_list_subastas.filtrar(filtrar_lista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        MenuItem searchItem = menu.findItem(R.id.action_search_);
        SearchManager searchManager = (SearchManager) Lista_Subatas_usuarios.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Lista_Subatas_usuarios.this.getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        getMenuInflater().inflate(R.menu.menu_perfil_subastar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_atras_perfil_sub:
                Intent manejo = new Intent(Lista_Subatas_usuarios.this, Inicio_Ganaderapp.class);
                startActivity(manejo);
                break;
            case R.id.action_p_sub_add_animal:
                Intent manejo_lista_animal = new Intent(Lista_Subatas_usuarios.this, Manejo_animales_produ.class);
                startActivity(manejo_lista_animal);
                break;
            case R.id.action_p_sub_list_subasta:
                finish();
                System.exit(0);
                break;

        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_VIDEO) {
            videoUri = data.getData();
            guardar_video(videoUri);
        }

    }

    private void guardar_video(Uri videouri) {
        storageReference = FirebaseStorage.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        videoRef = storageRef.child("videos").child ( "");
        upload(view);


    }
    public void upload(View view) {
        if (videoUri != null) {
            UploadTask uploadTask = videoRef.putFile(videoUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Lista_Subatas_usuarios.this,
                            "Upload failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Lista_Subatas_usuarios.this, "Upload complete",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Lista_Subatas_usuarios.this, "Registrp Exitoso", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            Toast.makeText(Lista_Subatas_usuarios.this, "Nothing to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void editar_parto(){
        String fe_edit = edt_fecha_parto.getText().toString();
        String obs = edt_obs.getText().toString();
        String result = edt_result.getText().toString();
        String n_toro = edt_nombre_padre.getText().toString();
        String n_cria = edt_numero_cria.getText().toString();
        String droga = edt_droga.getText().toString();
        String cant_droga = edt_cant_droga.getText().toString();
        int cant_drogas  = Integer.parseInt(cant_droga);
        if (n_hijo != 0){
            numero_hijo = Integer.parseInt(n_cria);
        }else {
            numero_hijo = 0;
        }
        DocumentReference docRef = db.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(n_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id);
        regis_ref.update("parto_fecha",fe_edit,"parto_droga_aplicada",droga,"parto_cant_droga",cant_drogas,"parto_observaciones",obs,"parto_n_padre",n_toro,"parto_numero_cria",numero_hijo,"parto_result",result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Lista_Subatas_usuarios.this, "exitoso", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
