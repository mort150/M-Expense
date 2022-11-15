package com.example.assignment_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Database.Entity.Trip;
import com.example.assignment_demo.Tools.Uploader;
import com.example.assignment_demo.databinding.FragmentListTripBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ListTripFragment
        extends Fragment {

    private ListTripViewModel mViewModel;
    private FragmentListTripBinding binding;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        handler = new Handler();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ListTripViewModel.class);
        AppDB db = Room.databaseBuilder(requireContext(), AppDB.class, "TripApp.db").allowMainThreadQueries().build();
        binding = FragmentListTripBinding.inflate(inflater, container, false);
//        binding.toolbar.setTitle("Trip Management");
        binding.toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu);
                ((SearchView) menu.findItem(R.id.searchAction).getActionView()).setOnClickListener((v -> {
                    binding.toolbarTitle.setVisibility(View.GONE);
                }));
                ((SearchView) menu.findItem(R.id.searchAction).getActionView()).setOnCloseListener(() -> {
                    binding.toolbarTitle.setVisibility(View.VISIBLE);
                    return false;
                });
                ((SearchView) menu.findItem(R.id.searchAction).getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mViewModel.searchTripByName("%" + newText + "%").observe(getViewLifecycleOwner(), (ListTripFragment.this::loadRecyclerView));
                        return false;
                    }

                });

                menu.add("Upload").setOnMenuItemClickListener(menuItem -> {
                    List<Trip> tripList = db.tripDAO().getAllDataWithoutLive();
                    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        Uploader uploader = new Uploader("http://cwservice1786.herokuapp.com/sendPayLoad");
                        uploader.setUserId("123");
                        uploader.setParam(tripList);
                        handler.post(() -> {
                            new AlertDialog.Builder(requireContext()).setTitle("Request").setMessage(uploader.getJson_payload())
                                    .setNeutralButton("OK", null).show();
                        });
                        try {
                            return uploader.upload();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
                    future.thenAccept(result -> {

                        handler.post(() -> {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                new AlertDialog.Builder(requireContext()).setTitle(jsonObject.getString("uploadResponseCode"))
                                        .setMessage("User ID: " + jsonObject.getString("userid") + "\n"
                                                + "Number of uploaded trips: " + jsonObject.getString("number") + "\n"
                                                + "Message: " + jsonObject.getString("message"))

                                        .setNeutralButton("OK", null).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    });
                    return false;
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        mViewModel.setDb(db);
        mViewModel.getListTrips().observe(
                getViewLifecycleOwner(),
                this::loadRecyclerView
        );

        binding.FabAdd.setOnClickListener((view) -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_listTripFragment_to_editorFragment);
        });
        return binding.getRoot();
    }

    public void loadRecyclerView(List<Trip> listTrip) {
        TripListAdapter adapter = new TripListAdapter(listTrip);
        adapter.setOnClickListener(((view, position) -> {
            // Each time clicks on item, code will run here
            Bundle bundle = new Bundle();
            bundle.putInt("tripId", listTrip.get(position).getTripId());
            NavHostFragment.findNavController(this).navigate(R.id.action_listTripFragment_to_expenseFragment, bundle);
        }));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager((new LinearLayoutManager(requireContext())));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(),
                RecyclerView.VERTICAL));
    }
}