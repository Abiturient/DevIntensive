package com.softdesign.devintensive.utils;

import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.softdesign.devintensive.R;

/**
 * Created by Alex on 09.07.2016.
 */

public class UserInfoValidator implements TextWatcher {

    private static final String TAG = ConstantManager.TAG_PREFIX + " text validator ";

    private static final int MAX_DIGITS     = 11;
    private static final int MAX_SYMBOLS    = 16;

    private int mTimerDuration = ConstantManager.ERROR_TIMER_LENGTH_NORMAL;

    private static final Handler ERROR_STOP_HANDLER = new Handler();
    private static final int ERROR_TIMER_LENGTH = 3000;
    private Boolean handlerIsAlive = false;

    private final Resources mResources;
    private final EditText mEditText;
    private final TextInputLayout mTextInputLayout;

    public UserInfoValidator(EditText editText, TextInputLayout textInputLayout) {
        this.mResources = editText.getContext().getResources();
        this.mEditText = editText;
        this.mTextInputLayout = textInputLayout;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (mEditText.getId()) {
            case R.id.phone_et:
                validatePhoneNumber(s);
                break;

            case R.id.email_et:
                validateEmail(s);
                break;

            case R.id.profile_vk_et:
                validateVK(s);
                break;

            case R.id.repository_et:
                validateGitHub(s);
                break;
        }
    }

