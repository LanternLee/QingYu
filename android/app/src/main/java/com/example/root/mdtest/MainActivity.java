package com.example.root.mdtest;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.root.mdtest.Common.AppClient;

import com.example.root.mdtest.Adapter.menuAdapter;
import com.example.root.mdtest.Common.LoginStatus;
import com.example.root.mdtest.Model.LoginResult;
import com.example.root.mdtest.Model.MenuItem;
import com.example.root.mdtest.Model.User;
import com.example.root.mdtest.Model.Weather;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecycleList;
    private menuAdapter adapter;
    private ArrayList<MenuItem> menuItems;
    private TextView title;
    private ImageView weatherIcon;
    private TextView weatherText;
    private TextView score;
    private TextView has_borrow;
    private TextView ncount;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycleList=(RecyclerView)findViewById(R.id.recycleList);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        title=(TextView)findViewById(R.id.toolbar_title);
        weatherText=(TextView)findViewById(R.id.weatherText);
        weatherIcon=(ImageView)findViewById(R.id.weatherIcon);
        score=(TextView)findViewById(R.id.credit);
        has_borrow=(TextView)findViewById(R.id.has_borrow);
        ncount=(TextView)findViewById(R.id.all_borrow);
        layout=(View)findViewById(R.id.layout);

        init();
    }

    private void initData(){
        menuItems=new ArrayList<MenuItem>(4);
        MenuItem item1=new MenuItem();
        MenuItem item2=new MenuItem();
        MenuItem item3=new MenuItem();

        item1.menuText="我要借伞";
        item1.menuIcon=R.mipmap.borrow_icon;
        item2.menuText="我要还伞";
        item2.menuIcon=R.mipmap.return_icon;
        item3.menuText="退出登录";
        item3.menuIcon=R.mipmap.exit_icon;

        menuItems.add(item1);
        menuItems.add(item2);
        menuItems.add(item3);
    }

    private void init(){
        //user message
        if(!LoginStatus.getInstance().isLogin()){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }

        //init weather info
        AppClient.httpService.getNowWeather(Weather.APPID,Weather.LOCATION,Weather.LANGUAGE,Weather.UNIT).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather weather=response.body();
                if(weather.status==null){
                    Snackbar.make(layout,"get weather fail "+weather.status,Snackbar.LENGTH_LONG).show();
                }

                weatherText.setText(weather.results.get(0).now.text);
                int code=Integer.parseInt(weather.results.get(0).now.code);
                if(code>=0&&code<=38){
                    weatherIcon.setImageDrawable(getDrawable(Weather.WEATHERICON[code]));
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Snackbar.make(layout,"network error get weather fail",Snackbar.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

        //init person info
        User user=LoginStatus.getInstance().getUser();
        score.setText(user.score+"");
        has_borrow.setText(user.has_borrow+"");
        ncount.setText(user.ncount+"");

        //init recycler list
        initData();
        adapter=new menuAdapter(this,menuItems);
        adapter.setClickListener(new menuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("onItemClick",""+position);

                switch (position){
                    case 0:
                        //借伞
                        startActivity(new Intent(MainActivity.this,BorrowActivity.class));
                        break;
                    case 1:
                        //还伞
                        startActivity(new Intent(MainActivity.this,ReturnActivity.class));
                        break;
                    case 2:
                        //退出
                        LoginStatus.getInstance().setUser(null);
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        break;
                    case 6:
                        //测试
                        Log.d("Test","click");

                        AppClient.httpService.getBorrowUsers(0).enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                List<User> users=response.body();
                                Log.d("Retrofit","test success "+users.size());
                                Toast.makeText(MainActivity.this,"User[0] "+users.get(0).mail,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {
                                t.printStackTrace();
                                Log.d("Retrofit","test fail 0.0");
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
        mRecycleList.setLayoutManager(new LinearLayoutManager(this));
        mRecycleList.setAdapter(adapter);

        //init toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title.setText("QingYu");
    }
}
