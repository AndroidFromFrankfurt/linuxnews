package org.androidfromfrankfurt.archnews.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

public class IntentUtil {

    public static @Nullable Intent getWebIntent(@Nullable String url) {
        Intent result = null;
        if (url != null) {
            result = new Intent(Intent.ACTION_VIEW);
            result.setData(Uri.parse(url));
        }
        return result;
    }
}
