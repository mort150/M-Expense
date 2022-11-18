package com.example.assignment_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Database.Entity.Trip;
import com.example.assignment_demo.Helper.InputTool;
import com.example.assignment_demo.databinding.FragmentEditorBinding;

public class EditorFragment extends Fragment {

    private EditorViewModel mViewModel;
    private FragmentEditorBinding binding;
    private AppDB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        db = Room.databaseBuilder(requireContext(), AppDB.class, "TripApp.db")
                .allowMainThreadQueries()
                .build();
        mViewModel.setDb(db);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditorBinding.inflate(inflater, container, false);

        InputTool.createDatePicker(requireContext(), binding.date);
        if (getArguments() == null) {
            requireActivity().setTitle("Add trip");

            binding.submitbtn.setOnClickListener(
                    (view) -> {
                        if (InputTool.validate(binding.name, binding.destination, binding.date)) {
                            Trip trip = new Trip();
                            trip.setName(binding.name.getText().toString());
                            trip.setDestination(binding.destination.getText().toString());
                            trip.setAssessment(binding.assessment.isChecked());
                            trip.setDate(binding.date.getText().toString());
                            trip.setDescription(binding.description.getText().toString().isEmpty() ? "" : binding.description.getText().toString());
                            trip.setExpectedCost(Double.parseDouble(binding.expectedCost.getText().toString().isEmpty() ? "0.0" : binding.expectedCost.getText().toString()));
                            trip.setMembersAmount(Integer.parseInt((binding.membersAmount.getText().toString().isEmpty() ? "0" : binding.membersAmount.getText().toString())));

                            db.tripDAO().insert(trip);
                            Toast.makeText(requireContext(), "Add successfully!!!", Toast.LENGTH_SHORT).show();
                            requireActivity().onBackPressed();
                        }
                    }
            );
        } else {
            requireActivity().setTitle("Edit trip");
            int tripId = getArguments().getInt("tripId");
            db.tripDAO().findTripById(tripId).observe(getViewLifecycleOwner(), (trip -> {
                binding.name.setText(trip.getName());
                binding.destination.setText(trip.getDestination());
                binding.assessment.setChecked(trip.isAssessment());
                binding.date.setText(trip.getDate());
                binding.description.setText(trip.getDescription());
                binding.expectedCost.setText(String.valueOf(trip.getExpectedCost()));
                binding.membersAmount.setText(String.valueOf(trip.getMembersAmount()));

                binding.submitbtn.setOnClickListener(
                        (view) -> {
                            if (InputTool.validate(binding.name, binding.destination, binding.date)) {
                                trip.setName(binding.name.getText().toString());
                                trip.setDestination(binding.destination.getText().toString());
                                trip.setAssessment(binding.assessment.isChecked());
                                trip.setDate(binding.date.getText().toString());
                                trip.setDescription(binding.description.getText().toString().isEmpty() ? "" : binding.description.getText().toString());
                                trip.setExpectedCost(Double.parseDouble(binding.expectedCost.getText().toString().isEmpty() ? "0.0" : binding.expectedCost.getText().toString()));
                                trip.setMembersAmount(Integer.parseInt((binding.membersAmount.getText().toString().isEmpty() ? "0" : binding.membersAmount.getText().toString())));

                                db.tripDAO().update(trip);
                                Toast.makeText(requireContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                                requireActivity().onBackPressed();
                            }
                        });
            }));
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
    }

}