package com.example.calendarlibrary;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liaoruochen on 2017/4/5.
 * Description:
 */

public interface CaledarAdapter {
    View getView(View convertView, ViewGroup parentView,CalendarBean bean);
}
