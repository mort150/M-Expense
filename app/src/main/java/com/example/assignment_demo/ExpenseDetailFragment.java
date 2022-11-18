package com.example.assignment_demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Helper.ViewAlertTool;
import com.example.assignment_demo.databinding.FragmentExpenseDetailBinding;

public class ExpenseDetailFragment extends Fragment {
    private FragmentExpenseDetailBinding binding;
    private AppDB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        requireActivity().setTitle("Expense Detail");
        db = Room.databaseBuilder(requireContext(), AppDB.class, "TripApp.db")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseDetailBinding.inflate(inflater,container,false);
        int expenseId = getArguments().getInt("expenseId");
        db.expenseDAO().findExpenseById(expenseId).observe(getViewLifecycleOwner(),expense -> {
            binding.expenseName.setText("Expense Type: " + expense.getExpenseName());
            binding.expenseDate.setText("Expense Date: " + expense.getExpenseDate());
            binding.expenseCost.setText("Expense Cost: " + expense.getAmount());
            binding.expenseComment.setText("Expense Comment: " + expense.getComment());
        });

        binding.deleteExpenseFab.setOnClickListener(v -> {

            ViewAlertTool.showConfirmDialog(requireContext(),"Do you want to delete this Expense?","Confirm",
                    (dialog, which) -> {
                        db.expenseDAO().delete(expenseId);
                        Toast.makeText(requireContext(), "Delete Successfully!!!", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    },
                    (dialog, which) -> {
                        dialog.dismiss();
                    });
        });

        binding.editExpenseFab.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("expenseId",expenseId);
            NavHostFragment.findNavController(ExpenseDetailFragment.this)
                    .navigate(R.id.action_expenseDetailFragment_to_expenseEditorFragment,bundle);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        requireActivity().setTitle("Expense Detail");
    }
}