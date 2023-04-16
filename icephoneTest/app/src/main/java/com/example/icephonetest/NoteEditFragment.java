package com.example.icephonetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class NoteEditFragment extends Fragment {
    public Spinner itemSpinner;

    public String[] spinnerList;
    public View view;
    public ImageView noteedit_backrow;
    public Button editsave_button;

    FragmentManager manager;
    FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_noteedit, container, false);
        initNoteEditView();
        initNoteEditData();
        initNoteEditListener();
        return view;
    }


    public void initNoteEditView() {
        noteedit_backrow = view.findViewById(R.id.noteedit_backrows);
        editsave_button = view.findViewById(R.id.noteedit_savebutton);
        itemSpinner = view.findViewById(R.id.noteedit_spinner);
    }

    public void initNoteEditData() {
        manager = getParentFragmentManager();
        transaction = manager.beginTransaction();

        setItemSpinner();
    }

    public void initNoteEditListener() {

        noteedit_backrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteDetailFragment noteDetailFragment = new NoteDetailFragment();
                transaction.replace(R.id.fragment_container, noteDetailFragment);
                transaction.commit();
            }
        });

        //保存按钮，添加保存逻辑
        editsave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "成功添加", Toast.LENGTH_SHORT).show();
                NoteDetailFragment noteDetailFragment = new NoteDetailFragment();
                transaction.replace(R.id.fragment_container, noteDetailFragment);
                transaction.commit();
            }
        });

    }

    public void setItemSpinner() {
        spinnerList = new String[]{"select item", "item1", "item2", "item3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(adapter);
    }
}