package com.example.administrator.myapplication.test;

import android.graphics.drawable.Drawable;

/**
 * Created by liaoruochen on 2017/3/24.
 * Description:
 */

public interface ClusterRender {
    /**
     * 根据数目返回渲染背景样式
     * @param clusterNum
     * @return
     */
    Drawable getDrawAble(int clusterNum);
}
