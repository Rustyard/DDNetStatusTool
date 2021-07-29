package com.rustyard.ddnetstatustool;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CompareTask extends AsyncTask<String, Integer, Integer> {
    protected WeakReference<Fragment> fragment;
    PlayerInfoViewModel viewModel;

    public CompareTask(CompareFragment context) {
        super();
        fragment = new WeakReference<>(context);
        viewModel = new ViewModelProvider(fragment.get().requireActivity())
                .get(PlayerInfoViewModel.class);
    }

    @Override
    protected void onPreExecute() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(fragment.get().getString(R.string.textLoading));
        viewModel.compareInfoList.postValue(temp);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int resultCode1 = viewModel.crawl(strings[0], viewModel.player1Info);
        int resultCode2 = viewModel.crawl(strings[1], viewModel.player2Info);
        viewModel.generateCompareInfo(fragment.get().requireActivity());
        return Math.min(resultCode1, resultCode2);
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        ArrayList<String> temp = new ArrayList<>();
        if (resultCode != PlayerInfoViewModel.STATUS_SUCCESS) {
            if (resultCode == PlayerInfoViewModel.STATUS_NO_PLAYER) {
                temp.add(fragment.get().getString(R.string.textNoPlayerAtLeast));
            }
            if (resultCode == PlayerInfoViewModel.STATUS_TIMEOUT) {
                temp.add(fragment.get().getString(R.string.textTimeout));
            }
            if (resultCode <= PlayerInfoViewModel.STATUS_NO_RESULT) {
                temp.add(fragment.get().getString(R.string.textWentWrong));
            }
            viewModel.compareInfoList.postValue(temp);
        }
    }
}
