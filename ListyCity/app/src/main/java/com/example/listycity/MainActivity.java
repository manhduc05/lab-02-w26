package com.example.listycity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;

    Button btnAdd, btnDelete, btnConfirm;
    LinearLayout addRow;
    EditText inputCity;

    int selectedPos = AdapterView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        cityList   = findViewById(R.id.city_list);
        btnAdd     = findViewById(R.id.btn_add);
        btnDelete  = findViewById(R.id.btn_delete);
        btnConfirm = findViewById(R.id.btn_confirm);
        addRow     = findViewById(R.id.add_row);
        inputCity  = findViewById(R.id.input_city);

        // Data
        String[] cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"};
        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));

        // Adapter (use built-in activated row so taps highlight)
        cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, dataList);
        cityList.setAdapter(cityAdapter);
        cityList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // List item click = select
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            selectedPos = position;
            cityList.setItemChecked(position, true);
        });

        // ADD CITY: show input row
        btnAdd.setOnClickListener(v -> {
            addRow.setVisibility(View.VISIBLE);
            inputCity.requestFocus();
        });

        // CONFIRM: add to list
        btnConfirm.setOnClickListener(v -> {
            String name = inputCity.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }
            // Optional: prevent duplicates (case-insensitive)
            for (String s : dataList) {
                if (s.equalsIgnoreCase(name)) {
                    Toast.makeText(this, "City already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            dataList.add(name);
            cityAdapter.notifyDataSetChanged();
            inputCity.setText("");
            addRow.setVisibility(View.GONE);
            // clear selection after add
            clearSelection();
        });

        // DELETE CITY: remove selected
        btnDelete.setOnClickListener(v -> {
            if (selectedPos == AdapterView.INVALID_POSITION) {
                Toast.makeText(this, "Tap a city to select it first", Toast.LENGTH_SHORT).show();
                return;
            }
            dataList.remove(selectedPos);
            cityAdapter.notifyDataSetChanged();
            clearSelection();
        });
    }

    private void clearSelection() {
        cityList.clearChoices();
        cityList.requestLayout();
        selectedPos = AdapterView.INVALID_POSITION;
    }
}
