package com.mi1.duitku.Common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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
}
