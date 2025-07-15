package com.example.smartmeterapp.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smartmeterapp.MeterData;
import com.example.smartmeterapp.R;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {
    private TextView meterIdTextView;
    private TextView balanceTextView;
    private TextView lastTokenTextView;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pollRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        meterIdTextView = view.findViewById(R.id.homeMeterId);
        balanceTextView = view.findViewById(R.id.homeBalance);
        lastTokenTextView = view.findViewById(R.id.homeLastToken);

        // Display the current meter ID
        String meterId = MeterData.getInstance().getMeterId();
        meterIdTextView.setText("Meter ID: " + meterId);

        // Define the polling task
        pollRunnable = new Runnable() {
            @Override
            public void run() {
                new Thread(() -> {
                    try {
                        // Fetch meter status from server
                        URL url = new URL("http://" + /* your PC IP */ "192.168.8.197"
                                + ":5000/api/meter/" + meterId);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);

                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream(), "utf-8")
                            );
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            JSONObject response = new JSONObject(sb.toString());
                            int balance = response.getInt("balance");
                            String lastToken = response.optString("last_token", "N/A");

                            // Update shared state
                            MeterData.getInstance().setBalance(balance);
                            MeterData.getInstance().setLastToken(lastToken);

                            // Update UI on main thread
                            requireActivity().runOnUiThread(() -> {
                                balanceTextView.setText("Balance: " + balance + " Units");
                                lastTokenTextView.setText("Last Token: " + lastToken);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // Schedule next poll in 3 seconds
                        handler.postDelayed(this, 3000);
                    }
                }).start();
            }
        };

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(pollRunnable);  // Start polling when fragment is visible
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(pollRunnable);  // Stop polling when fragment is hidden
    }
}
