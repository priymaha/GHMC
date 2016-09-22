package com.example.priyanka.ghmc.viewbinder.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.activity.HomeActivity;
import com.example.priyanka.ghmc.activity.SignupActivity;
import com.example.priyanka.ghmc.utils.UIValidator;

/**
 * Created by sahul on 9/20/16.
 */

public class LoginActivityVB extends BaseActivityViewBinder{


    private EditText mUserNameET, mPasswordET;
    private Button mLoginB;
    private TextView mSignupText2TV;
    private TextView mForgotPasswordTV;
    private AlphaAnimation viewClick;
    private EditText mEmailET;
    private Button mOkB;

    public LoginActivityVB(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void initContentView() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_login, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
    }

    @Override
    public void initViews() {

        mUserNameET = (EditText) contentView.findViewById(R.id.login_username_et);
        mPasswordET = (EditText) contentView.findViewById(R.id.login_password_et);
        mLoginB = (Button) contentView.findViewById(R.id.login_button);
        mForgotPasswordTV = (TextView) contentView.findViewById(R.id.login_forgot_password_tv);
        mSignupText2TV = (TextView) contentView.findViewById(R.id.login_sign_up2_tv);

        try {
            activity.getSupportActionBar().hide();
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {
        if (mLoginB != null) {
            mLoginB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInClicked();
                }
            });
        }

        if (mPasswordET != null) {
            mPasswordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        signInClicked();
                    }
                    return false;
                }
            });
        }

        if (mSignupText2TV != null) {
            mSignupText2TV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(viewClick);
                    Intent intent = new Intent(activity, SignupActivity.class);
                    activity.startActivity(intent);
                }
            });
        }

        if (mForgotPasswordTV != null) {
            mForgotPasswordTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(viewClick);
                    showResetPassword();
                }
            });
        }
    }

    @Override
    public void onInitFinish() {
        viewClick = new AlphaAnimation(1F, 0.6F);
        mPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordET.setTypeface(mUserNameET.getTypeface());
        mSignupText2TV.setPaintFlags(mSignupText2TV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mForgotPasswordTV.setPaintFlags(mSignupText2TV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void signInClicked() {
        hideKeyBoard();
        if (validData()) {

            /*if (NetworkInfo.isNetworkAvailable(context)) {
                if (AppUtils.isInternetAccessible(activity)) {
                    signInUser();
                } else {
                    AppUtils.showNoInternetAccessibleAlert(context);
                }
            } else {
                AppUtils.showWiFiSettingsAlert(context);
            }*/
            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
            finishActivity();
        }
    }

    private boolean validData() {
        return !UIValidator.isError(context, mUserNameET, mPasswordET);
    }

    @Override
    public boolean handleOptionsSelected(int itemId) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

    }

    @Override
    public void onBackPressed() {

    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPasswordET.getWindowToken(), 0);
    }

    private void showResetPassword() {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_forgot_password);

            mEmailET = (EditText) dialog.findViewById(R.id.fp_alert_email_et);
            mOkB = (Button) dialog.findViewById(R.id.fp_alert_submit_b);

        mOkB.setBackground(activity.getResources().getDrawable(R.drawable.status_alert_button_background));
        GradientDrawable bgShape = (GradientDrawable) mOkB.getBackground();
        bgShape.setColor(Color.parseColor("#105D91"));
        mEmailET.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (UIValidator.isValidEmail(mEmailET)) {
                        dialog.dismiss();
                        //resetPassword(mEmailET.getText().toString().trim());
                    } else {
                        mEmailET.setError(context.getResources().getString(R.string.signup_validation_email_invalid));
                    }
                }
                return false;
            }
        });

        mOkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIValidator.isValidEmail(mEmailET)) {
                    dialog.dismiss();
                    //resetPassword(mEmailET.getText().toString().trim());
                } else {
                    mEmailET.setError(context.getResources().getString(R.string.signup_validation_email_invalid));
                }
            }
        });
        dialog.show();
    }

}
