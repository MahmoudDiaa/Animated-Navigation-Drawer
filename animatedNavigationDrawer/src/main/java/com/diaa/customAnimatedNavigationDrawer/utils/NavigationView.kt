package com.diaa.customAnimatedNavigationDrawer.utils

import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.diaa.customAnimatedNavigationDrawer.widget.AnimatedNavigationDrawer
import com.google.android.material.navigation.NavigationView

fun AnimatedNavigationDrawer.setupWithNavController(navController: NavController) {
    NavigationUI.setupWithNavController(this, navController)
}