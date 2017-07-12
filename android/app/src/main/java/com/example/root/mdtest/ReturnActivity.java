package com.example.root.mdtest;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.mdtest.Common.AppClient;
import com.example.root.mdtest.Common.LoginStatus;
import com.example.root.mdtest.Model.UblResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private TextInputLayout idInputLayout;
    private EditText mIdInput;
    private Button mReturnBtn;
    private View layout;
    private View secret_block;
    private TextView mSecretText;

    private String[] errorMessage={
            "",
            "有伞没还，借伞失败",
            "锁上有伞",
            "锁不存在",
            "信用分不够",
            "错误的行为",
            "用户名查询错误",
            "数据库连接错误",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        title=(TextView)findViewById(R.id.toolbar_title);
        mIdInput=(EditText)findViewById(R.id.lock_id);
        mReturnBtn=(Button)findViewById(R.id.return_btn);
        layout=(View)findViewById(R.id.layout);
        idInputLayout=(TextInputLayout)findViewById(R.id.textInputId);
        secret_block=(View)findViewById(R.id.secret_block);
        mSecretText=(TextView)findViewById(R.id.secret);

        init();
    }

    private void init(){
        //toolbar
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.return_title));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //edit change linstener
        mReturnBtn.setEnabled(isFormFinished());
        mIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                mReturnBtn.setEnabled(isFormFinished());
            }
        });

        //input area
        showSecretInput();

        //btn listerner
        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LoginStatus.getInstance().isLogin()){
                    startActivity(new Intent(ReturnActivity.this,LoginActivity.class));
                }

                int id=Integer.parseInt(mIdInput.getText().toString());
                AppClient.httpService.handleUbl(LoginStatus.getInstance().getUser().mail,1,id).enqueue(new Callback<UblResult>() {
                    @Override
                    public void onResponse(Call<UblResult> call, Response<UblResult> response) {
                        UblResult result=response.body();

                        if(result.status==0){
                            //success
                            String secret=result.retVal;
                            showSecretResult(secret);
                        }
                        else{
                            String error=errorMessage[result.status];
                            Snackbar.make(layout,error,Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UblResult> call, Throwable t) {
                        Snackbar.make(layout,"network error",Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean isFormFinished(){
        String input=mIdInput.getText().toString();
        return !input.isEmpty();
    }

    private void showSecretInput(){
        secret_block.setVisibility(View.GONE);
        idInputLayout.setVisibility(View.VISIBLE);
        mReturnBtn.setVisibility(View.VISIBLE);
    }

    private void showSecretResult(String secret){
        mSecretText.setText(secret);

        secret_block.setVisibility(View.VISIBLE);
        idInputLayout.setVisibility(View.GONE);
        mReturnBtn.setVisibility(View.GONE);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.secret_anim);
        secret_block.startAnimation(animation);
    }
}
