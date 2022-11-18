package com.example.assignment_demo.Helper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.util.Calendar;

public class InputTool {
    public static boolean validate(EditText... editTexts) {
        if (editTexts == null || editTexts.length == 0) {
            return false;
        } else {
            for (EditText editText : editTexts) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Invalid input. Please enter again!!!");
                    return false;
                }
            }
            return true;
        }
    }

    public static void createDatePicker(Context context, EditText editText) {
        editText.setFocusable(false);
        editText.setOnClickListener((v) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                String dateTime = "dd-MM-YYYY".replace("YYYY", String.valueOf(year))
                        .replace("MM", String.valueOf(month + 1))
                        .replace("dd", String.valueOf(dayOfMonth));
                editText.setText(dateTime);
            }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }
}
