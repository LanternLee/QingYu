package com.example.root.mdtest;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.mdtest.Common.AppClient;
import com.example.root.mdtest.Common.LoginStatus;
import com.example.root.mdtest.Model.UblResult;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private TextInputLayout idInputLayout;
    private EditText mIdInput;
    private Button mBorrowBtn;
    private View layout;
    private View secret_block;
    private TextView mSecretText;

    private String[] errorMessage={
            "",
            "有伞没还，借伞失败",
            "锁上没有伞",
            "锁不存在",
            "信用分不够",
            "错误的行为",
            "用户名查询错误",
            "数据库连接错误",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        title=(TextView)findViewById(R.id.toolbar_title);
        idInputLayout=(TextInputLayout)findViewById(R.id.textInputId);
        mIdInput=(EditText)findViewById(R.id.lock_id);
        mBorrowBtn=(Button)findViewById(R.id.borrow_btn);
        layout=(View)findViewById(R.id.layout);
        secret_block=(View)findViewById(R.id.secret_block);
        mSecretText=(TextView)findViewById(R.id.secret);

        init();
    }

    private void init(){
        //init toolbar
        setSupportActionBar(toolbar);
        title.setText(getString(R.string.borrow_title));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //text listener
        mBorrowBtn.setEnabled(isFormFinished());
        mIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                mBorrowBtn.setEnabled(isFormFinished());
            }
        });

        //input area
        showSecretInput();
        showKeyboard();

        //btn listener
        mBorrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LoginStatus.getInstance().isLogin()){
                    startActivity(new Intent(BorrowActivity.this,LoginActivity.class));
                }

                closeKeyBoard();
                int id=Integer.parseInt(mIdInput.getText().toString());
                AppClient.httpService.handleUbl(LoginStatus.getInstance().getUser().mail,0,id).enqueue(new Callback<UblResult>() {
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
        mBorrowBtn.setVisibility(View.VISIBLE);
    }

    private void showSecretResult(String secret){
        mSecretText.setText(secret);

        secret_block.setVisibility(View.VISIBLE);
        idInputLayout.setVisibility(View.GONE);
        mBorrowBtn.setVisibility(View.GONE);

        Animation animation=AnimationUtils.loadAnimation(this,R.anim.secret_anim);
        secret_block.startAnimation(animation);
    }

    private void showKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mIdInput, 0);
            }
        }, 1000);
    }

    private void closeKeyBoard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
