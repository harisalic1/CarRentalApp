package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn, notify_me;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager notifyManager;
    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullname_id);
        mEmail = findViewById(R.id.email_id);
        mPassword = findViewById(R.id.password_id);
        mPhone = findViewById(R.id.phone_id);
        mRegisterBtn = findViewById(R.id.login_button);
        mLoginBtn = findViewById(R.id.arlh);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        fStore = FirebaseFirestore.getInstance();
        notify_me = findViewById(R.id.notify);

        //if account is already created

       /* if (fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        }*/

        //onclicklistener kada kliknemo na register button

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();//trim za format podataka
                String fullname = mFullName.getText().toString();
                String phone = mPhone.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is required!");
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password is required!");
                    return;
                }
                if (password.length()<6)
                {
                    mPassword.setError("Password must be >= 6 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //register the user in the firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            /*// send verfication link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "Verification Email Has Been Sent.", Toast.LENGTH_SHORT).show(); //u slucaju da je email sentan
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent" + e.getMessage());
                                }
                            });*/

                            Toast.makeText(Register.this, "User created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid(); //ID koji se nalazi u firestore, napravljen je za svakog usera
                            DocumentReference documentReference = fStore.collection("users").document(userID); //store ID
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullname);
                            user.put("email", email);
                            user.put("phone", phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "OnSuccess: user Profile is created for " +userID);  //ctrl+al+c za TAG, sam ti odradi private static final string TAG
                                 }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "On Failure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        progressBar.setVisibility(View.GONE);


        // DIO ZA NOTIFIKACIJE

        notify_me = findViewById(R.id.notify);
        notify_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        createNotificationChannel();
    }
    public void sendNotification()
    {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel()
    {
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "MY notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("This is my notification!");

            notifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder()
    {
        Intent notificationIntent = new Intent(this, Register.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("REGISTRUJ SE!")
                .setContentText("Popuni sva polja.")
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true);

        return notifyBuilder;


    }
}