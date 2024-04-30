package com.meng.mediatool.tools;

/**
 * @author 司徒灵羽
 */

import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;

public class MNetwork {

    public static final String UA = "jvav";
    public static String httpPost(String url, String cookie, Map<String,String> headers, Object... params) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.userAgent(UA);
        if (headers != null) {
            connection.headers(headers);
        }
        if (cookie != null) {
            connection.cookies(cookieToMap(cookie));
        }
        connection.ignoreContentType(true).method(Connection.Method.POST);
        for (int i = 0;i < params.length;i += 2) {
            connection.data(String.valueOf(params[i]), String.valueOf(params[i + 1]));
        }
        Connection.Response response = null;
        response = connection.execute();
        if (response.statusCode() != 200) {
            return String.valueOf(response.statusCode());
        }
        return response.body();
    }

    public static Map<String, String> cookieToMap(String value) {
        Map<String, String> map = new HashMap<>();
        String[] values = value.split("; ");
        for (String val : values) {
            String[] vals = val.split("=");
            if (vals.length == 2) {
                map.put(vals[0], vals[1]);
            } else if (vals.length == 1) {
                map.put(vals[0], "");
            }
        }
        return map;
    }

    public static String getRealUrl(String surl) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(surl).openConnection();
        conn.setInstanceFollowRedirects(false);
        return conn.getHeaderField("Location");
    }

    public static byte[] httpGetRaw(String url) {
        return httpGetRaw(url, null, null);
    }

    public static byte[] httpGetRaw(String url, String cookie) {
        return httpGetRaw(url, cookie, null);
    }

    public static byte[] httpGetRaw(String url, String cookie, String refer) {
        Connection.Response response = null;
        Connection connection;
        try {
            connection = Jsoup.connect(url);
            if (cookie != null) {
                connection.cookies(cookieToMap(cookie));
            }
            if (refer != null) {
                connection.referrer(refer);
            }
            connection.userAgent(UA);
            connection.maxBodySize(1024 * 1024 * 10).ignoreContentType(true).method(Connection.Method.GET);
            response = connection.execute();
            if (response.statusCode() != 200) {
                System.out.println(String.valueOf(response.statusCode()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return response.bodyAsBytes();
    }


    public static String httpGet(String url) {
        return httpGet(url, null, null);
    }

    public static String httpGet(String url, String cookie) {
        return httpGet(url, cookie, null);
    }

    public static String httpGet(String url, String cookie, String refer) {
        Connection.Response response = null;
        Connection connection;
        try {
            connection = Jsoup.connect(url);
            if (cookie != null) {
                connection.cookies(cookieToMap(cookie));
            }
            if (refer != null) {
                connection.referrer(refer);
            }
            connection.userAgent(UA);
            connection.maxBodySize(1024 * 1024 * 10).ignoreContentType(true).method(Connection.Method.GET);
            response = connection.execute();
            if (response.statusCode() != 200) {
                System.out.println(String.valueOf(response.statusCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body();
    }
}
