package com.example.root.mdtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.root.mdtest.Common.AppClient;

import com.example.root.mdtest.Adapter.menuAdapter;
import com.example.root.mdtest.Common.LoginStatus;
import com.example.root.mdtest.Model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecycleList;
    private menuAdapter adapter;
    private ArrayList<String> menuItems;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycleList=(RecyclerView)findViewById(R.id.recycleList);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        title=(TextView)findViewById(R.id.toolbar_title);

        init();
    }

    private void initData(){
        menuItems=new ArrayList<String>(4);
        menuItems.add("借伞");
        menuItems.add("还伞");
        menuItems.add("捐伞");
        menuItems.add("历史");
        menuItems.add("退出");
        menuItems.add("后端测试");
    }

    private void init(){
        //user message
        if(!LoginStatus.getInstance().isLogin()){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }

        //init recycler list
        initData();
        adapter=new menuAdapter(this,menuItems,LoginStatus.getInstance().getUser());
        adapter.setClickListener(new menuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("onItemClick",""+position);

                switch (position){
                    case 1:
                        //借伞
                        startActivity(new Intent(MainActivity.this,BorrowActivity.class));
                        break;
                    case 2:
                        //还伞
                        startActivity(new Intent(MainActivity.this,ReturnActivity.class));
                        break;
                    case 3:
                        //捐伞
                        break;
                    case 4:
                        //历史记录
                        break;
                    case 5:
                        //退出
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
