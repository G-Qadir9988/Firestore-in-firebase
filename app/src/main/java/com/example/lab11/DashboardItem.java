package com.example.lab11;

import androidx.annotation.DrawableRes;

public class DashboardItem {
    public String title;
    @DrawableRes
    public int iconResId;
    public Class<?> targetActivity;

    public DashboardItem(String title, int iconResId, Class<?> targetActivity) {
        this.title = title;
        this.iconResId = iconResId;
        this.targetActivity = targetActivity;
    }
}