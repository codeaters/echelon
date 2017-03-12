package com.app.innovationweek.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.innovationweek.LoginActivity;
import com.app.innovationweek.QuestionActivity;
import com.app.innovationweek.R;
import com.app.innovationweek.util.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Madeyedexter on 08-03-2017.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = MessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...



        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        System.out.println(TAG + ": From: " + remoteMessage.getFrom());
        Map<String, String> data = remoteMessage.getData();
        if (data.containsKey("notificationType") && data.get("notificationType").equals("new_question")) {
            String messageTitle = "Question " + data.get("question_count") + " of " + data.get("quiz_name") + " is here.";
            String messageBody = data.get("question_statement");
            String notificationAction = Utils.isLoggedIn(getApplicationContext()) ? QuestionActivity.class.getName() : LoginActivity.class.getName();
            Intent intent = new Intent(getApplicationContext(), Utils.isLoggedIn(getApplicationContext()) ? QuestionActivity.class : LoginActivity.class);
            intent.putExtra("quiz_id", data.get("quiz_id"));
            intent.putExtra("question_id", data.get("question_id"));
            intent.putExtra("loginMessage", getResources().getString(R.string.loginMessage));
            intent.putExtra("launchNext", QuestionActivity.class.getSimpleName());
            sendNotification(messageBody, messageTitle, intent);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String messageBody, String messageTitle, Intent intent) {

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
        ;

        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
