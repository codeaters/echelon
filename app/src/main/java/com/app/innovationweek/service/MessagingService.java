package com.app.innovationweek.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.app.innovationweek.LeaderboardActivity;
import com.app.innovationweek.LoginActivity;
import com.app.innovationweek.MainActivity;
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

    // launch question activity
    private static final String NEW_QUESTION = "new_question";

    //launch apt leaderboard
    private static final String LB_UPDATE = "leaderboard_update";

    //winner declared or event starting
    private static final String NEWS_UPDATE = "news_update";




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
        if (data.containsKey("notificationType") && data.get("notificationType").equals(NEW_QUESTION)) {
            String messageTitle = "Question " + data.get("question_count") + " of " + data.get("quiz_name") + " is here.";
            String messageBody = data.get("question_statement");
            Intent intent = new Intent(getApplicationContext(), Utils.isLoggedIn(getApplicationContext()) ? QuestionActivity.class : LoginActivity.class);
            intent.putExtra("quiz_id", data.get("quiz_id"));
            //intent.putExtra("question_id", data.get("question_id"));
            intent.putExtra("loginMessage", getResources().getString(R.string.login_message));
            intent.putExtra("launchNext", QuestionActivity.class.getSimpleName());
            sendNotification(messageBody, messageTitle, intent);
        }
        if (data.containsKey("notificationType") && data.get("notificationType").equals(NEWS_UPDATE)) {
            String messageTitle = data.get("contentTitle");
            String messageBody = data.get("contentText");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            sendNotification(messageBody, messageTitle, intent);
        }
        if (data.containsKey("notificationType") && data.get("notificationType").equals(LB_UPDATE)) {
            String messageTitle = data.get("contentTitle");
            String messageBody = data.get("contentText");
            Intent intent = new Intent(getApplicationContext(), LeaderboardActivity.class);
            intent.putExtra("quiz_id", data.get("quiz_id"));
            sendNotification(messageBody, messageTitle, intent);
        }
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
