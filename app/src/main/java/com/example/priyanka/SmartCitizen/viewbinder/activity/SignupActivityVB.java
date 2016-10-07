package com.example.priyanka.SmartCitizen.viewbinder.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.utils.AppUtils;
import com.example.priyanka.SmartCitizen.utils.UIValidator;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.helper.NetworkInfo;

import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.HashMap;

/**
 * Created by sahul on 9/20/16.
 */

public class SignupActivityVB extends BaseActivityViewBinder{

    private EditText mFirstNameET;
    private EditText mLastNameET;
    private EditText mEmailET;
    private EditText mPhoneET;
    private EditText mPasswordET;
    private EditText mConPasswordET;
    private Button mSignupB;
    private ProgressDialog dialog;
    private LinearLayout mAlertLL;
    private TextView mTitleTV;
    private TextView mStatusTV;
    private ImageView mStatusIV;
    private Button mOkB;

    public SignupActivityVB(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void initContentView() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_sign_up, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
    }


    @Override
    public void initViews() {
        mFirstNameET = (EditText) contentView.findViewById(R.id.signup_fn_et);
        mLastNameET = (EditText) contentView.findViewById(R.id.signup_ln_et);
        mEmailET = (EditText) contentView.findViewById(R.id.signup_ea_et);
        mPhoneET = (EditText) contentView.findViewById(R.id.signup_pn_et);
        mPasswordET = (EditText) contentView.findViewById(R.id.signup_password_et);
        mConPasswordET = (EditText) contentView.findViewById(R.id.signup_con_password_et);
        mSignupB = (Button) contentView.findViewById(R.id.signup_b);

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
        if (mSignupB != null) {
            mSignupB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signupClicked();
                }
            });
        }

        if (mPhoneET != null) {
            mPhoneET.addTextChangedListener(new PhoneNumberFormattingTextWatcher("IN"));
        }

        if(mConPasswordET != null){
            mConPasswordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        signupClicked();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onInitFinish() {
        mPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mConPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordET.setTypeface(mPhoneET.getTypeface());
        mConPasswordET.setTypeface(mPhoneET.getTypeface());
    }

    public void signupClicked() {
        hideKeyBoard();
        if (validData()) {
            if (NetworkInfo.isNetworkAvailable(context)) {
                if (AppUtils.isInternetAccessible(activity)) {
                    signupUser();
                } else {
                    AppUtils.showNoInternetAccessibleAlert(context);
                }
            } else {
                AppUtils.showWiFiSettingsAlert(context);
            }
        }
    }

    private boolean validData() {
        return !UIValidator.isError(context, mFirstNameET, mLastNameET, mEmailET, mPhoneET, mPasswordET, mConPasswordET);
    }

    private void signupUser() {
        dialog = ProgressDialog.show(activity, "", "Authenticating");
        try {
            String domainName = context.getResources().getString(R.string.domain_name);
            String firstName = mFirstNameET.getText().toString().trim();
            String lastName = mLastNameET.getText().toString().trim();
            String emailAddress = mEmailET.getText().toString().trim();
            String repPhoneNum = mPhoneET.getText().toString().trim().replaceAll("[()\\s-]", "");
            String ownerPhoneNum = "1234567890";
            String password = mPasswordET.getText().toString().trim();
            HashMap<String,Object> parameters = new HashMap<>();
            HashMap<String,String> extras = new HashMap<>();
            parameters.put("firstName",firstName);
            parameters.put("lastName",lastName);
            parameters.put("enterprise",ownerPhoneNum);
            parameters.put("type","owner");
            extras.put("phoneNumber",repPhoneNum);
            parameters.put("extras",extras);
            String url = UrlBuilder.getUrl(context);
            KeepTrax keepTrax = KeepTraxImpl.getInstance(context, url, UrlBuilder.getApiKey(context));
            keepTrax.signup(domainName, emailAddress, password,parameters, new VoidCallback() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    showSuccessAlert();
                }
                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    showShortToast(t.getMessage());
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            showShortToast("Signup failed");
        }
    }

    private void showSuccessAlert() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.signup_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

            mAlertLL = (LinearLayout) dialog.findViewById(R.id.signup_alert_ll);
            mTitleTV = (TextView) dialog.findViewById(R.id.signup_alert_status_tv);
            mStatusTV = (TextView) dialog.findViewById(R.id.signup_alert_status_tv);
            mStatusIV = (ImageView) dialog.findViewById(R.id.signup_alert_status_iv);
            mOkB = (Button) dialog.findViewById(R.id.signup_alert_ok_b);

        mOkB.setBackground(activity.getResources().getDrawable(R.drawable.status_alert_button_background));
        GradientDrawable bgShape = (GradientDrawable) mOkB.getBackground();
        bgShape.setColor(Color.parseColor("#105D91"));
        mOkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();
            }
        });

        dialog.show();

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
        imm.hideSoftInputFromWindow(mPhoneET.getWindowToken(), 0);
    }
}
