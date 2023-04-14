package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentCinema extends Fragment {
    public FragmentCinema() {
    }

    boolean hasLogged;
    private RecyclerView recyclerView;
    private View view;
    private ArrayList<MainpageCinemaItem> hotCinemaItemList = new ArrayList<>();
    private ArrayList<MainpageCinemaItem> moreCinemaItemList = new ArrayList<>();
    private ArrayList<MainpageCinemaItem> addCinemaItemList = new ArrayList<>();
    private CinemaRecyclerViewAdapter madapter;
    private Integer currentPage = 1, totalPage = 2;
    private Boolean isMoreRecord = true;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView2;
    private SmartRefreshLayout smartRefreshLayout;
    private Thread t1, t2;
    int page = 1;
    String token;
    Thread tread = Thread.currentThread();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences pref = getContext().getSharedPreferences("data", MODE_PRIVATE);
        hasLogged = pref.getBoolean("hasLogged", false);
        if (hasLogged) {

            initHotMovieData();


            view = inflater.inflate(R.layout.fragment_ciname_logged, container, false);

            smartRefreshLayout = view.findViewById(R.id.refresh_parent);
            smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this.getContext()));
            smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this.getContext()));


            recyclerView = view.findViewById(R.id.haslog_showcinema);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            //延时刷新等待子线程
            try {
                tread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    Toast.makeText(getContext(), "正在刷新", Toast.LENGTH_SHORT).show();

                    smartRefreshLayout.finishRefresh(2000);
                    Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();

                }
            });
            smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                    if (isMoreRecord || currentPage >= totalPage) {
                        smartRefreshLayout.finishRefresh(200);
                        Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        //加载currenpage下一页
                        page = currentPage + 1;
                        whileLodeMore(page);
                        try {
                            tread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
                        smartRefreshLayout.finishRefresh(200);
                    }
                }
            });

            madapter = new CinemaRecyclerViewAdapter(getContext(), hotCinemaItemList, moreCinemaItemList);
            recyclerView.setAdapter(madapter);
            return view;
        } else {
            return inflater.inflate(R.layout.fragment_ciname_notlog, container, false);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (hasLogged) {

        } else {
            notlog_toLogin();
        }
    }

    //去登录按钮
    public void notlog_toLogin() {
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        Button cinema_goTolog = this.getView().findViewById(R.id.notlot_CinemagotoLog);
        cinema_goTolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }


    public void initHotMovieData() {
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                    token = pref.getString("token", "");
                    OkHttpClient hotCinemaClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .addHeader("token", token)
                            .get()
                            .url("https://test.xiandejia.com:8888/douban_server/movie/listUpcomingMovie")
                            .build();
                    Response response = hotCinemaClient.newCall(request).execute();
                    String responseHotData = response.body().string();

                    Log.d("hello", "handleMessage: responseHotData"+responseHotData);
                    OkHttpClient normalCinemaClient = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .addHeader("token", token)
                            .get()
                            .url("https://test.xiandejia.com:8888/douban_server/movie/pageMovie?pageNum=1&pageSize=40")
                            .build();
                    Response response1 = normalCinemaClient.newCall(request1).execute();
                    String responseNormalData = response1.body().string();
                    Log.d("TAG", "run: " + responseNormalData);
                    parseJSONWithJSONObject(responseHotData, responseNormalData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

    }

    public void parseJSONWithJSONObject(String responseData1, String responseData2) {

        final applyCinema hotcinemas = new Gson().fromJson(responseData1, applyCinema.class);
        final pageCinema normalcinemas = new Gson().fromJson(responseData2, pageCinema.class);


        hotCinemaItemList = hotcinemas.getData();
        moreCinemaItemList = normalcinemas.getData();
    }
    //求出page页数
    public void whileLodeMore(Integer page) {
        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String responseNormalData = "";
                try {
                    SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                    token = pref.getString("token", "");
                    OkHttpClient moreOkhttpClien = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .addHeader("token", token)
                            .get()
                            .url("https://test.xiandejia.com:8888/douban_server/movie/pageMovie?pageNum=" + page + "&pageSize=40")
                            .build();
                    Response response1 = moreOkhttpClien.newCall(request1).execute();
                    responseNormalData = response1.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<MainpageCinemaItem> listtt = new ArrayList<>();
                final pageCinema normalcinemas = new Gson().fromJson(responseNormalData, pageCinema.class);
                listtt = normalcinemas.getData();

                currentPage = normalcinemas.getCurPage();
                totalPage = normalcinemas.getTotPages();
                isMoreRecord = normalcinemas.isMoreRecord;
                addCinemaItemList = listtt;
            }
        });
        t2.start();
    }

    //获取电影资源类mainpage
    public class applyCinema {
        private Integer id;
        private String message;
        private boolean success;
        private ArrayList<MainpageCinemaItem> data;

        public applyCinema() {
        }

        public applyCinema(Integer id, String message, boolean success, ArrayList<MainpageCinemaItem> data) {
            this.id = id;
            this.message = message;
            this.success = success;
            this.data = data;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public ArrayList<MainpageCinemaItem> getData() {
            return data;
        }

        public void setData(ArrayList<MainpageCinemaItem> data) {
            this.data = data;
        }

        class MovioVO {
            String name;
            String picUrl;
            BigDecimal grade;
        }
    }

    public class pageCinema {
        private String message;
        private Boolean success;
        private ArrayList<MainpageCinemaItem> data;
        private Integer totPages;
        private Integer curPage;
        private Long totElements;
        private Boolean isMoreRecord;

        public pageCinema() {
        }

        public pageCinema(String message, boolean success, ArrayList<MainpageCinemaItem> data, Integer totalPages, Integer currentPage, Long totalElement, boolean isMoreRecord) {
            this.message = message;
            this.success = success;
            this.data = data;
            this.totPages = totalPages;
            this.curPage = currentPage;
            this.totElements = totalElement;
            this.isMoreRecord = isMoreRecord;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public ArrayList<MainpageCinemaItem> getData() {
            return data;
        }

        public void setData(ArrayList<MainpageCinemaItem> data) {
            this.data = data;
        }

        public Integer getTotPages() {
            return totPages;
        }

        public void setTotPages(Integer totPages) {
            this.totPages = totPages;
        }

        public Integer getCurPage() {
            return curPage;
        }

        public void setCurPage(Integer curPage) {
            this.curPage = curPage;
        }

        public Long getTotElements() {
            return totElements;
        }

        public void setTotElements(Long totElements) {
            this.totElements = totElements;
        }

        public boolean isMoreRecord() {
            return isMoreRecord;
        }

        public void setMoreRecord(boolean moreRecord) {
            isMoreRecord = moreRecord;
        }
    }
    //压缩
    public class dataCollect {
        ArrayList<MainpageCinemaItem> hotCinemadata;
        ArrayList<MainpageCinemaItem> moreCinemadata;
    }
}