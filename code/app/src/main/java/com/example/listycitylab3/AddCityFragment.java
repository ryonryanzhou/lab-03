package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
    }

    private AddCityDialogListener listener;
    private City editCity;

    public static AddCityFragment newInstance(City city){
        AddCityFragment fragment  = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof AddCityDialogListener){
        listener = (AddCityDialogListener) context;
    }else{
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Retrieve city from arguments if editing
        if (getArguments() != null) {
            editCity = (City) getArguments().getSerializable("city");
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Pre-fill fields if editing
        if (editCity != null) {
            editCityName.setText(editCity.getName());
            editProvinceName.setText(editCity.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(editCity != null ? "Edit City" : "Add City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (editCity != null) {
                        // Update existing city
                        editCity.setName(cityName);
                        editCity.setProvince(provinceName);
                        if (listener instanceof MainActivity) {
                            ((MainActivity) listener).updateListView();
                        }
                    } else {
                        // Add new city
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }




}
