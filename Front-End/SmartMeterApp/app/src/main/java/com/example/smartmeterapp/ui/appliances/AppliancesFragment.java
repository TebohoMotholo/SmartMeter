package com.example.smartmeterapp.ui.appliances;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.smartmeterapp.R;

import java.util.ArrayList;
import java.util.List;

public class AppliancesFragment extends Fragment {
    private ListView appliancesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appliancesfragment, container, false);
        appliancesListView = view.findViewById(R.id.appliancesList);

        // Simulated appliance consumption data (units consumed so far)
        List<String> appliances = new ArrayList<>();
        appliances.add("Fridge: 5 units");
        appliances.add("TV: 2 units");
        appliances.add("Washing Machine: 3 units");
        appliances.add("Air Conditioner: 4 units");
        appliances.add("Lights: 1 unit");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                appliances
        );
        appliancesListView.setAdapter(adapter);

        return view;
    }
}
