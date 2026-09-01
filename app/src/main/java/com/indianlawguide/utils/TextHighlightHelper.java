package com.indianlawguide.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHighlightHelper {

    public static CharSequence highlightText(String fullText, String query) {
        if (fullText == null) return "";
        if (query == null || query.trim().isEmpty()) return fullText;

        SpannableString spannable = new SpannableString(fullText);
        String cleanQuery = Pattern.quote(query.trim());
        Pattern pattern = Pattern.compile(cleanQuery, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fullText);

        while (matcher.find()) {
            spannable.setSpan(
                new BackgroundColorSpan(Color.parseColor("#FFF176")),
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            spannable.setSpan(
                new ForegroundColorSpan(Color.parseColor("#000000")),
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return spannable;
    }
}
