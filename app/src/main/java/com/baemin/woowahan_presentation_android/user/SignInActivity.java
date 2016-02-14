package com.baemin.woowahan_presentation_android.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.main.MainActivity;
import com.baemin.woowahan_presentation_android.model.AuthenticationModel;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.network.UserService;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignInActivity extends AppCompatActivity {

    @Bind(R.id.activity_sign_in_email_et)
    EditText emailEditText;
    @Bind(R.id.activity_sign_in_password_et)
    EditText passwordEditText;
    private boolean hasEmail;
    private boolean hasPassword;

    @Bind(R.id.activity_sign_in_sign_in_btn)
    Button signinButton;


    @OnClick({R.id.activity_sign_in_sign_in_btn, R.id.activity_sign_in_sign_up_tv})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.activity_sign_in_sign_in_btn:
                startSignIn();
                break;

            case R.id.activity_sign_in_sign_up_tv:
                intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        initializeFlag();
        addTextChangedListener();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_right_left_enter, R.anim.end_right_left_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    private void initializeFlag() {
        hasEmail = false;
        hasPassword = false;
    }

    private void addTextChangedListener() {
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    hasEmail = true;
                } else {
                    hasEmail = false;
                }

                setSignInButtonStatus(hasEmail, hasPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    hasPassword = true;
                } else {
                    hasPassword = false;
                }

                setSignInButtonStatus(hasEmail, hasPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setSignInButtonStatus(boolean hasEmail, boolean hasPassword) {
        if (hasEmail && hasPassword) {
            signinButton.setEnabled(true);
        } else {
            signinButton.setEnabled(false);
        }
    }

    private void startSignIn() {

        UserService userService = ServiceGenerator.createService(UserService.class);
        Call<AuthenticationModel> call = userService.signinUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
        Callback<AuthenticationModel> callback = new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Response<AuthenticationModel> response, Retrofit retrofit) {
                AuthenticationModel authenticationModel = response.body();

                PreferencesManager.getInstance().setAccessToken(authenticationModel.getAccess_token());
                PreferencesManager.getInstance().setUser(authenticationModel.getUser());

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        call.enqueue(callback);
    }
}
