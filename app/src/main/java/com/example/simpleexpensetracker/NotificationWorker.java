package com.example.simpleexpensetracker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.Random;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        if (SettingsManager.areNotificationsEnabled(context)) {
            String[] messages = {
                    "Don't forget to track your expenses today to stay on budget!",
                    "Every entry counts! Have you logged your recent spending?",
                    "Keeping tabs on your money is the key to financial success. Add your expenses now!",
                    "Check-in time! How's your budget looking today? Log your expenses to see.",
                    "A quick reminder to log your expenses and stay on track with your financial goals.",
                    "Have you made any purchases today? Take a moment to record them.",
                    "Small expenses add up! Keep your budget updated.",
                    "Your financial future is built today. Log your expenses to see your progress.",
                    "Stay mindful of your spending. Have you logged everything for today?",
                    "Consistent tracking is the secret to a healthy budget. Add your expenses now!"
            };


            Random random = new Random();
            String randomMessage = messages[random.nextInt(messages.length)];

            DatabaseHelper db = new DatabaseHelper(context);
            db.addNotification(randomMessage, "reminder");

            NotificationUtils.showDailyReminderNotification(context, "Expense Tracker Reminder", randomMessage);
        }

        return Result.success();
    }
}
