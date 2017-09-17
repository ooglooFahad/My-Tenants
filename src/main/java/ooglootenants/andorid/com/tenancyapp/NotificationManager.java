package ooglootenants.andorid.com.tenancyapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotificationManager extends WakefulBroadcastReceiver {


    Tenancy_Dbhelper tenancy_dbhelper;
    int hour, min, sec;

    @Override
    public void onReceive(Context context, Intent intent) {

        tenancy_dbhelper = new Tenancy_Dbhelper(context);
        long longDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(longDate);
        final java.util.Calendar caal = java.util.Calendar.getInstance();
        hour = caal.get(Calendar.HOUR);
        min = caal.get(Calendar.MINUTE);
        sec = caal.get(Calendar.SECOND);

        if (hour == 11 && min < 2) {

            Cursor cursor = tenancy_dbhelper.gettimedrenters(dateString);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            String tname, tphone, trentamount, tpaymentreceived;

                            tname = cursor.getString(2);
                            tphone = cursor.getString(3);
                            trentamount = cursor.getString(7);
                            tpaymentreceived = cursor.getString(9);
                            String Message = "Dear " + tname + "! Please Submit your rent.";
                            int rentamount = Integer.valueOf(trentamount);
                            int paymentreceived = Integer.valueOf(tpaymentreceived);

                            if (rentamount - paymentreceived > 0) {
                                Notification(context, tname);

                                String phonenumber = tphone;
                                String message = Message;
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(phonenumber, null, message, null, null);
                            }
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Notification(Context context, String name) {

        String message = "Rent payment duration of " + name + " is completed!!!";
        Intent intent = new Intent(context, splashactivity.class);
        PendingIntent pintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.spicon))
                .setContentTitle("Tenancy Notification")
                .setContentText(message)
                .setContentIntent(pintent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setLights(Color.BLUE, 500, 500)
                .setSound(alarmSound);
        builder.setSmallIcon(getNotificationIcon(builder));
        Notification notification = builder.build();
        long[] vibrate = {0, 100, 200, 300, 400, 500};
        notification.vibrate = vibrate;
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(Color.parseColor("#425f5a"));
            return R.drawable.ic_stat;
        } else {
            String color = "#425f5a";
            int mycolor = Color.parseColor(color);
            notificationBuilder.setColor(mycolor);
            return R.drawable.spicon;
        }
    }
}
