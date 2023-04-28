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
import android.widget.ImageView;
import android.widget.TextView;

public class NoteDetailFragment extends Fragment {
    public ImageView notedetail_backrow;
    public ImageView notedetail_editpen;
    public View viewND;
    FragmentManager manager;
    FragmentTransaction transaction;
    public PublicResult publicResult;
    public TextView noteDetailTitle;
    public TextView noteDetailContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewND = inflater.inflate(R.layout.fragment_notedetail, container, false);
        initNoteDetailData();
        initNoteDetailView();
        initNoteDetailListener();
        return viewND;
    }

    public void initNoteDetailView() {
        notedetail_backrow = viewND.findViewById(R.id.notedetail_backrows);
        notedetail_editpen = viewND.findViewById(R.id.notedetail_penedit);

        noteDetailTitle = viewND.findViewById(R.id.notedetails_title);
        noteDetailContent = viewND.findViewById(R.id.notedetail_content);

        if (publicResult != null) {
            noteDetailTitle.setText(publicResult.title);
            noteDetailContent.setText(publicResult.content);
        }
    }

    public void initNoteDetailData() {
        manager = getParentFragmentManager();
        transaction = manager.beginTransaction();
        Bundle args = getArguments();
        if (args != null) publicResult = (PublicResult) args.getSerializable("public_result");
    }


    public void initNoteDetailListener() {
        notedetail_backrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeListFragment homeListFragment = new HomeListFragment();
                transaction.replace(R.id.fragment_container, homeListFragment);
                transaction.commit();
            }
        });

        notedetail_editpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("public_result",publicResult);

                NoteEditFragment noteEditFragment = new NoteEditFragment();
                noteEditFragment.setArguments(args);


                transaction.replace(R.id.fragment_container, noteEditFragment);
                transaction.commit();
            }
        });
    }
}