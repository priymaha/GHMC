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
import com.example.priyanka.SmartCitizen.activity.MainActivity;
import com.example.priyanka.SmartCitizen.activity.SignupActivity;
import com.example.priyanka.SmartCitizen.callback.LoadEventCallback;
import com.example.priyanka.SmartCitizen.controller.EntryController;
import com.example.priyanka.SmartCitizen.utils.AppPreferences;
import com.example.priyanka.SmartCitizen.utils.AppUtils;
import com.example.priyanka.SmartCitizen.utils.CameraHelper;
import com.example.priyanka.SmartCitizen.utils.Constants;
import com.example.priyanka.SmartCitizen.utils.Globals;
import com.example.priyanka.SmartCitizen.utils.UIValidator;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.cachemanager.PageToken;
import com.keeptraxinc.cachemanager.dao.Document;
import com.keeptraxinc.cachemanager.dao.Enterprise;
import com.keeptraxinc.cachemanager.dao.Event;
import com.keeptraxinc.cachemanager.dao.EventDao;
import com.keeptraxinc.cachemanager.dao.Setting;
import com.keeptraxinc.cachemanager.dao.SettingDao;
import com.keeptraxinc.cachemanager.query.LimitClause;
import com.keeptraxinc.cachemanager.query.ListCallback;
import com.keeptraxinc.cachemanager.query.WhereClause;
import com.keeptraxinc.cachemanager.query.WhereSimple;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.helper.DateUtils;
import com.keeptraxinc.utils.helper.NetworkInfo;
import com.keeptraxinc.utils.logger.Logger;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahul on 9/20/16.
 */

public class LoginActivityVB extends BaseActivityViewBinder implements LoadEventCallback{


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
    private EntryController mEntryController;

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
                    AppPreferences.setBooleanValue("LOGIN", true, context);
                    gotoHome();
//                    getEvents();
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

    private void getEnterpriseEvents(final Enterprise enterprise) {
        WhereClause wc = WhereSimple.le(EventDao.Properties.Start.name, DateUtils.getISOTime(System.currentTimeMillis()))
                .and(WhereSimple.ge(EventDao.Properties.End.name, DateUtils.getISOTime(System.currentTimeMillis()))

                        .and(WhereSimple.ne(EventDao.Properties.Status.name, Constants.EVENT_STATUS_COMPLETED)));
        enterprise.getEvents(wc, null, null, new ListCallback<Event>() {
            @Override
            public void onSuccess(PageToken pageToken, List<Event> list) {
                if (list != null && !list.isEmpty()) {
//                    populateSampleData (list);
                    gotoHome();

                } else {


                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void getEvents() {
        if (keepTrax.getUser() != null) {

            keepTrax.getUser().getEnterprise(new ObjectCallback<Enterprise>() {
                @Override
                public void onSuccess(Enterprise enterprise) {
                    if (enterprise != null) {
                        getEnterpriseEvents(enterprise);
                    } else {

                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }
    private void initSdk() {
        keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(activity), UrlBuilder.getApiKey(activity));
        saveAppPreferences();
        savePhotoPathSettings();
        savePhotoSizeSettings();
    }

    private void gotoHome() {
        mEntryController = new EntryController(activity);
        mEntryController.init(this);

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

    private void savePhotoPathSettings() {
        KeepTrax keepTrax = KeepTraxImpl.getInstance(context, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        WhereClause wc = WhereSimple.eq(SettingDao.Properties.Module.name, Document.MODULE)
                .and(WhereSimple.eq(SettingDao.Properties.Key.name, Document.DOCUMENT_PATH));
        keepTrax.getUser().getSettings(wc, null, new LimitClause(1), new ListCallback<Setting>() {
            @Override
            public void onSuccess(PageToken pageToken, List<Setting> list) {
                if (list != null && !list.isEmpty()) {
                } else {
                    File file = CameraHelper.createDirIfNotExists("", context);
                    createSettings(Document.DOCUMENT_PATH, file.getAbsolutePath() + "/");
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

    }

    private void savePhotoSizeSettings() {
        KeepTrax keepTrax = KeepTraxImpl.getInstance(context, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        WhereClause wc = WhereSimple.eq(SettingDao.Properties.Module.name, Document.MODULE)
                .and(WhereSimple.eq(SettingDao.Properties.Key.name, Document.MAX_DOCUMENT_SIZE));
        keepTrax.getUser().getSettings(wc, null, new LimitClause(1), new ListCallback<Setting>() {
            @Override
            public void onSuccess(PageToken pageToken, List<Setting> list) {
                if (list != null && !list.isEmpty()) {
                } else {
                    createSettings(Document.MAX_DOCUMENT_SIZE, 200000+"");
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

    }

    private void createSettings(String key, String value) {
        KeepTrax keepTrax = KeepTraxImpl.getInstance(context, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        Setting setting = (Setting) keepTrax.createModel(Setting.NAME);
        setting.setModule(Document.MODULE);
        setting.setKey(key);
        setting.setValue(value);
        setting.save(null);
    }


    @Override
    public void loadListOfShows() {
        if (Globals.dialog!=null && Globals.dialog.isShowing())
            Globals.dialog.dismiss();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        finishActivity();
    }
}
