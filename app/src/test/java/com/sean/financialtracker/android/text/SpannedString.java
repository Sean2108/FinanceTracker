package com.sean.financialtracker.android.text;

public class SpannedString {

    private String mText;

    public SpannedString(CharSequence source) {
        mText = source.toString();
    }
    public final int length() {
        return mText.length();
    }
}
