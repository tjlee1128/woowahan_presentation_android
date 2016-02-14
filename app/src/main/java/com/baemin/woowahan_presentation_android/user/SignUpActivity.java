package com.baemin.woowahan_presentation_android.user;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.AuthenticationModel;
import com.baemin.woowahan_presentation_android.model.TeamModel;
import com.baemin.woowahan_presentation_android.model.UserModel;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.network.UserService;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.activity_sign_up_toolbar_include)
    View toolbarView;

    @Bind(R.id.activity_sign_up_email_et)
    EditText emailEditText;
    @Bind(R.id.activity_sign_up_password_et)
    EditText passwordEditText;
    @Bind(R.id.activity_sign_up_fullname_et)
    EditText fullnameEditText;
    @Bind(R.id.activity_sign_up_team_spinner)
    Spinner teamSpinner;
    private boolean hasEmail;
    private boolean hasPassword;
    private boolean hasFullname;
    private boolean hasTeam;

    @Bind(R.id.activity_sign_up_sign_up_btn)
    Button signupButton;

    // VAR
    private List<String> spinnerList;
    private List<TeamModel> teamModelList;
    private UserModel userModel;
    private AuthenticationModel authenticationModel;

    @OnClick(R.id.activity_sign_up_sign_up_btn)
    public void onClick() {
        userModel.setEmail(emailEditText.getText().toString());
        userModel.setPassword(passwordEditText.getText().toString());
        userModel.setFullname(fullnameEditText.getText().toString());
        userModel.setTeam_id(teamModelList.get(teamSpinner.getSelectedItemPosition()).getId());

        UserService userService = ServiceGenerator.createService(UserService.class);
        Call<AuthenticationModel> call = userService.signupUser(userModel);
        Callback<AuthenticationModel> callback = new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Response<AuthenticationModel> response, Retrofit retrofit) {
                authenticationModel = response.body();

                PreferencesManager.getInstance().setAccessToken(authenticationModel.getAccess_token());
                PreferencesManager.getInstance().setUser(authenticationModel.getUser());
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        call.enqueue(callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_top_bottom_enter, R.anim.start_top_bottom_exit);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        userModel = new UserModel();

        initializeToolBar();
        initializeFlag();
        initializeSpinner();
        addTextChangedListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_top_bottom_enter, R.anim.end_top_bottom_exit);
    }

    private void initializeToolBar() {
        ((TextView)toolbarView.findViewById(R.id.layout_toolbar_close_title_tv)).setText("SIGN UP");
        ((RelativeLayout)toolbarView.findViewById(R.id.layout_toolbar_close_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeFlag() {
        hasEmail = false;
        hasPassword = false;
        hasFullname = false;
        hasTeam = false;
    }

    private void initializeSpinner() {
        spinnerList = new ArrayList<>();
        teamModelList = PreferencesManager.getInstance().getTeams();
        for (int i = 0; i < teamModelList.size(); i++) {
            spinnerList.add(teamModelList.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        teamSpinner.setAdapter(arrayAdapter);
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hasTeam = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

                setSignUpButtonStatus(hasEmail, hasPassword, hasFullname, hasTeam);
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

                setSignUpButtonStatus(hasEmail, hasPassword, hasFullname, hasTeam);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    hasFullname = true;
                } else {
                    hasFullname = false;
                }

                setSignUpButtonStatus(hasEmail, hasPassword, hasFullname, hasTeam);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setSignUpButtonStatus(boolean hasEmail, boolean hasPassword, boolean hasFullname, boolean hasTeam) {
        if (hasEmail && hasPassword && hasFullname && hasTeam) {
            signupButton.setEnabled(true);
        } else {
            signupButton.setEnabled(false);
        }
    }
}
