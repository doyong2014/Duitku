package com.mi1.duitku.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by owner on 3/8/2017.
 */

public class CommonFunction {

    public static final String md5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatNumbering(String amount)
    {
        Locale currentLocale = new Locale("id", "ID");
        Double a = Double.valueOf(amount);
        NumberFormat nf = NumberFormat.getNumberInstance(currentLocale);
        DecimalFormat df = (DecimalFormat)nf;

        return df.format(a);
    }

    private static final Set<String> abbrs = new HashSet<String>(Arrays.asList(
            new String[] {"PLN","XL", "HP", "PAM", "BPJS"}
    ));

    public static String CapitalizeSentences(String sentence) {
        StringBuilder result = new StringBuilder();
        String[] words = sentence.split("\\s");

        for(int i=0,l=words.length;i<l;++i) {
            //give space between words
            if(i>0 && i < l)
                result.append(" ");

            //
            if (abbrs.contains(words[i].toUpperCase()))
                result.append(words[i]);
            else {
                result.append(Character.toUpperCase(words[i].charAt(0)))
                        .append(words[i].substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    public static void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;

                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }

            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf"));
            }

        } catch (Exception e) {
        }
    }
}
