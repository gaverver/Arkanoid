package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MessageReminder extends BroadcastReceiver {
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users").child(this.user.getUid());
        if (intent.getAction() != null && intent.getAction().equals("NOTIFICATION_ACTION")) {
            String channelId = "notification_channel";
            CharSequence channelName = "Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            HashMap<String, Object> userData = new HashMap<>();
            referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!(snapshot.exists())) {
                        return;
                    }
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        userData.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                    }
                    Object numGamesObj = userData.get("numGames");
                    if (numGamesObj != null) {
                        String numGames = numGamesObj.toString();
                        // Now you can use numGames safely
                        Object gamesWonObj = userData.get("gamesWon");
                        if (gamesWonObj != null) {
                            String gamesWon = gamesWonObj.toString();
                            Notification.Builder builder = new Notification.Builder(context)
                                    .setSmallIcon(R.drawable.paddle)
                                    .setContentTitle("Your stats:")
                                    .setContentText("You played: " + numGames + " games, and you won " + gamesWon + " of them")
                                    .setChannelId("notification_channel");
                            Intent notificationIntent = new Intent(context, ArkLogin.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
                            builder.setContentIntent(pendingIntent);
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(1, builder.build());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    String dd = error.toString();
                    String ssd = error.getMessage();
                }
            });
        }
    }
}
