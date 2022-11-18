package com.example.assignment_demo.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ViewAlertTool {
    public static void showDialog(Context context, String message, String title) {
        new AlertDialog.Builder(context).setMessage(message)
                .setNeutralButton("OK", ((dialog, which) -> dialog.dismiss())).setTitle(title).show();
    }

    public static void showConfirmDialog(Context context, String message,
                                         String title, DialogInterface.OnClickListener onOk, DialogInterface.OnClickListener onCancel) {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK", onOk)
                .setNegativeButton("Cancel", onCancel).setTitle(title).show();
    }
}
