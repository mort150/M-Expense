package com.example.assignment_demo.Tools;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

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

    public static int[] sum(int... numbers) {
        int[] nums;
        nums = numbers;
        return nums;
    }

    public static void createDatePicker(Context context,EditText editText){
        editText.setFocusable(false);
        editText.setOnClickListener((v)->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                String date = "dd-MM-YYYY".replace("YYYY", String.valueOf(year))
                        .replace("MM", String.valueOf(month + 1))
                        .replace("dd", String.valueOf(dayOfMonth));
                editText.setText(date);
            }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }
}
