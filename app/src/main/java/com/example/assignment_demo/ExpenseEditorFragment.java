package com.example.assignment_demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Database.Entity.Expense;
import com.example.assignment_demo.Helper.ArrayTool;
import com.example.assignment_demo.Helper.InputTool;
import com.example.assignment_demo.databinding.FragmentExpenseEditorBinding;

public class ExpenseEditorFragment extends Fragment {
    private FragmentExpenseEditorBinding binding;
    private int tripId;
    private AppDB db;
    private String[] expenseTypes = {"Food","Transport","Rent"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        db = Room.databaseBuilder(requireContext(), AppDB.class, "TripApp.db")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseEditorBinding.inflate(inflater, container, false);

        InputTool.createDatePicker(requireContext(), binding.expenseTime);
        ArrayAdapter<String> expenseTypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, expenseTypes);
        binding.expenseName.setAdapter(expenseTypeAdapter);

        if (getArguments().getInt("expenseId") == 0) {
            requireActivity().setTitle("Add Expense");
            tripId = getArguments().getInt("tripId");

            binding.savebtn.setOnClickListener(
                    (view) -> {
                        if (InputTool.validate(binding.expenseTime, binding.expenseCost)) {
                            Expense expense = new Expense();
                            expense.setExpenseName(binding.expenseName.getSelectedItem().toString());
                            expense.setExpenseDate(binding.expenseTime.getText().toString());
                            expense.setAmount(Double.parseDouble(binding.expenseCost.getText().toString()));
                            expense.setComment(binding.comment.getText().toString().isEmpty() ? "" : binding.comment.getText().toString());

                            db.tripDAO().findTripById(tripId).observe(getViewLifecycleOwner(), (trip) -> {
                                expense.setTrip(trip);
                                db.expenseDAO().insert(expense);
                                Toast.makeText(requireContext(), "Add successfully!!!", Toast.LENGTH_SHORT).show();
                                requireActivity().onBackPressed();
                            });
                        }
                    }
            );
        } else {
            requireActivity().setTitle("Edit Expense");
            int expenseId = getArguments().getInt("expenseId");
            db.expenseDAO().findExpenseById(expenseId).observe(getViewLifecycleOwner(), (expense -> {
                binding.expenseName.setSelection(ArrayTool.findItemPosition(expenseTypes,expense.getExpenseName()));
                binding.expenseTime.setText(expense.getExpenseDate());
                binding.expenseCost.setText(String.valueOf(expense.getAmount()));
                binding.comment.setText(expense.getComment());

                binding.savebtn.setOnClickListener(
                        (view) -> {
                            if (InputTool.validate(binding.expenseTime, binding.expenseCost)) {
                                expense.setExpenseName(binding.expenseName.getSelectedItem().toString());
                                expense.setExpenseDate(binding.expenseTime.getText().toString());
                                expense.setAmount(Double.parseDouble(binding.expenseCost.getText().toString()));
                                expense.setComment(binding.comment.getText().toString().isEmpty() ? "" : binding.comment.getText().toString());

                                db.expenseDAO().update(expense);
                                Toast.makeText(requireContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                                requireActivity().onBackPressed();
                            }
                        });
            }));
        }
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
    }

}