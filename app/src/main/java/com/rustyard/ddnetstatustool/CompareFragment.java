package com.rustyard.ddnetstatustool;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rustyard.ddnetstatustool.databinding.FragmentCompareBinding;

public class CompareFragment extends Fragment {
    private CustomAdapter adapter;
    private PlayerInfoViewModel viewModel;
    private FragmentCompareBinding binding;
    public CompareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity())
                .get(PlayerInfoViewModel.class);
        // Inflate the layout for this fragment
        binding = FragmentCompareBinding.inflate(inflater, container, false);
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
        viewModel.compareInfoList.observe(requireActivity(),
                strings -> adapter.setLocalDataSet(strings));

        binding.buttonCompare.setOnClickListener(v -> {
            if (binding.editTextComparePlayerID1.getText() != null
            && binding.editTextComparePlayerID2.getText() != null) {
                CompareTask task = new CompareTask(this);
                task.execute(binding.editTextComparePlayerID1.getText().toString(),
                        binding.editTextComparePlayerID2.getText().toString());
            }
        });
    }
}