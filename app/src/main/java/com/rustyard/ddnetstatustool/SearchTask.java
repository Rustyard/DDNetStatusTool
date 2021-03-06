package com.rustyard.ddnetstatustool;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SearchTask extends AsyncTask<String, Integer, Integer> {
    protected WeakReference<Fragment> fragment;
    PlayerInfoViewModel viewModel;

    public SearchTask(SingleFragment context) {
        super();
        fragment = new WeakReference<>(context);
        viewModel = new ViewModelProvider(fragment.get().requireActivity())
                .get(PlayerInfoViewModel.class);
    }

    @Override
    protected void onPreExecute() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(fragment.get().getString(R.string.textLoading));
        viewModel.p1InfoList.postValue(temp);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int resultCode = viewModel.crawl(strings[0], viewModel.player1Info);
        viewModel.generateP1Info(fragment.get().requireActivity());
        return resultCode;
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        ArrayList<String> temp = new ArrayList<>();
        if (resultCode != PlayerInfoViewModel.STATUS_SUCCESS) {
            if (resultCode == PlayerInfoViewModel.STATUS_NO_PLAYER) {
                temp.add(fragment.get().getString(R.string.textNoPlayer));
            }
            if (resultCode == PlayerInfoViewModel.STATUS_TIMEOUT) {
                temp.add(fragment.get().getString(R.string.textTimeout));
            }
            if (resultCode <= PlayerInfoViewModel.STATUS_NO_RESULT) {
                temp.add(fragment.get().getString(R.string.textWentWrong));
            }
            viewModel.p1InfoList.postValue(temp);
        }
    }
}
