package com.agroapp.proyecto_esmeralda.views.fragment_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import static android.content.Context.MODE_PRIVATE;

public class Mostrar_Video extends AppCompatDialogFragment {

    private View view_;
    VideoView video_prueba;
    FirebaseStorage firebaseStorage;
    Animal_Model modelo_animal;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    String n_finca;
    private String ruta_foto;
    String video_nombre;
    Uri uir_video;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view_  = inflater.inflate(R.layout.mostrar_video_dialog,null);
        iniciar_activity();
        validar_finca_n();
        nombre_video();
        if ( validar_finca_n() == null) {
            Toast.makeText(getContext(), "no se podra complletar el registro por fallos en los datos de recursos", Toast.LENGTH_LONG).show();
            dismiss();
        }

        builder.setView(view_).setTitle("Registrar Animal")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });


        return  builder.create();
    }
    private String nombre_video(){
        preferences =  view_.getContext().getSharedPreferences("preferences", MODE_PRIVATE);
        video_nombre = preferences.getString("video_nombre",null);
        if ( video_nombre!=null){
            return video_nombre;
        }else {
            return  null;
        }
    }
    private String url_video(){
        preferences =  view_.getContext().getSharedPreferences("preferences", MODE_PRIVATE);
        video_nombre = preferences.getString("video_nombre",null);
        if ( video_nombre!=null){
            return video_nombre;
        }else {
            return  null;
        }
    }

    private void downloadManager(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("download");
        request.setTitle(""+video_nombre);
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+video_nombre+".mp4");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        video_prueba.setVideoURI(uir_video);
        MediaController mediaController = new MediaController(getContext());
        video_prueba.setMediaController(mediaController);

    }

    private   void  iniciar_activity(){

        firebaseStorage = FirebaseStorage.getInstance();
        video_prueba = view_.findViewById(R.id.video_prueba_1);

    }
    private String validar_finca_n(){
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        n_finca = preferences.getString("finca",null);
        if ( n_finca!=null){
            return n_finca;
        }else {
            return  null;
        }
    }



}
