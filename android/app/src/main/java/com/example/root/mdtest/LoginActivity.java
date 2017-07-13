package com.example.root.mdtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.mdtest.Common.AppClient;
import com.example.root.mdtest.Common.LoginStatus;
import com.example.root.mdtest.Common.NotifyHelper;
import com.example.root.mdtest.Model.LoginResult;
import com.example.root.mdtest.Model.UblMess;
import com.example.root.mdtest.Model.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    // UI references.
    private Toolbar toolbar;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mLoginBtn;
    private TextInputLayout mIdInputLayout;
    private TextInputLayout mPassInputLayout;
    private View layout;
    private String tail="@tst.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        mEmailInput=(EditText)findViewById(R.id.loginEmail);
        mPasswordInput=(EditText)findViewById(R.id.loginPassword);
        mLoginBtn=(Button)findViewById(R.id.loginBtn);
        mIdInputLayout=(TextInputLayout)findViewById(R.id.textInputEmail) ;
        mPassInputLayout=(TextInputLayout)findViewById(R.id.textInputPass);
        layout=(View)findViewById(R.id.layout);

        init();
    }

    private void init(){
        //toolbar
        toolbar.setTitle(R.string.login_title);
        setSupportActionBar(toolbar);

        //btn
        mLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();

                String mail=mEmailInput.getText().toString();
                String pass=mPasswordInput.getText().toString();

                AppClient.httpService.login(mail,pass).enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        LoginResult result=response.body();

                        if(result.status==0){
                            Intent intent=new Intent(LoginActivity.this.getApplicationContext(),MainActivity.class);
                            LoginStatus.getInstance().setUser(result.user);
                            startActivity(intent);
                        }
                        else{
                            Snackbar.make(layout,"Login fail "+result.status+" "+result.error,Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Snackbar.make(layout,"Login Fail",Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mLoginBtn.setEnabled(isFormFinish());

        //input area
        Timer timer = new Timer();
        timer.schedule(new TimerTask()   {
            public void run() {
                showKeyboard();
            }
        }, 1000 );

        mEmailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    return;
                }
                String id=mEmailInput.getText().toString();
                if(id.isEmpty()){
                    mIdInputLayout.setErrorEnabled(true);
                    mIdInputLayout.setError(getString(R.string.login_id_empty));
                    return;
                }
                else{
                    mIdInputLayout.setError("");
                    mIdInputLayout.setErrorEnabled(false);
                }

                if(id.endsWith(tail)){
                    return;
                }
                else{
                    mEmailInput.setText(id+tail);
                }

            }
        });

        mPasswordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    return;
                }

                String pass=mPasswordInput.getText().toString();
                if(pass.isEmpty()){
                    mPassInputLayout.setErrorEnabled(true);
                    mPassInputLayout.setError(getString(R.string.login_email_empty));
                    return;
                }
                else{
                    mPassInputLayout.setError("");
                    mPassInputLayout.setErrorEnabled(false);
                }
            }
        });

        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mLoginBtn.setEnabled(isFormFinish());
            }
        };

        mEmailInput.addTextChangedListener(watcher);
        mPasswordInput.addTextChangedListener(watcher);
    }

    private boolean isFormFinish(){
        String email=mEmailInput.getText().toString();
        String pass=mPasswordInput.getText().toString();

        return !email.isEmpty()&&!pass.isEmpty();
    }

    private void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mEmailInput, 0);
    }

    private void closeKeyBoard(){
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

