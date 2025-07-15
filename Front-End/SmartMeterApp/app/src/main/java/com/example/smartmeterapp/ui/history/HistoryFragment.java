package com.example.smartmeterapp.ui.history;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.smartmeterapp.MeterData;
import com.example.smartmeterapp.R;

public class HistoryFragment extends Fragment {
    private ListView historyListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historyfragment, container, false);
        historyListView = view.findViewById(R.id.historyList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                MeterData.getInstance().getHistory()
        );
        historyListView.setAdapter(adapter);

        return view;
    }
}
