package com.example.colormemory;

public interface INotifManager {

    int sendNotification(String title, String text, int iconId);

    int sendNotification(int titleRes, int textRes, int iconId);
}
