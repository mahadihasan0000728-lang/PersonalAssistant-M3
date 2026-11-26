package com.m3.assistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.JavascriptInterface;
import java.util.Calendar;

public class MainActivity {
    Context ctx;
    public MainActivity(Context c){ ctx = c; }

    @JavascriptInterface
    public void setAlarm(String text, String time) {
        try {
            String[] p = time.split(":");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(p[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(p[1]));
            cal.set(Calendar.SECOND, 0);
            if (cal.getTimeInMillis() < System.currentTimeMillis()) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            Intent intent = new Intent(ctx, AlarmReceiver.class);
            intent.putExtra("text", text);
            PendingIntent pi = PendingIntent.getBroadcast(ctx, (int)cal.getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (am.canScheduleExactAlarms()) am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            } else {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
