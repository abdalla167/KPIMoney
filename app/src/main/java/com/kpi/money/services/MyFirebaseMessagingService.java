package com.kpi.money.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.kpi.money.R;
import com.kpi.money.activities.AppActivity;
import com.kpi.money.activities.FragmentsActivity;
import com.kpi.money.app.App;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kpi.money.receiver.BatteryStatusReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.kpi.money.constants.Constants.DEBUG_MODE;

/**
 * Created by DroidOXY
 *
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private BatteryStatusReceiver broadcaster;

    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;
    BatteryStatusReceiver mBatteryStatusReceiver;

    Bitmap image;






    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        super.onMessageReceived(remoteMessage);


        Intent intent = new Intent("MyData");
        this.sendBroadcast(intent);

        Log.d(TAG, "From : " + remoteMessage.getFrom());
//        Log.d(TAG, "FCM Data : " + remoteMessage.getData());

        if(DEBUG_MODE) {
            Log.d(TAG, "From : " + remoteMessage.getFrom());
            Log.d(TAG, "FCM Data : " + remoteMessage.getData());
        }

        String tag = "text";
        String title = remoteMessage.getData().get("title");
        String msg = remoteMessage.getData().get("message");
        String img = remoteMessage.getData().get("image");
        String type = remoteMessage.getData().get("type");

        if (img.length() > 5) { tag ="image"; image = getBitmapFromURL(img);}
        if(type == null){ type = "none"; }

        switch(title){

            case "credit":

                title = getResources().getString(R.string.congratulations);
                msg = msg+ " " + getResources().getString(R.string.app_currency) + " " + getResources().getString(R.string.successfull_received);
                type = "transactions";

                break;

            case "redeemed":

                title = getResources().getString(R.string.redeem_recevied)+" "+msg;
                msg = getResources().getString(R.string.redeem_succes_message);
                type = "transactions";

                break;

            case "referer":

                title = getResources().getString(R.string.congratulations);
                msg = msg+ " " + getResources().getString(R.string.app_currency) + " " + getResources().getString(R.string.refer_bonus_received);

                break;

            case "invite":

                title = getResources().getString(R.string.congratulations);
                msg = msg+ " " + getResources().getString(R.string.app_currency) + " " + getResources().getString(R.string.invitation_bonus_received);
                type = "transactions";

                break;
        }

        sendNotification(tag, title, msg, type, image);
    }

    /**
     * Show the notification received
     */
    private void sendNotification(String tag, String title, String messageBody, String type, Bitmap img) {
		
		Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelId = "fcm_default_channel";
        String channelName = getResources().getString(R.string.app_name);

        if (tag.equalsIgnoreCase("image")) {
            notificationBuilder = new NotificationCompat.Builder(this,channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(img))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);

        } else {

            notificationBuilder = new NotificationCompat.Builder(this,channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
        }

        if(type.equals("transactions")){

			Intent transactionsintent = new Intent(this, FragmentsActivity.class);
            transactionsintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            transactionsintent.putExtra("show","transactions");
			PendingIntent transactionsIntent = PendingIntent.getActivity(this, 0, transactionsintent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(transactionsIntent);

        }else{

			Intent appintent = new Intent(this, AppActivity.class);
            appintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent appIntent = PendingIntent.getActivity(this, 0, appintent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(appIntent);
			
		}

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        int Notification_ID = App.getInstance().get("noid",10)+1;
        App.getInstance().store("noid",Notification_ID);

        notificationManager.notify(Notification_ID , notificationBuilder.build());
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            return BitmapFactory.decodeStream(input);

        } catch (IOException e) { /* e.printStackTrace() */; return null; }
    }

    // [START on_new_token]
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        App.getInstance().store("FireBaseToken", token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }
}
