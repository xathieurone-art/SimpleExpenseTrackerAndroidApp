package com.example.simpleexpensetracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {

    public static final String BUDGET_CHANNEL_ID = "budget_warnings";
    public static final String REMINDER_CHANNEL_ID = "daily_reminders";
    private static final int WELCOME_NOTIFICATION_ID = 101;
    private static final int REMINDER_NOTIFICATION_ID = 202;
    private static final int BUDGET_NOTIFICATION_ID = 303;

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel budgetChannel = new NotificationChannel(
                    BUDGET_CHANNEL_ID,
                    "Budget Warnings",
                    NotificationManager.IMPORTANCE_HIGH
            );
            budgetChannel.setDescription("Notifications for when budget limits are exceeded");

            NotificationChannel reminderChannel = new NotificationChannel(
                    REMINDER_CHANNEL_ID,
                    "Daily Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            reminderChannel.setDescription("Periodic reminders to log expenses");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(budgetChannel);
                manager.createNotificationChannel(reminderChannel);
            }
        }
    }

    public static void showBudgetWarningNotification(Context context, String message) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BUDGET_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning_red)
                .setContentTitle("Budget Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(BUDGET_NOTIFICATION_ID, builder.build());
    }

    public static void showWelcomeNotification(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String title = "Welcome to Expense Tracker!";
        String message = "Your journey to financial clarity starts now. Tap to get started.";

        try (DatabaseHelper db = new DatabaseHelper(context)) {
            db.addNotification(message, "welcome");
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(WELCOME_NOTIFICATION_ID, builder.build());
    }

    public static void showDailyReminderNotification(Context context, String title, String content) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(REMINDER_NOTIFICATION_ID, builder.build());
    }
}
