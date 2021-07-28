package com.rustyard.ddnetstatustool;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rustyard.ddnetstatustool.databinding.FragmentSingleBinding;

import java.util.List;

public class SingleFragment extends Fragment {
    private CustomAdapter adapter;
    private PlayerInfoViewModel viewModel;
    private FragmentSingleBinding binding;

    public SingleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(PlayerInfoViewModel.class);
        // Inflate the layout for this fragment
        binding = FragmentSingleBinding.inflate(inflater, container, false);
        adapter = new CustomAdapter();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);
        viewModel.p1InfoList.observe(requireActivity(), strings -> adapter.setLocalDataSet(strings));

        binding.buttonSearch.setOnClickListener(v -> {
            if (binding.editTextPlayerID.getText() != null) {
                SearchTask task = new SearchTask(this);
                task.execute(binding.editTextPlayerID.getText().toString());
            }
        });
    }
}