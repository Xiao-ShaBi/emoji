package com.wzf.emojilib;


import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtils {

    public static final String patch = "[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]";
    public static final String patch2 = "\\[+[0-9a-fA-F]+]";

    /**
     * 将含有4字节字符的字符转为固定格式
     *
     * @param msg
     * @return
     */
    public static String getEmoji(String msg) {
        Pattern pattern = Pattern.compile(patch);
        Matcher matcher = pattern.matcher(msg);
        StringBuffer sb = new StringBuffer();
        try {
            while (matcher.find()) {

                byte[] count;
                count = URLDecoder.decode(matcher.group(), "UTF-8").getBytes();

                String cha = "[";
                for (int i = 0; i < count.length; i++) {
                    String s = Integer.toHexString(count[i]);
                    if (s.length() > 2) {
                        s = s.substring(s.length() - 2);
                    }
                    cha += s;
                }
                cha += "]";
                matcher.appendReplacement(sb, cha);
            }
        } catch (Exception e) {
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 将含有固定格式的字符串转化为四字节字符
     *
     * @param msg
     * @return
     */
    public static String getString(String msg) {
        Pattern pattern = Pattern.compile(patch2);
        Matcher matcher = pattern.matcher(msg);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String count = matcher.group();
            count = count.substring(1, count.length() - 1);
            matcher.appendReplacement(sb, hexStringToString(count));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
        }
        return s;
    }


}
