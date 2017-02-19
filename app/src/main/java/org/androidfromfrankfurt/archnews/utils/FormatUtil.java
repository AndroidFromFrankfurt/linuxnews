package org.androidfromfrankfurt.archnews.utils;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {
    /**
     * Formats RSS Publish Date using same patter as on web-site (eg 2017-12-31)
     * @param publishDate Full publish date
     * @return Formatted value of "-" if unable to format
     */
    public static String formatPublishDate(@Nullable String publishDate) {
        if (publishDate != null) {
            Date pubDate = new Date(publishDate);
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
            return df.format(pubDate);
        }
        return "-";
    }
}
