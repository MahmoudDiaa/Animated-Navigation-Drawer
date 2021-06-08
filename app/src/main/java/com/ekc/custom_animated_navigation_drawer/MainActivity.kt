package com.ekc.custom_animated_navigation_drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.diaa.customAnimatedNavigationDrawer.pojo.DrawerItems
import com.diaa.customAnimatedNavigationDrawer.widget.AnimatedNavigationDrawer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navDrawer = findViewById<AnimatedNavigationDrawer>(R.id.nav_drawer)
        val drawerItems: ArrayList<DrawerItems> = arrayListOf()
        drawerItems.add(DrawerItems("الصفخة", R.drawable.news_bg))
        drawerItems.add(DrawerItems("الطلبات", R.drawable.feed_bg))
        drawerItems.add(DrawerItems("الخروج", R.drawable.message_bg))

        navDrawer.setAppbarTitleTV("الصفحة الرئسية")
        navDrawer.setItemsList(drawerItems)
    }
}