package com.agroapp.proyecto_esmeralda.controlador;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Gasto_Insumos_Recycle_Anmls_Adapter;
import com.agroapp.proyecto_esmeralda.adapters.Palpation_Recycle_Anmls_Adapter;
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Login_view;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;
import com.agroapp.proyecto_esmeralda.interfaces.Control_agricola_Interface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Control_agricola_Presenter implements Control_agricola_Interface {
    Context context;
    private final static int id_nunico_parto = 125;
    private final static int id_nunico = 124;

    private static final String CHANNEL_ID_SECADO = "message secado ";
    private static final String CHANNEL_ID_PARTO = "message parto";
    Bitmap picture;
    Resources res;
    CollectionReference animales_ref;
    String title_secado, title_parto, text_parto, text_secado;

    public Control_agricola_Presenter(Context context, CollectionReference animales_ref) {
        this.context = context;
        this.animales_ref = animales_ref;
    }

    Share_References_presenter share_preferences_presenter = new Share_References_presenter(context);

    @Override
    public void notify_drying() {
        NotificationCompat.Builder notificacion;
        picture = BitmapFactory.decodeResource(res, R.drawable.ic_toro_mitad);
        create_channel_notify();
        notificacion = new NotificationCompat.Builder(context, CHANNEL_ID_SECADO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.secado_animal_notification_placeholder_text_template);
            String description = context.getString(R.string.channel_description_parto);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SECADO, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        notificacion.setAutoCancel(true);
        notificacion.setSmallIcon(R.drawable.ic_toro_mitad);
        notificacion.setPriority(Notification.PRIORITY_HIGH);
        notificacion.setTicker("TIENES QUE SECAR ALGUNAS VACAS");
        notificacion.setWhen(System.currentTimeMillis());
        notificacion.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text_secado)
                        .setBigContentTitle(title_secado)
                        .setSummaryText("Para Secar"));
        notificacion.setContentTitle("SECADO DE VACAS");
        notificacion.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificacion.setContentText("sugerencia de secado");

        Intent intent = new Intent(context, Login_view.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificacion.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(id_nunico, notificacion.build());

    }

    @Override
    public void notify_part() {
        NotificationCompat.Builder notificacion;
        notificacion = new NotificationCompat.Builder(context, CHANNEL_ID_PARTO);
        create_channel_notify();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.parto_animal_notification_placeholder_text_template);
            String description = context.getString(R.string.channel_description_parto);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_PARTO, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        notificacion.setAutoCancel(true);
        notificacion.setSmallIcon(R.drawable.ic_toro_mitad);
        notificacion.setPriority(Notification.PRIORITY_HIGH);
        notificacion.setTicker("TIENES QUE TRAER ALGUNAS VACAS QUE ESTAN CERCANAS A PARIR");
        notificacion.setWhen(System.currentTimeMillis())
                .setLargeIcon(picture)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text_parto)
                        .setBigContentTitle(title_parto)
                        .setSummaryText("Lactancia Actual"));
        notificacion.setContentTitle("LACTANCIA DEL ANIMAL");
        notificacion.setContentText("sugerencia de parto");

        Intent intent = new Intent(context, Manejo_animal_view.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificacion.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(id_nunico_parto, notificacion.build());

    }

    @Override
    public void consultar_parto_animales_primer_periodo( int mes, int ano) {

        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("tipo_registro", "secado").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                    Gastos_Insumos ficha_secado = documentSnapshots.toObject(Gastos_Insumos.class);
                                    String fecha_c = ficha_secado.getFecha_registro();
                                    if (fecha_c != null) {
                                        int[] fecha = share_preferences_presenter.parse_date(fecha_c);
                                        int month = fecha[1];
                                        int year = fecha[2];
                                        if (month > 2 && year == ano & month < 4) {
                                            notify_part();
                                            return;
                                        }
                                    }

                                }
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public void consultar_parto_animales_segundo_periodo( int mes, int ano) {

        int ano_bus = ano - 1;
        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("tipo_registro", "secado").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                    Gastos_Insumos ficha_secado = documentSnapshots.toObject(Gastos_Insumos.class);
                                    String fecha_c = ficha_secado.getFecha_registro();
                                    if (fecha_c != null) {
                                        int[] fecha = share_preferences_presenter.parse_date(fecha_c);
                                        int month = fecha[1];
                                        int year = fecha[2];
                                        if (ano_bus == year) {
                                            if (month > 10) {
                                                notify_part();
                                                return;
                                            }
                                        }
                                    }

                                }
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public void consultar_secado_animales_primer_periodo( int mes, int ano) {
        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("tipo_registro", "palpacion").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                    Palpacion_Model ficha_palpacion;
                                    ficha_palpacion = documentSnapshots.toObject(Palpacion_Model.class);
                                    String fecha_c = ficha_palpacion.getPalp_fecha_pre();
                                    if (fecha_c != null) {
                                        int[] fecha = share_preferences_presenter.parse_date(fecha_c);
                                        int month = fecha[1];
                                        int year = fecha[2];
                                        for (int i = 0; mes > month; i++) {
                                            if (i == 7 & year == ano) {
                                                notify_drying();
                                                return;
                                            } else if (i > 8) {
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public void consultar_secado_animales_segundo_periodo( int mes, int ano) {

        int ano_bus = ano - 1;
        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("tipo_registro", "palpacion").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                    Palpacion_Model ficha_palpacion;
                                    ficha_palpacion = documentSnapshots.toObject(Palpacion_Model.class);
                                    String fecha_c = ficha_palpacion.getPalp_fecha_pre();
                                    String resultado = ficha_palpacion.getPalp_result();
                                    if (fecha_c != null && resultado.equals("Preñada")) {
                                        int[] fecha = share_preferences_presenter.parse_date(fecha_c);
                                        int month = fecha[1];
                                        int year = fecha[2];
                                        if (year == ano_bus) {
                                            for (int i = 0; mes < month; i++) {
                                                if (i == 7) {
                                                    notify_drying();
                                                    return;
                                                } else if (i > 7) {
                                                    return;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public void show_dry(int mes, int ano, RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        Gasto_Insumos_Recycle_Anmls_Adapter supplies_viewhollder;
        ArrayList<Gastos_Insumos> show_dry_list = new ArrayList<>();
        supplies_viewhollder = new Gasto_Insumos_Recycle_Anmls_Adapter(context, R.layout.item_palpacion, show_dry_list);
        if (mes >= 2) {
            int meses = mes - 2;
            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                        Animal_Model animal_model = documentSnapshots.toObject(Animal_Model.class);
                        final String id = documentSnapshots.getId();
                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("tipo_registro", "secado").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Gastos_Insumos ficha_secado = documentSnapshot.toObject(Gastos_Insumos.class);
                                    ficha_secado.setNombre_droga(id);
                                    String fecha_secado = ficha_secado.getFecha_registro();
                                    int[] fecha = share_preferences_presenter.parse_date(fecha_secado);
                                    int month = fecha[1];
                                    int year = fecha[2];
                                    Toast.makeText(context, "fuera condicional " + month, Toast.LENGTH_SHORT).show();

                                    if (month < meses & year == ano) {
                                        Toast.makeText(context, "entra al condicional", Toast.LENGTH_SHORT).show();
                                        show_dry_list.add(ficha_secado);
                                    }

                                }
                                recyclerView.setAdapter(supplies_viewhollder);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                recyclerView.addItemDecoration(dividerItemDecoration);

                            }
                        });
                    }


                }
            });

        } else {
            int anofinal = ano - 1;

            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Animal_Model animal_model = documentSnapshot.toObject(Animal_Model.class);
                        String id = documentSnapshot.getId();

                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("scd_tipo", "secado").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                    Gastos_Insumos ficha_secado;
                                    ficha_secado = documentSnapshots.toObject(Gastos_Insumos.class);
                                    Toast.makeText(context, "entra al condicional", Toast.LENGTH_SHORT).show();

                                    String fechas = ficha_secado.getFecha_registro();
                                    int[] fecha = share_preferences_presenter.parse_date(fechas);
                                    int mes_bus = fecha[1];
                                    int year = fecha[2];
                                    if (mes_bus >= 10 & year == anofinal) {
                                        show_dry_list.add(ficha_secado);

                                    }

                                }

                                recyclerView.setAdapter(supplies_viewhollder);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                recyclerView.addItemDecoration(dividerItemDecoration);

                            }
                        });

                    }


                }
            });


        }


    }

    @Override
    public void show_part(int mes, int ano, RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        Palpation_Recycle_Anmls_Adapter palpacion_viewhollder;
        ArrayList<Palpacion_Model> show_part_list = new ArrayList<>();
        palpacion_viewhollder = new Palpation_Recycle_Anmls_Adapter(context, R.layout.item_palpacion, show_part_list);
        if (mes <= 5) {
            final int ano_bus = ano - 1;
            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Animal_Model animal_model = documentSnapshot.toObject(Animal_Model.class);
                        String id = documentSnapshot.getId();
                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("palp_tipo", "palpacion").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                    if (documentSnapshots.exists()) {
                                        Palpacion_Model ficha_palpacion = documentSnapshots.toObject(Palpacion_Model.class);
                                        String fecha_c = ficha_palpacion.getPalp_fecha_pre();
                                        if (fecha_c != null) {
                                            int[] fecha = share_preferences_presenter.parse_date(fecha_c);
                                            int month = fecha[1];
                                            int year = fecha[2];
                                            if (month >= 7 & year == ano_bus) {
                                                show_part_list.add(ficha_palpacion);
                                            }
                                        }

                                    }
                                }

                                recyclerView.setAdapter(palpacion_viewhollder);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                recyclerView.addItemDecoration(dividerItemDecoration);


                            }
                        });
                    }
                }
            });

        } else {

            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Animal_Model animal_model = documentSnapshot.toObject(Animal_Model.class);
                        String id = animal_model.getAnml_nombre();
                        final CollectionReference tres = animales_ref.document(id).collection("registro_animal");
                        tres.whereEqualTo("palp_tipo", "palpacion").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                    if (documentSnapshots.exists()) {
                                        Palpacion_Model ficha_palpacion = documentSnapshots.toObject(Palpacion_Model.class);
                                        String fecha_c = ficha_palpacion.getPalp_fecha_pre();
                                        int[] fecha = share_preferences_presenter.parse_date(fecha_c);
                                        int month = fecha[1];
                                        int year = fecha[2];
                                        if (month <= 6 & year == ano) {
                                            show_part_list.add(ficha_palpacion);

                                        }
                                    }
                                }
                                recyclerView.setAdapter(palpacion_viewhollder);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                recyclerView.addItemDecoration(dividerItemDecoration);

                            }
                        });
                    }
                }
            });
        }

    }

    @Override
    public void create_channel_notify() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.parto_animal_notification_placeholder_text_template);
            String description = context.getString(R.string.channel_description_parto);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_PARTO, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
