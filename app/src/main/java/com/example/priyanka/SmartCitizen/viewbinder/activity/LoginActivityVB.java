package com.example.priyanka.SmartCitizen.viewbinder.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;
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
import android.widget.TextView;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.activity.HomeActivity;
import com.example.priyanka.SmartCitizen.activity.SignupActivity;
import com.example.priyanka.SmartCitizen.utils.AppPreferences;
import com.example.priyanka.SmartCitizen.utils.AppUtils;
import com.example.priyanka.SmartCitizen.utils.Constants;
import com.example.priyanka.SmartCitizen.utils.UIValidator;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.helper.NetworkInfo;
import com.keeptraxinc.utils.logger.Logger;
import com.strongloop.android.loopback.callbacks.VoidCallback;

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
    private ProgressDialog dialog;
    private KeepTrax keepTrax;
    private final String LOG_TAG = getClass().getSimpleName();

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

        loadDataInViews();
    }

    public void loadDataInViews() {
        String email = AppPreferences.getValue(Constants.EMAIL_ID, context);
        Logger.debug(context, LOG_TAG, " email : " + email);
        if (email != null) {
            mUserNameET.setText(email);
        }
    }

    public void signInClicked() {
        hideKeyBoard();
        if (validData()) {

            if (NetworkInfo.isNetworkAvailable(context)) {
                if (AppUtils.isInternetAccessible(activity)) {
                    signInUser();
                } else {
                    AppUtils.showNoInternetAccessibleAlert(context);
                }
            } else {
                AppUtils.showWiFiSettingsAlert(context);
            }

        }
    }

    private void signInUser() {
//        gotoHome();
        dialog = ProgressDialog.show(activity, "", "Authenticating");
        try {
            String domainName = context.getResources().getString(R.string.domain_name);
            String userName = mUserNameET.getText().toString().trim();
            String password = mPasswordET.getText().toString().trim();
            String url = UrlBuilder.getUrl(context);
            KeepTrax keepTrax = KeepTraxImpl.getInstance(context, url, UrlBuilder.getApiKey(context));
            String deviceId = Settings.Secure.getString(activity.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String uuid = deviceId + userName;
            keepTrax.login(domainName, userName, password, uuid, new VoidCallback() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    initSdk();
                    AppPreferences.saveValue("LOGIN", true, context);
                    gotoHome();
                }

                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    showShortToast(t.getMessage());
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            showShortToast("Login failed");
        }
    }

    private void initSdk() {
        keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(activity), UrlBuilder.getApiKey(activity));
        saveAppPreferences();

    }

    private void gotoHome() {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
        finishActivity();
    }

    private void saveAppPreferences() {
        String userName = mUserNameET.getText().toString().trim();
        AppPreferences.saveValue(Constants.EMAIL_ID, userName, context);
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
                        resetPassword(mEmailET.getText().toString().trim());
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
                    resetPassword(mEmailET.getText().toString().trim());
                } else {
                    mEmailET.setError(context.getResources().getString(R.string.signup_validation_email_invalid));
                }
            }
        });
        dialog.show();
    }

    private void resetPassword(String email) {
        String url = UrlBuilder.getUrl(context);
        KeepTrax keepTrax = KeepTraxImpl.getInstance(context, url, UrlBuilder.getApiKey(context));
        keepTrax.resetPassword(email, new VoidCallback() {
            @Override
            public void onSuccess() {
                showResetPasswordSuccess();
            }

            @Override
            public void onError(Throwable t) {
                showShortToast(t.getMessage());
            }
        });
    }

    private void showResetPasswordSuccess() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(activity.getResources().getString(R.string.reset_success))
                .setMessage(activity.getResources().getString(R.string.help_info))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

}
