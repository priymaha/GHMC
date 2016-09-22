package com.example.priyanka.ghmc.utils;

import android.content.Context;
import android.widget.EditText;

import com.example.priyanka.ghmc.R;

/**
 * Created by sahul on 9/21/16.
 */

public class UIValidator {

    public static boolean isError(Context context, EditText etUserName, EditText password) {

        boolean result = false;

        if (password.getText().toString().trim().isEmpty()) {
            password.setError(context.getResources().getString(R.string.signup_validation_password_empty));
            result = true;
        }else if (password.getText().toString().trim().length() < 6) {
            password.setError(context.getResources().getString(R.string.signup_validation_pass_size));
            result = true;
        }

        if (etUserName.getText().toString().trim().isEmpty()) {
            etUserName.setError(context.getResources().getString(R.string.signup_validation_email_empty));
            result = true;
        }else if(!isValidEmail(etUserName)){
            etUserName.setError(context.getResources().getString(R.string.signup_validation_email_invalid));
            result = true;
        }
        return result;
    }


    public static boolean isError(Context context, EditText firstName, EditText lastName, EditText email, EditText phone,
                                  EditText password, EditText conPassword) {

        boolean result = false;
        String phoneNum = phone.getText().toString().replaceAll("[()\\s-]", "");

        if (phone.getText().toString().isEmpty()) {
            phone.setError(context.getResources().getString(R.string.login_validation_pnum_empty));
            result = true;
        } else if (phoneNum.length() != 10) {
            phone.setError(context.getResources().getString(R.string.login_validation_pnum_size));
            result = true;
        }

        String firstNameET = firstName.getText().toString().trim();
        if (firstNameET.isEmpty()) {
            firstName.setError(context.getResources().getString(R.string.signup_validation_fname_empty));
            result = true;
        }

        String lastNameET = lastName.getText().toString().trim();
        if (lastNameET.isEmpty()) {
            lastName.setError(context.getResources().getString(R.string.signup_validation_lname_empty));
            result = true;
        }

        if (password.getText().toString().trim().isEmpty()) {
            password.setError(context.getResources().getString(R.string.signup_validation_password_empty));
            result = true;
        }else if (password.getText().toString().trim().length() < 6) {
            password.setError(context.getResources().getString(R.string.signup_validation_pass_size));
            result = true;
        }

        if (conPassword.getText().toString().trim().isEmpty()) {
            conPassword.setError(context.getResources().getString(R.string.signup_validation_cpassword_empty));
            result = true;
        }else if (!conPassword.getText().toString().trim().equals(password.getText().toString().trim())){
            conPassword.setError(context.getResources().getString(R.string.signup_validation_cpassword_mismatch));
            result = true;
        }

        String emailET = email.getText().toString().trim();
        if (emailET.isEmpty()) {
            email.setError(context.getResources().getString(R.string.signup_validation_email_empty));
            result = true;
        }else if(!isValidEmail(email)){
            email.setError(context.getResources().getString(R.string.signup_validation_email_invalid));
            result = true;
        }
        return result;
    }

    public static boolean isValidEmail(EditText email) {
        return email != null && email.getText() != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches();
    }

    public static boolean eventIdSearch (Context context,EditText eventId){
        boolean result = false;
        if (eventId.getText().toString().isEmpty()) {
            eventId.setError(context.getResources().getString(R.string.store_name_validation));
            result = true;
        }
        return result;
    }
}
