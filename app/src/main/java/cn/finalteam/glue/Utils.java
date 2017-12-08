package cn.finalteam.glue;

import android.text.TextUtils;

import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by carlshen on 17-12-8.
 */

public class Utils {

    public static MediaType getMediaType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        if (fileName.lastIndexOf("png") > 0 || fileName.lastIndexOf("PNG") > 0) {
            mediaType = MediaType.parse("image/png; charset=UTF-8");
        } else if (fileName.lastIndexOf("jpg") > 0 || fileName.lastIndexOf("JPG") > 0
                || fileName.lastIndexOf("jpeg") > 0 || fileName.lastIndexOf("JPEG") > 0) {
            mediaType = MediaType.parse("image/jpeg; charset=UTF-8");
        }
        return mediaType;
    }

    public static String getFullUrl(String url, Map<String, String> params) {
        StringBuffer urlFull = new StringBuffer();
        urlFull.append(url);
        if (urlFull.indexOf("?", 0) < 0 && params != null && params.size() > 0) {
            urlFull.append("?");
        }
        if (params != null && params.size() > 0) {
            int flag = 0;
            for (Map.Entry<String, String> part : params.entrySet()){
                String key = part.getKey();
                String value = part.getValue();
                urlFull.append(key).append("=").append(value);
                if (++flag != params.size()){
                    urlFull.append("&");
                }
            }
        }
        return urlFull.toString();
    }
}
