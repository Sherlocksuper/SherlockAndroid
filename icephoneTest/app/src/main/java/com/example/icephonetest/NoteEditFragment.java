package com.example.icephonetest;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteEditFragment extends Fragment {
    public Spinner itemSpinner;

    public View view;
    public ImageView noteedit_backrow;
    public Button editsave_button;

    public EditText titleEdit;
    public EditText contentEdit;
    FragmentManager manager;
    FragmentTransaction transaction;
    public SharedPreferences preferences;
    public List<PublicResult> myDataList;
    public List<String> spinnerList;
    public PublicResult publicResult;

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
        titleEdit = view.findViewById(R.id.editNote_titleEdit);
        contentEdit = view.findViewById(R.id.editNote_contentEdit);
        myDataList = new ArrayList<>();

        preferences = getContext().getSharedPreferences("MyAppData", MODE_PRIVATE);
    }

    public void initNoteEditData() {
        manager = getParentFragmentManager();
        transaction = manager.beginTransaction();

        Bundle args = getArguments();
        if (args != null) publicResult = (PublicResult) args.getSerializable("public_result");
        setItemSpinner();
        if (publicResult != null) initEditShow();

    }

    public void initNoteEditListener() {

        noteedit_backrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeListFragment homeListFragment = new HomeListFragment();
                transaction.replace(R.id.fragment_container, homeListFragment);
                transaction.commit();
            }
        });

        //保存按钮，添加保存逻辑
        editsave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(titleEdit.getText()) || TextUtils.isEmpty(contentEdit.getText())) {
                    Toast.makeText(getContext(), "输入不可为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (publicResult == null) {
                        addNewItem();
                    } else {
                        updateNote();
                    }
                }
            }
        });

    }

    public void setItemSpinner() {

        SharedPreferences preferences = getContext().getSharedPreferences("MyAppData", MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet("spinnerList", new HashSet<String>()); // 读取Set数据
        spinnerList = new ArrayList<String>(stringSet); // 将Set转换为List


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(adapter);

    }

    public void initEditShow() {
        titleEdit.setText(publicResult.title);
        contentEdit.setText(publicResult.content);
        itemSpinner.setSelection(spinnerList.indexOf(publicResult.kind));
    }

    public void addNewItem() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd"); // 定义格式化器
        Date date = new Date(); // 获取当前日期
        String dateString = formatter.format(date); // 按照格式化器转化当前日期为字符串
        //待添加的content
        PublicResult addContent = new PublicResult(titleEdit.getText().toString(), contentEdit.getText().toString(), dateString, itemSpinner.getSelectedItem().toString());
        // 从SharedPreferences中检索JSON字符串
        String json = preferences.getString("publicResultList", "");
        // 将JSON字符串转换回List<PublicResult>
        Gson gson = new Gson();
        Type type = new TypeToken<List<PublicResult>>() {
        }.getType();
        myDataList = gson.fromJson(json, type);
        if (myDataList == null) myDataList = new ArrayList<>();
        myDataList.add(addContent);
        // 将List<PublicResult>转换为JSON字符串
        String jsonPut = gson.toJson(myDataList);
        // 将JSON字符串保存到SharedPreferences中
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("publicResultList", jsonPut);
        editor.apply();

        Toast.makeText(getActivity(), "成功添加", Toast.LENGTH_SHORT).show();
        HomeListFragment homeListFragment = new HomeListFragment();
        transaction.replace(R.id.fragment_container, homeListFragment);
        transaction.commit();
    }

    public void updateNote() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd"); // 定义格式化器
        Date date = new Date(); // 获取当前日期
        String dateString = formatter.format(date); // 按照格式化器转化当前日期为字符串
        //待添加的content
        PublicResult updateData = new PublicResult(titleEdit.getText().toString(), contentEdit.getText().toString(), dateString, itemSpinner.getSelectedItem().toString());
        // 从SharedPreferences中检索JSON字符串
        String json = preferences.getString("publicResultList", "");
        // 将JSON字符串转换回List<PublicResult>
        Gson gson = new Gson();
        Type type = new TypeToken<List<PublicResult>>() {
        }.getType();
        myDataList = gson.fromJson(json, type);
        if (myDataList == null) myDataList = new ArrayList<>();
        else Log.d("TAG", "updateNote: "+myDataList.contains(publicResult));;
        // 将List<PublicResult>转换为JSON字符串
        String jsonPut = gson.toJson(myDataList);
        // 将JSON字符串保存到SharedPreferences中
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("publicResultList", jsonPut);
        editor.apply();

        Toast.makeText(getActivity(), "成功添加", Toast.LENGTH_SHORT).show();
        HomeListFragment homeListFragment = new HomeListFragment();
        transaction.replace(R.id.fragment_container, homeListFragment);
        transaction.commit();
    }
}