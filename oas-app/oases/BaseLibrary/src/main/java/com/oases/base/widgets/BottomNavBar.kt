package com.oases.base.widgets

import android.content.Context
import android.util.AttributeSet
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem
import com.ashokvarma.bottomnavigation.TextBadgeItem
import com.oases.base.R

/*
    底部导航
 */
class BottomNavBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationBar(context, attrs, defStyleAttr) {

    init {
        //首页
        val homeItem = BottomNavigationItem(R.drawable.home_fill,resources.getString(R.string.nav_bar_home))
                .setInactiveIconResource(R.drawable.home)
                .setActiveColorResource(R.color.common_green)
                .setInActiveColorResource(R.color.text_normal)
        //钱包
        val walletItem = BottomNavigationItem(R.drawable.wallet_fill,resources.getString(R.string.nav_bar_category))
                .setInactiveIconResource(R.drawable.wallet)
                .setActiveColorResource(R.color.common_green)
                .setInActiveColorResource(R.color.text_normal)
        //我的
        val userItem = BottomNavigationItem(R.drawable.smile_fill,resources.getString(R.string.nav_bar_user))
                .setInactiveIconResource(R.drawable.smile)
                .setActiveColorResource(R.color.common_green)
                .setInActiveColorResource(R.color.text_normal)

        //设置底部导航模式及样式
        setMode(BottomNavigationBar.MODE_FIXED)
        setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        setBarBackgroundColor(R.color.common_white)
        //添加Tab
        addItem(homeItem)
                .addItem(walletItem)
                .addItem(userItem)
                .setFirstSelectedPosition(0)
                .initialise()
    }
}
