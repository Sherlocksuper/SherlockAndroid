package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.Selector;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailedCinemaThings extends AppCompatActivity {

    ImageView im_arrowBack;
    ImageView bu_collectStar;
    Button bu_collectJudege;
    Selector selector;
    Integer id;
    String grade;
    String token;
    Boolean isfavor;
    applyFavor getFavor1;
    Thread thread = Thread.currentThread();
    applyCinemaInfo cinemaInfo;
    commentThings commentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //获取token并且接受id
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        token = pref.getString("token", "");
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        grade = bundle.getString("grade");
        getMovieInfo();

        setContentView(R.layout.activity_detailed_cinema_things);

        setArrowBackListen();//返回监听
        collectListener();//收藏监听
        getMovieFavor();//收藏状态
        getComments();//测试获取评论
        setFoldAndShow();
    }

    //设置返回按钮监听
    public void setArrowBackListen() {
        im_arrowBack = findViewById(R.id.detailed_arrowback);
        im_arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //设置收藏按钮的监听
    public void collectListener() {
        bu_collectJudege = findViewById(R.id.detailed_collectButton);

        bu_collectJudege.setOnClickListener(view -> {
            if (bu_collectJudege.getText().equals("收藏")) {
                bu_collectJudege.setText("已收藏");
                bu_collectJudege.setSelected(true);
            } else if (bu_collectJudege.getText().equals("已收藏")) {
                bu_collectJudege.setText("收藏");
                bu_collectJudege.setSelected(false);
            }
            refreshFavor();
        });
    }

    //刷新收藏状态（后端数据有问题）
    public void refreshFavor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseData = "未初始化";
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .addHeader("token", token)
                            .get()
                            .url("https://test.xiandejia.com:8888/douban_server/movie/updateFavor?movieId=" + id)
                            .build();

                    Response response = okHttpClient.newCall(request).execute();
                    responseData = response.body().string();
                    Log.d("TAG", "run: " + responseData + "refreshFavor");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取是否被收藏（后端有问题）
    public void getMovieFavor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .addHeader("token", token)
                            .get()
                            .url("https://test.xiandejia.com:8888/douban_server/movie/isFavor?movieId=" + id)
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    getFavor1 = new Gson().fromJson(responseData, applyFavor.class);
                    isfavor = getFavor1.isData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isfavor) bu_collectJudege.setSelected(true);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //接受并且设置相关信息
    private Handler handlerMovieInfo = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            applyCinemaInfo cinemaInfo2 = (applyCinemaInfo) msg.obj;
            TextView tv_cinemaInfo = findViewById(R.id.detailed_cinemaInfo);
            TextView tv_cinemaName = findViewById(R.id.detailed_cinemaName);
            TextView tv_cinemaIntro = findViewById(R.id.detailed_simpleContext1);
            TextView tv_cinemaGrade = findViewById(R.id.detailed_cinemaGrade);
            TextView tv_detaileGrade = findViewById(R.id.detailed_detailGrade);
            TextView tv_actorName1 = findViewById(R.id.detailed_actorName1);
            TextView tv_actorName2 = findViewById(R.id.detailed_actorName2);
            TextView tv_actorName3 = findViewById(R.id.detailed_actorName3);

            //设置图片资源
            ImageView im_cinemaPic = findViewById(R.id.detailed_picture);
            ImageView im_actorPic1 = findViewById(R.id.detailed_actorPicture1);
            ImageView im_actorPic2 = findViewById(R.id.detailed_actorPicture2);
            ImageView im_actorPic3 = findViewById(R.id.detailed_actorPicture3);
            ImageView im_stagePic1 = findViewById(R.id.detailed_stagePicture1);
            ImageView im_stagePic2 = findViewById(R.id.detailed_stagePicture2);
            ImageView im_stagePic3 = findViewById(R.id.detailed_stagePicture3);



            String info = cinemaInfo2.getData().showPlace + " / " + cinemaInfo2.getData().lable + " / "
                    + cinemaInfo2.getData().showDate + " / " + cinemaInfo2.getData().lastTime;
            String detailGrade = "2分人数：" + cinemaInfo2.getData().gradeInfo[0] + "\n"
                    + "4分人数：" + cinemaInfo2.getData().gradeInfo[1] + "\n"
                    + "6分人数：" + cinemaInfo2.getData().gradeInfo[2] + "\n"
                    + "8分人数：" + cinemaInfo2.getData().gradeInfo[3] + "\n"
                    + "10分人数：" + cinemaInfo2.getData().gradeInfo[4];


            tv_cinemaInfo.setText(info);
            tv_cinemaName.setText(cinemaInfo2.getData().name);
            tv_cinemaIntro.setText(cinemaInfo2.getData().introduction);
            tv_cinemaGrade.setText(grade);
            tv_detaileGrade.setText(detailGrade);

            tv_actorName1.setText(cinemaInfo2.getData().getActorList().get(0).nameC);
            tv_actorName2.setText(cinemaInfo2.getData().getActorList().get(1).nameC);

            Glide.with(DetailedCinemaThings.this).load(cinemaInfo2.getData().picUrl).into(im_cinemaPic);
            Glide.with(DetailedCinemaThings.this).load(cinemaInfo2.getData().getActorList().get(0).picUrl).into(im_actorPic1);
            Glide.with(DetailedCinemaThings.this).load(cinemaInfo2.getData().getActorList().get(1).picUrl).into(im_actorPic2);

            Glide.with(DetailedCinemaThings.this).load(cinemaInfo2.getData().stagePhotoUrl[0]).into(im_stagePic1);
            Glide.with(DetailedCinemaThings.this).load(cinemaInfo2.getData().stagePhotoUrl[1]).into(im_stagePic2);
            Glide.with(DetailedCinemaThings.this).load(cinemaInfo2.getData().stagePhotoUrl[2]).into(im_stagePic3);

            if (cinemaInfo2.getData().getActorList().size() == 3) {
                tv_actorName3.setText(cinemaInfo2.getData().getActorList().get(2).nameC);
                Glide.with(DetailedCinemaThings.this).load(cinemaInfo2.getData().getActorList().get(2).picUrl).into(im_actorPic3);
            }

            super.handleMessage(msg);
        }
    };

    public void getMovieInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("222333", "run: " + "88888");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .addHeader("token", token)
                            .get()
                            .url("https://test.xiandejia.com:8888/douban_server/movie/" + id)
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    cinemaInfo = new Gson().fromJson(responseData, applyCinemaInfo.class);
                    handlerMovieInfo.obtainMessage(3, cinemaInfo).sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handlerCommentInfo = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            commentThings lists = (commentThings) msg.obj;
            RecyclerView mRecyclerView = findViewById(R.id.commentRecyclerView);
            CinemaCommentRecyclerViewAdapter mAdapter;
            mAdapter = new CinemaCommentRecyclerViewAdapter(DetailedCinemaThings.this, commentInfo);
            LinearLayoutManager layoutManager = new LinearLayoutManager(DetailedCinemaThings.this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(layoutManager);
            Log.d("TAG", "handleMessage: zhixingdaozhelilelele");
            super.handleMessage(msg);
        }
    };

    public void getComments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .addHeader("token", token)
                            .get()
                            .url("https://test.xiandejia.com:8888/douban_server/remark/listRemark/?id=" + id)
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    commentInfo = new Gson().fromJson(responseData, commentThings.class);
                    handlerCommentInfo.obtainMessage(1, commentInfo).sendToTarget();
                    Log.d("5201314", "run: "+responseData);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //用来获取电影详情信息
    //包括movieIO，ActorVO
    public class applyCinemaInfo {
        private String message;
        private Boolean success;
        private MovieInfoVO data;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public MovieInfoVO getData() {
            return data;
        }

        public void setData(MovieInfoVO data) {
            this.data = data;
        }

        class MovieInfoVO {
            private String name;
            private String picUrl;
            private BigDecimal grade;
            private String introduction;
            private Integer[] gradeInfo = new Integer[5];
            private ArrayList<ActorVO> actorList;
            private String[] stagePhotoUrl = new String[3];
            private String showPlace;
            private String lable;
            private String showDate;
            private String lastTime;

            public MovieInfoVO() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public BigDecimal getGrade() {
                return grade;
            }

            public void setGrade(BigDecimal grade) {
                this.grade = grade;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public Integer[] getGradeInfo() {
                return gradeInfo;
            }

            public void setGradeInfo(Integer[] gradeInfo) {
                this.gradeInfo = gradeInfo;
            }

            public ArrayList<ActorVO> getActorList() {
                return actorList;
            }

            public void setActorList(ArrayList<ActorVO> actorList) {
                this.actorList = actorList;
            }

            public String[] getStagePhotoUrl() {
                return stagePhotoUrl;
            }

            public void setStagePhotoUrl(String[] stagePhotoUrl) {
                this.stagePhotoUrl = stagePhotoUrl;
            }

            public String getShowPlace() {
                return showPlace;
            }

            public void setShowPlace(String showPlace) {
                this.showPlace = showPlace;
            }

            public String getLable() {
                return lable;
            }

            public void setLable(String lable) {
                this.lable = lable;
            }

            public String getShowDate() {
                return showDate;
            }

            public void setShowDate(String showDate) {
                this.showDate = showDate;
            }

            public String getLastTime() {
                return lastTime;
            }

            public void setLastTime(String lastTime) {
                this.lastTime = lastTime;
            }
        }

        class ActorVO {
            Integer id;
            String nameC;
            String nameE;
            String picUrl;
            String introduction;
        }
    }

    //获取演员信息类
    public class applyActor {
        private String message;
        private Boolean success;
        private ActorVO data;

        public applyActor() {
        }

        public applyActor(String message, Boolean success, ActorVO data) {
            this.message = message;
            this.success = success;
            this.data = data;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public ActorVO getData() {
            return data;
        }

        public void setData(ActorVO data) {
            this.data = data;
        }

        class ActorVO {
            Integer id;
            String nameC;
            String nameE;
            String picUrl;
            String introduce;
        }
    }

    //获取是否被收藏
    public class applyFavor {
        private String message;
        private boolean success;
        private boolean data;

        public applyFavor() {
        }

        public applyFavor(String message, boolean success, boolean data) {
            this.message = message;
            this.success = success;
            this.data = data;
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

        public boolean isData() {
            return data;
        }

        public void setData(boolean data) {
            this.data = data;
        }
    }

    //创建评论类
    public class commentThings {
        private String message;
        private Boolean success;
        private ArrayList<RemarkVO> data;


        public commentThings(String message, Boolean success, ArrayList<RemarkVO> data) {
            this.message = message;
            this.success = success;
            this.data = data;
        }

        public commentThings() {
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public ArrayList<RemarkVO> getData() {
            return data;
        }

        public void setData(ArrayList<RemarkVO> data) {
            this.data = data;
        }

        class RemarkVO {
            Integer id;
            Integer userId;
            String userName;
            String userPicUrl;
            Integer grade;
            String content;
            Integer favorPeople;

        }
    }

    public void   setFoldAndShow(){
        TextView tv_foldselect = findViewById(R.id.detailed_tvTelescopicTwo);
        TextView tv_simplecontent = findViewById(R.id.detailed_simpleContext1);
        tv_foldselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_foldselect.getText().equals("...展开")){
                    tv_simplecontent.setMaxLines(100);
                    tv_foldselect.setText("...收起");
                }else if (tv_foldselect.getText().equals("...收起")){
                    tv_simplecontent.setMaxLines(3);
                    tv_foldselect.setText("...展开");
                }
            }
        });
    }
}