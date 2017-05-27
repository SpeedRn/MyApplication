package com;

import android.text.TextUtils;

import java.util.ArrayList;

public class PosterHolder {
    /**
     * 时间戳
     */
    public long timestamp;

    public ArrayList<Poster> data;

    public boolean isValid() {
        return data != null && data.size() > 0;
    }

    public void checkValid() {
        if (data != null) {
            for (int i = data.size() - 1; i >= 0; i--) {
                Poster p = data.get(i);
                if (p == null || TextUtils.isEmpty(p.abs) || TextUtils.isEmpty(p.image_url)) {
                    data.remove(i);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (isValid()) {
            sb.append("data-" + timestamp + "-{");
            for (Poster img : data) {
                sb.append("[" + img.abs + "," + img.image_url + "]");
            }
            sb.append("}");
        }
        return sb.toString();
    }
}
