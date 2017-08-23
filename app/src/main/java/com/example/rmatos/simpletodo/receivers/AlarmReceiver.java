package com.example.rmatos.simpletodo.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.example.rmatos.simpletodo.R;
import com.example.rmatos.simpletodo.Task;
import com.example.rmatos.simpletodo.TaskStore;
import com.example.rmatos.simpletodo.activities.TaskListActivity;
import com.example.rmatos.simpletodo.activities.TaskPagerActivity;
import com.example.rmatos.simpletodo.fragments.TaskFragment;

import java.util.Date;
import java.util.List;

/**
 * Created by RMatos on 15/08/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {


    static public void setAlarms(Context context) {

        List<Task> tasks = TaskStore.get(context).getTasksWithAlarms();

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        for (Task task : tasks) {

            //If alarm is set
            if (task.getReminderType() == Task.ReminderType.ALARM)
            {
                final long difference = Long.valueOf(new Date().getTime() - SystemClock.elapsedRealtime());     //AlarmManager time is based on SystemClock.elapsedRealtime not Date
                long alarmTime = task.getReminder().getTime() - difference;

                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getAlarmID(),
                        intent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime, alarmIntent);

            }
            //If notification is set
            else if (task.getReminderType() == Task.ReminderType.NOTIFICATION)
            {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!");

                Intent resultIntent = new Intent(context, TaskPagerActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(resultPendingIntent);

                // Sets an ID for the notification
                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }



        }

    }



    @Override
    public void onReceive(Context context, Intent intent) {
        playAlarm(context);
        startApp(context);
    }

    private void playAlarm(Context context) {

        final MediaPlayer player = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        player.start();

        CountDownTimer timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                if (player.isPlaying()) {
                    player.stop();
                    player.release();
                }
            }
        };
        timer.start();
    }

    private void startApp(Context context) {
        final long DELAY_IN_MILLIS = 1000 + System.currentTimeMillis();

        Intent intent = new Intent(context, TaskListActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 10, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, DELAY_IN_MILLIS,pendingIntent);
    }

}
