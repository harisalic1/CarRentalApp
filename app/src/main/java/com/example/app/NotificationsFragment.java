package com.example.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

public class NotificationsFragment extends Fragment {

    /**
     * NotificationId used for the notifications from this Fragment.
     */
    private static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;

    /**
     * Button to show a notification.
     */
    private Button mShowNotificationButton, volvoBtn;

    /**
     * If checked, notifications that this Fragment creates will be displayed as Heads-Up
     * Notifications.
     */
    //private CheckBox mUseHeadsUpCheckbox;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context
                .NOTIFICATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mShowNotificationButton = (Button) view.findViewById(R.id.corsabtn);
        mShowNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mNotificationManager.notify(NOTIFICATION_ID, createNotification(
                 //       mUseHeadsUpCheckbox.isChecked()));
                Toast.makeText(getActivity(), "Corsa rented", Toast.LENGTH_SHORT).show();
            }
        });

        //mUseHeadsUpCheckbox = (CheckBox) view.findViewById(R.id.use_heads_up_checkbox);

        volvoBtn = (Button) view.findViewById(R.id.volvobtn);
        volvoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Volvo rented", Toast.LENGTH_SHORT).show();
            }
        });


        }

    /**
     * Creates a new notification depending on the argument.
     *
     * @param makeHeadsUpNotification A boolean value to indicating whether a notification will be
     *                                created as a heads-up notification or not.
     *                                <ul>
     *                                <li>true : Creates a heads-up notification.</li>
     *                                <li>false : Creates a non-heads-up notification.</li>
     *                                </ul>
     *
     * @return A Notification instance.
     */

    /*private Notification createNotification(boolean makeHeadsUpNotification) {
        Notification.Builder notificationBuilder = new Notification.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("Sample Notification")
                .setContentText("This is a normal notification.");
        if (makeHeadsUpNotification) {
            Intent push = new Intent();
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(getActivity(), MainActivity2.class);

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(getActivity(), 0,
                    push, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationBuilder
                    .setContentText("Heads-Up Notification on Android L or above.")
                    .setFullScreenIntent(fullScreenPendingIntent, true);
        }
        return notificationBuilder.build();
    }*/
}