    /**
     * displays or removes error at current TextInputLayout
     *
     * @param isError   - if true - displays an error, if false - removes
     * @param errorType - error message
     */
    private void errorHandler(Boolean isError, final String errorType) {
        if (isError) {
            mTextInputLayout.setErrorEnabled(true);
            mTextInputLayout.setError(errorType);

            ERROR_STOP_HANDLER.removeCallbacksAndMessages(null);
            ERROR_STOP_HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextInputLayout.setError(null);
                    mTextInputLayout.setErrorEnabled(false);
                }
            }, ERROR_TIMER_LENGTH);
        } else {
            mTextInputLayout.setError(null);
            mTextInputLayout.setErrorEnabled(false);
        }
    }

    /**
     * validates and reformats cellphone number into ru.locale format
     * @param s editText text
     */
    private void validatePhoneNumber(Editable s) {
        String errorText;

        String phoneNumber          = s.toString().trim();
        Boolean setCursorPosToEnd   = false;
        Boolean isError             = false;
        String formattedPhone       = "";
        int cursorPosition          = mEditText.getSelectionStart();

        if (cursorPosition == phoneNumber.length()) {
            setCursorPosToEnd = true;
        }

        //to validate
        errorText = isValidPhoneNumber(phoneNumber);
        if (errorText != null) isError = true;

        //to reformat
        int a = 0;
        if (!phoneNumber.startsWith("+")) a = 1;
        if (phoneNumber.length() > (MAX_SYMBOLS - a)) {
            if (setCursorPosToEnd || cursorPosition == 0 ||
                    (phoneNumber.startsWith("7") || phoneNumber.startsWith("8")) && cursorPosition == 1 ||
                    (phoneNumber.startsWith("(7") || phoneNumber.startsWith("(8")) && cursorPosition == 2) {
                cursorPosition = cursorPosition + 1;
                phoneNumber = phoneNumber.substring(0, MAX_SYMBOLS + a); //отсекаем лишние символы в конце номера
            } else {
                //to delete input symbol
                phoneNumber = phoneNumber.substring(0, cursorPosition - 1) + phoneNumber.substring(cursorPosition);
                cursorPosition -= 1;
            }
        }

        //format to +7(ххх)ххх-хх-хх
        phoneNumber = phoneNumber.replaceAll("\\D", "");

        if (phoneNumber.length() > 0) {
            formattedPhone = getFormattedPhone(phoneNumber);
        }

        //phonenumber to EditText and change cursor position
        mEditText.removeTextChangedListener(this);
        mEditText.setText(formattedPhone);
        if (setCursorPosToEnd || cursorPosition > mEditText.getText().toString().length()) {
            mEditText.setSelection(mEditText.getText().toString().length());
        } else {
            mEditText.setSelection(cursorPosition);
        }
        mEditText.addTextChangedListener(this);
        errorHandler(isError, errorText);
    }

    /**
     * validates cellphone number if it matches ru.locale format
     * @param phoneNumber cellphone number
     * @return error message if phone is not valid; null if it is valid;
     */
    private String isValidPhoneNumber(String phoneNumber) {

        String checkPhone = phoneNumber.replaceAll("[\\(\\)\\-\\+]", "");

        //error: input only digits
        if (checkPhone.matches("(\\d*\\D\\d*)*")) {
            //mTimerDuration = ConstantManager.ERROR_TIMER_LENGTH_LONG;
            return mResources.getString(R.string.error_editText_phone_only_digits);
        }

        //error: <>11 digits
        if (checkPhone.length() < MAX_DIGITS) {
            //*mTimerDuration = ConstantManager.ERROR_TIMER_LENGTH_LONG;
            return mResources.getString(R.string.error_editText_phone_symbols_11);
        } else {
            //error: phonnumber prefix 7 or 8
            if (!checkPhone.startsWith("7") && !checkPhone.startsWith("8")) {
                //mTimerDuration = ConstantManager.ERROR_TIMER_LENGTH_LONG;
                return mResources.getString(R.string.error_editText_phone_prefix);
            }
            //error: >11 digits
            if (checkPhone.length() > MAX_DIGITS) {
                //mTimerDuration = ConstantManager.ERROR_TIMER_LENGTH_SHORT;
                return mResources.getString(R.string.error_editText_phone_wrong);
            }
        }
        return null;
    }

    /**
     * reformats cellphone number into +7(***)***-**-**
     * @param phoneNumber cellphone number
     * @return String reformatted phone
     */
    private String getFormattedPhone(String phoneNumber) {
        String countryCode          = "";
        String mobileOperatorCode   = "";

        String firstNumberPart  = "";
        String secondNumberPart = "";
        String thirdNumberPart  = "";

        int index = 0;
        //country code
        if (phoneNumber.startsWith("7") || phoneNumber.startsWith("8")) {
            countryCode = "+7";
            index = 1;
        }

        if (index < phoneNumber.length()) {
            phoneNumber = phoneNumber.substring(index);

            if (phoneNumber.length() <= 3) {
                mobileOperatorCode = phoneNumber.substring(0, phoneNumber.length());

            } else {
                mobileOperatorCode = phoneNumber.substring(0, 3);
                if (phoneNumber.length() <= 6) {
                    firstNumberPart = phoneNumber.substring(3, phoneNumber.length());

                } else {
                    firstNumberPart = phoneNumber.substring(3, 6);
                    if (phoneNumber.length() <= 8) {
                        secondNumberPart = phoneNumber.substring(6, phoneNumber.length());
                    } else {
                        secondNumberPart = phoneNumber.substring(6, 8);
                        thirdNumberPart = phoneNumber.substring(8, phoneNumber.length());
                    }
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (countryCode.length() > 0) {
            stringBuilder.append(countryCode);
        }
        if (mobileOperatorCode.length() > 0) {
            stringBuilder.append("(");
            stringBuilder.append(mobileOperatorCode);
        }
        if (firstNumberPart.length() > 0) {
            stringBuilder.append(")");
            stringBuilder.append(firstNumberPart);
        }
        if (secondNumberPart.length() > 0) {
            stringBuilder.append("-");
            stringBuilder.append(secondNumberPart);
        }
        if (thirdNumberPart.length() > 0) {
            stringBuilder.append("-");
            stringBuilder.append(thirdNumberPart);
        }

        return stringBuilder.toString();
    }



    /**
     * validates email if it matches format ***@**.**
     * @param s editText text
     */
    private void validateEmail(Editable s) {

        String email = s.toString().trim();
        Boolean isValid = isValidEmail(email);
        errorHandler(!isValid, mResources.getString(R.string.error_editText_email));
    }

    /**
     *
     * @param email   email
     * @return true if it matches format ***@**.**
     */
    public static boolean isValidEmail(String email) {
        String pattern = "^[\\w\\+\\.\\%\\-]{3,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{1,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25})+$";
        return !TextUtils.isEmpty(email) && email.matches(pattern);
    }



    /**
     * cuts https(s) and validates vk link to match format vk.com/***
     * @param s editText text
     */
    private void validateVK(Editable s) {
        String vk = s.toString().trim().toLowerCase();
        Boolean isValid = false;
        int a;
        if ((a = vk.indexOf("vk.com")) != -1) {
            if (a != 0) {
                String newString = s.toString().trim().substring(a);
                mEditText.removeTextChangedListener(this);
                mEditText.setText(newString);
                mEditText.addTextChangedListener(this);
            } else {
                isValid = isValidVK(vk);
            }
        }
        errorHandler(!isValid, mResources.getString(R.string.error_editText_vk));
    }

    /**
     *
     * @param vk vk link
     * @return true if it matches format vk.com/***
     */
    public static boolean isValidVK(String vk) {
        String pattern = "^vk.com\\/\\w{3,256}$";
        return !TextUtils.isEmpty(vk) && vk.matches(pattern);
    }



    /**
     * cuts https(s) and validates gitHub link to match format gitHub.com/***
     * @param s editText text
     */
    private void validateGitHub(Editable s) {

        String gh = s.toString().trim().toLowerCase();
        Boolean isValid = false;
        int a;
        if ((a = gh.indexOf("github.com")) != -1) {
            if (a != 0) {
                String newString = s.toString().trim().substring(a);
                mEditText.removeTextChangedListener(this);
                mEditText.setText(newString);
                mEditText.addTextChangedListener(this);
            } else {
                isValid = isValidGitHub(gh);
            }
        }
        errorHandler(!isValid, mResources.getString(R.string.error_editText_gitHub));
    }

    /**
     *
     * @param s gitHub link
     * @return true if it matches format gitHub.com/***
     */
    public static boolean isValidGitHub(String s) {
        String pattern = "^github.com\\/\\w{3,256}(\\/\\w+)?$";
        return !TextUtils.isEmpty(s) && s.matches(pattern);
    }


}
