package com.ekc.customanimatednavigationdrawer.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.ekc.customanimatednavigationdrawer.widget.AnimatedNavigationDrawer

class CustomViewFactory private constructor() : LayoutInflater.Factory {
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        //Check if it's one of our custom classes, if so, return one using
        //the Context/AttributeSet constructor
        return if (AnimatedNavigationDrawer::class.java.simpleName == name) {
            AnimatedNavigationDrawer(context, attrs)
        } else null

        //Not one of ours; let the system handle it
    }

    companion object {
        private var mInstance: CustomViewFactory? = null
        val instance: CustomViewFactory?
            get() {
                if (mInstance == null) {
                    mInstance = CustomViewFactory()
                }
                return mInstance
            }
    }
}
