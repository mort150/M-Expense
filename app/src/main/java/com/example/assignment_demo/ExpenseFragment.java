package com.example.assignment_demo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Tools.ViewAlertTool;
import com.example.assignment_demo.databinding.FragmentExpenseBinding;

import java.util.Objects;

public class ExpenseFragment extends Fragment {
    private FragmentExpenseBinding binding;
    private ExpenseViewModel mViewModel;
    private int tripId;
    private AppDB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        db = Room.databaseBuilder(requireContext(), AppDB.class, "TripApp.db")
                .allowMainThreadQueries()
                .build();
        tripId = getArguments().getInt("tripId");
        mViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        mViewModel.setDb(db);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseBinding.inflate(inflater, container, false);
        db.tripDAO().findTripById(tripId).observe(getViewLifecycleOwner(), (trip -> {
            binding.toolBar.setTitle(trip.getName() + " Trip");
            binding.toolBar.setSubtitle("List Expenses");
            binding.toolBar.addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menu.add("Edit Trip").setIcon(R.drawable.ic_edit).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM).setOnMenuItemClickListener((view) -> {
                        Bundle bundle = new Bundle();
                        bundle.putInt("tripId", tripId);
                        NavHostFragment.findNavController(ExpenseFragment.this).navigate(R.id.action_expenseFragment_to_editorFragment, bundle);
                        return true;
                    });
                    menu.add("Delete Trip").setIcon(R.drawable.ic_delete).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM).setOnMenuItemClickListener((view) -> {
                        ViewAlertTool.showConfirmDialog(requireContext(), "Do you want to delete this Trip?", "Confirm", (dialog, which) -> {
                            db.tripDAO().delete(tripId);
                            Toast.makeText(requireContext(), "Delete Successfully!!!", Toast.LENGTH_SHORT).show();
                            requireActivity().onBackPressed();
                        }, (dialog, which) -> {
                            dialog.dismiss();
                        });
                        return true;
                    });
                    menu.add("Trip Detail").setIcon(R.drawable.ic_detail).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                            .setOnMenuItemClickListener((view) -> {
                                String info = "Trip Name: " + trip.getName() + "\n"
                                        + "Destination: " + trip.getDestination() + "\n"
                                        + "Date: " + trip.getDate() + "\n"
                                        + "Assessment: " + trip.isAssessment() + "\n"
                                        + "Description: " + trip.getDescription() + "\n"
                                        + "Expected Cost: " + trip.getExpectedCost() + "\n"
                                        + "Member Number: " + trip.getMembersAmount();
                                ViewAlertTool.showDialog(requireContext(), info, trip.getName() + " Detail");
                                return true;
                            });
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                    return false;
                }
            });
        }));

        mViewModel.getExpenseByTripId(tripId).observe(getViewLifecycleOwner(), (expenses -> {
            ExpenseAdapter expenseAdapter = new ExpenseAdapter(expenses);
            expenseAdapter.setOnClickListener((view, position) -> {
                Bundle bundle = new Bundle();
                bundle.putInt("expenseId", expenses.get(position).getExpenseId());
                NavHostFragment.findNavController(ExpenseFragment.this).navigate(R.id.action_expenseFragment_to_expenseDetailFragment, bundle);
            });
            binding.expenseRecyclerView.setAdapter(expenseAdapter);
            binding.expenseRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.expenseRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        }));


        binding.addExpenseFab.setOnClickListener((v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("tripId", tripId);
            NavHostFragment.findNavController(ExpenseFragment.this)
                    .navigate(R.id.action_expenseFragment_to_expenseEditorFragment, bundle);
        }));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
    }
}