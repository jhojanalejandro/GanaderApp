package com.agroapp.proyecto_esmeralda.views.inicio_view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.User_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.controlador.User_Presenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Login_view extends AppCompatActivity {

    EditText edt_clave, edt_nombre_usuario;
    private ProgressDialog progressBar;

    String  user_name, key_user;
    SharedPreferences preferences;
    Share_References_interface share_references_interface;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button btn_sign_in;
    User_Interface login_interface;

    private FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciar_variables();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Login_view.this, gso);
        btn_sign_in.setOnClickListener(v -> {
            progressBar.setTitle("cargando...");
            progressBar.setCancelable(true);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setMessage("Validando la Informacion ");
            progressBar.setIcon(R.drawable.ic_ganaderapp);
            progressBar.show();
            progressBar.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressBar.dismiss();
                }
            });

            if ( edt_nombre_usuario.getText().toString().length() == 0 || edt_clave.getText().toString().length() == 0) {
                Toast.makeText(Login_view.this, "Error  Fantan Datos  ", Toast.LENGTH_SHORT).show();
            }else {
                key_user = edt_clave.getText().toString();
                user_name = edt_nombre_usuario.getText().toString();
                login_interface.check_personal(user_name,key_user,preferences, progressBar);
            }
        });


    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login_view.this, "AutenticacionExitosa" + task.getResult().toString(), Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(Login_view.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            updateUI(null);


                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(Login_view.this, "firebaseAuthWithGoogle" + account.getId(), Toast.LENGTH_SHORT).show();

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(Login_view.this, "Error De Autenticacion" + e, Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cerrar_sesion();
        progressBar.dismiss();
        limpiar_campos();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent login = new Intent(Login_view.this, MainActivity.class);
            startActivity(login);
            b = true;
        }else {
            b = false;
        }

        return b;
    }


    private void limpiar_campos() {
        edt_clave.setText("");
        edt_nombre_usuario.setText("");
    }

    String currentPhotoPath;


    private void cerrar_sesion() {
        preferences.edit().clear().apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {


    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    private void iniciar_variables() {
        edt_clave = findViewById(R.id.edt_ingreso_usuario_contraseña);
        btn_sign_in = findViewById(R.id.btn_sig_in);
        edt_nombre_usuario = findViewById(R.id.edt_email_user_login);
        progressBar = new ProgressDialog(this);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        login_interface = new User_Presenter(Login_view.this);
        share_references_interface = new Share_References_presenter(Login_view.this);

    }



}
