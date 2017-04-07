package com.mi1.duitku.Common;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mi1.duitku.Tab2.Holder.QBUsersHolder;
import com.quickblox.users.model.QBUser;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

    public static String formatNumbering(Double amount)
    {
        Locale currentLocale = new Locale("id", "ID");
        NumberFormat nf = NumberFormat.getNumberInstance(currentLocale);
        DecimalFormat df = (DecimalFormat)nf;

        return "Rp " + df.format(amount);
    }

    public static String formatNumbering(String amount)
    {
        Locale currentLocale = new Locale("id", "ID");
        Double a = Double.valueOf(amount);
        NumberFormat nf = NumberFormat.getNumberInstance(currentLocale);
        DecimalFormat df = (DecimalFormat)nf;

        return "Rp " + df.format(a);
    }

    public static String formatNumberingWithoutRP(String amount)
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

    public static String getSHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String getFormatedDate(String date) {

        String retDate = "";
        if (date == null) return null;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
        try {
            Date newDate = df.parse(date);
            df = new SimpleDateFormat("MM/dd/yyyy");
            retDate = df.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  retDate;
    }

    public static String getFormatedDate1(long milliseconds) {

        if (milliseconds == 0) {
            milliseconds = Calendar.getInstance().getTimeInMillis();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static String getFilePathFromUri(Context context, Uri uri) {

        String filePath = "";

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(column_index);
        }
        cursor.close();

        return filePath;
    }

    public static String createChatDialogName(ArrayList<Integer> qbUsers){
        List<QBUser> qbUsers1 = QBUsersHolder.getInstance().getUsersByIds(qbUsers);
        StringBuilder name = new StringBuilder();
        for(QBUser user: qbUsers1){
            name.append(user.getFullName()).append(" ");
        }
        if(name.length() > 30) {
            name = name.replace(30, name.length()-1, "...");
        }
        return name.toString();
    }
}
