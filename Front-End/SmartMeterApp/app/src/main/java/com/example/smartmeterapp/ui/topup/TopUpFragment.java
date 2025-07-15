package com.example.smartmeterapp.ui.topup;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smartmeterapp.MeterData;
import com.example.smartmeterapp.R;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TopUpFragment extends Fragment {
    private EditText meterIdEditText;
    private EditText tokenEditText;
    private Button submitButton;
    private TextView responseTextView;
    private static final String SERVER_URL = "http://192.168.8.197:5000/api/token";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topupfragment, container, false);
        meterIdEditText = view.findViewById(R.id.meterIdInput);
        tokenEditText = view.findViewById(R.id.tokenInput);
        submitButton = view.findViewById(R.id.submitBtn);
        responseTextView = view.findViewById(R.id.responseView);

        submitButton.setOnClickListener(v -> {
            String meterId = meterIdEditText.getText().toString().trim();
            String token = tokenEditText.getText().toString().trim();
            if (!meterId.isEmpty() && !token.isEmpty()) {
                new Thread(() -> sendTokenToServer(meterId, token)).start();
            } else {
                requireActivity().runOnUiThread(() ->
                        responseTextView.setText("Please enter both Meter ID and Token")
                );
            }
        });

        return view;
    }

    private void sendTokenToServer(String meterId, String token) {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("meter_id", meterId);
            jsonRequest.put("token", token);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonRequest.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int statusCode = conn.getResponseCode();
            InputStream stream = (statusCode == HttpURLConnection.HTTP_OK)
                    ? conn.getInputStream() : conn.getErrorStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, "utf-8")
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject response = new JSONObject(sb.toString());
            if ("success".equals(response.optString("status"))) {
                int newBalance = response.getInt("new_balance");
                int oldBalance = MeterData.getInstance().getBalance();
                int credit = newBalance - oldBalance;

                MeterData.getInstance().setMeterId(meterId);
                MeterData.getInstance().setBalance(newBalance);
                MeterData.getInstance().setLastToken(token);
                MeterData.getInstance().addToHistory(
                        "Token " + token + " added " + credit + " units."
                );

                updateUI("Token accepted! +" + credit + " units");
            } else {
                String msg = response.optString("message", "Unknown error");
                updateUI("Error: " + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateUI("Error: " + e.getMessage());
        }
    }

    private void updateUI(String message) {
        requireActivity().runOnUiThread(() ->
                responseTextView.setText(message)
        );
    }
}
