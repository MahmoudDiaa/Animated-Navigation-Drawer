package com.diaa.customAnimatedNavigationDrawer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IntDef
import androidx.appcompat.widget.ThemeUtils
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.diaa.customAnimatedNavigationDrawer.R
import com.diaa.customAnimatedNavigationDrawer.pojo.DrawerItems
import com.diaa.customAnimatedNavigationDrawer.utils.OnSwipeTouchListener
import com.google.android.material.internal.ThemeEnforcement
import com.google.android.material.navigation.NavigationView
import java.util.*

class AnimatedNavigationDrawer : NavigationView {
    //Context
    private var mContext: Context? = null
    private var mLayoutInflater: LayoutInflater? = null

    //Layouts
    private var menuItemList: MutableList<DrawerItems>? = null
    private var rootLayout: RelativeLayout? = null
    var appbarRL: RelativeLayout? = null
    private var containerCV: CardView? = null
    private var appbarTitleTV: TextView? = null
    private var menuIV: ImageView? = null
    private var menuSV: ScrollView? = null
    var drawerContainer: LinearLayout? = null

    private var containerLL: LinearLayout? = null

    private var appbarColor: Int = Color.WHITE

    private var appbarTitleTextColor: Int = Color.BLACK
    private var menuItemSemiTransparentColor = Color.argb(60, 0, 0, 0)

    //        R.color.transparent_black_percent_60
    private var navigationDrawerBackgroundColor: Int = Color.WHITE
    private var primaryItemsTextColor: Int = Color.WHITE
    private var secondaryItemsTextColor: Int = Color.BLACK
    private var menuIconTintColor: Int = Color.BLACK

    //Todo Change Icon Size
    private var menuIconSize = 30f
    private var appbarTitleTextSize = 20f
        set(appbarTitleTextSize) {
            field = appbarTitleTextSize
            appbarTitleTV!!.textSize = appbarTitleTextSize
        }
    private var primaryItemsTextSize = 20f
    private var secondaryItemsTextSize = 20f

    //To check if drawer is open or not
    //Other stuff
    private var isDrawerOpen = false
    private var currentPos = 0
    var centerX = 0f
    var centerY = 0f


    //Listeners
    private var onHamMenuClickListener: OnHamMenuClickListener? = null
    private var onItemsClickListener: OnItemsClickListener? = null
    private var drawerListener: DrawerListener? = null

    constructor(context: Context?) : super(context!!)

    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        Log.e("Animated Class", "attrs ${ThemeEnforcement.checkAppCompatTheme(context)}")
        init(context)
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AnimatedNavigationDrawer,
            0, 0
        )
        setAttributes(a)
        a.recycle()
    }

    //Adding the child views inside CardView LinearLayout
    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (containerLL == null) {
            super.addView(child, index, params)
        } else {
            //Forward these calls to the content view
            containerLL!!.addView(child, index, params)
        }
    }

    //Initialization
    private fun init(context: Context?) {
        mContext = context
        mLayoutInflater = LayoutInflater.from(context)
        //Load RootView from xml
        val rootView: View =
            mLayoutInflater!!.inflate(R.layout.widget_navigation_drawer, this, true)
        rootLayout = rootView.findViewById(R.id.rootLayout)
        appbarRL = rootView.findViewById(R.id.appBarRL)
        containerCV = rootView.findViewById(R.id.containerCV)
        appbarTitleTV = rootView.findViewById(R.id.appBarTitleTV)
        menuIV = rootView.findViewById(R.id.menuIV)
        menuSV = rootView.findViewById(R.id.menu_sv)
        drawerContainer = rootView.findViewById(R.id.menuLL)
        containerLL = rootView.findViewById(R.id.containerLL)
        if (checkAr())
            menuIV!!.setImageResource(R.drawable.asl_pathmorph_drawer_arabic_edition)
        menuItemList = ArrayList()
        menuIV!!.setOnClickListener {
            hamMenuClicked()
            if (isDrawerOpen) {
                closeDrawer()
            } else {
                openDrawer()
            }
        }
        onSwipe()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onSwipe() {
        containerLL!!.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                if (!isDrawerOpen && !checkAr()) {
                    openDrawer()
                } else if (isDrawerOpen && checkAr()) {
                    closeDrawer()
                }


            }

            override fun onSwipeLeft() {
                if (isDrawerOpen && !checkAr())
                    closeDrawer()
                else if (!isDrawerOpen && checkAr())
                    openDrawer()

            }

            override fun onSwipeUp() {
                super.onSwipeUp()
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
            }
        }
        )


    }

    private fun initMenu() {
        for (i in menuItemList!!.indices) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.menu_row_item, null)
            val titleTV = view.findViewById<TextView>(R.id.titleTV)
            val titleTV1 = view.findViewById<TextView>(R.id.titleTV1)
            val backgroundIV = view.findViewById<ImageView>(R.id.backgroundIV)
            val backgroundCV: CardView = view.findViewById(R.id.backgroundCV)
            val tintView = view.findViewById(R.id.tintView) as View
            tintView.setBackgroundColor(

                menuItemSemiTransparentColor

            )
            titleTV.setTextColor(
                secondaryItemsTextColor
            )
            titleTV1.setTextColor(
                primaryItemsTextColor
            )
            titleTV.textSize = secondaryItemsTextSize
            titleTV1.textSize = primaryItemsTextSize
            val rootRL = view.findViewById<RelativeLayout>(R.id.rootRL)
            backgroundCV.tag = "cv$i"
            println("Testing " + backgroundCV.tag)
            titleTV.tag = "tv$i"
            if (i >= 1) {
                backgroundCV.visibility = GONE
                backgroundCV.animate().translationX(rootRL.x - backgroundCV.width)
                    .setDuration(1).start()
                titleTV.visibility = VISIBLE
            }
            rootRL.tag = i
            rootRL.setOnClickListener { v ->
                if (currentPos != Integer.valueOf(v.tag.toString())) {
                    val backCV1: CardView =
                        drawerContainer!!.findViewWithTag<View>("cv$currentPos") as CardView
                    val title1 =
                        drawerContainer!!.findViewWithTag<View>("tv$currentPos") as TextView
                    backCV1.animate().translationX(rootRL.x - backCV1.width).setDuration(300)
                        .start()
                    currentPos = Integer.valueOf(v.tag.toString())
                    menuItemClicked(currentPos)
                    appbarTitleTV!!.text = menuItemList!![currentPos].title
                    val backCV: CardView =
                        drawerContainer!!.findViewWithTag<View>("cv$currentPos") as CardView
                    val title =
                        drawerContainer!!.findViewWithTag<View>("tv$currentPos") as TextView
                    backCV.visibility = INVISIBLE
                    println("Drawer Testing " + backCV.tag)
                    backCV.animate().translationX(rootRL.x - backCV.width).setDuration(1)
                        .start()
                    backCV.animate().translationX(rootRL.x).setDuration(300).start()
                    backCV.visibility = VISIBLE
                    title.visibility = GONE

                    Handler(Looper.getMainLooper()).postDelayed({
                        backCV1.visibility = GONE
                        title1.visibility = VISIBLE
                    }, 300)
                    //Close Navigation Drawer
                    closeDrawer()
                } else {
                    menuItemClicked(currentPos)
                    closeDrawer()
                }
            }
            backgroundIV.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    menuItemList!![i].imageId,
                    null
                )
            )
            titleTV.text = menuItemList!![i].title
            titleTV1.text = menuItemList!![i].title
            drawerContainer!!.addView(view)
        }
    }

    //Hamburger button Click Listener
    interface OnHamMenuClickListener {
        fun onHamMenuClicked()
    }

    //Listener for menu item click
    interface OnItemsClickListener {
        fun onItemsClicked(position: Int)
    }

    //Listener for monitoring events about drawer.
    interface DrawerListener {
        //Called when a drawer is opening.
        fun onDrawerOpening()

        //Called when a drawer is closing.
        fun onDrawerClosing()

        //Called when a drawer has settled in a completely open state.
        fun onDrawerOpened()

        //Called when a drawer has settled in a completely closed state.
        fun onDrawerClosed()

        //Called when the drawer motion state changes. The new state will
        fun onDrawerStateChanged(@State newState: Int)
    }


    fun getOnHamMenuClickListener(): OnHamMenuClickListener? {
        return onHamMenuClickListener
    }

    fun setOnHamMenuClickListener(onHamMenuClickListener: OnHamMenuClickListener?) {
        this.onHamMenuClickListener = onHamMenuClickListener
    }

    fun getOnMenuItemClickListener(): OnItemsClickListener? {
        return onItemsClickListener
    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: OnItemsClickListener) {
        onItemsClickListener = onMenuItemClickListener
    }

    fun getDrawerListener(): DrawerListener? {
        return drawerListener
    }

    fun setDrawerListener(drawerListener: DrawerListener?) {
        this.drawerListener = drawerListener
    }


    private fun hamMenuClicked() {
        if (onHamMenuClickListener != null) {
            onHamMenuClickListener!!.onHamMenuClicked()
        }
    }

    private fun menuItemClicked(position: Int) {
        if (onItemsClickListener != null) {
            onItemsClickListener!!.onItemsClicked(position)
        }
    }

    private fun drawerOpened() {
        if (drawerListener != null) {
            drawerListener!!.onDrawerOpened()
            drawerListener!!.onDrawerStateChanged(STATE_OPEN)
        }
    }

    private fun drawerClosed() {
        println("Drawer Closing")
        if (drawerListener != null) {
            drawerListener!!.onDrawerClosed()
            drawerListener!!.onDrawerStateChanged(STATE_CLOSED)
        }
    }

    private fun drawerOpening() {
        if (drawerListener != null) {
            drawerListener!!.onDrawerOpening()
            drawerListener!!.onDrawerStateChanged(STATE_OPENING)
        }
    }

    private fun drawerClosing() {
        if (drawerListener != null) {
            drawerListener!!.onDrawerClosing()
            drawerListener!!.onDrawerStateChanged(STATE_CLOSING)
        }
    }

    //Closes drawer
    fun closeDrawer() {
        drawerClosing()
        isDrawerOpen = false
        val stateSet = intArrayOf(android.R.attr.state_checked * if (isDrawerOpen) 1 else -1)
        menuIV!!.setImageState(stateSet, true)
        appbarTitleTV!!.animate().translationX(centerX).start()
        containerCV!!.animate().translationX(rootLayout!!.x).translationY(rootLayout!!.y)
            .setDuration(500).start()

        Handler(Looper.getMainLooper()).postDelayed({
            drawerClosed()
            containerCV!!.cardElevation = 0f
            containerCV!!.radius = 0f
        }, 500)
    }

    //Opens Drawer
    fun openDrawer() {
        drawerOpening()
        isDrawerOpen = true
        val stateSet = intArrayOf(android.R.attr.state_checked * if (isDrawerOpen) 1 else -1)
        menuIV!!.setImageState(stateSet, true)
        containerCV!!.cardElevation = 100.0.toFloat()
        containerCV!!.radius = 60.0.toFloat()
        if (checkAr()) {
            appbarTitleTV!!.animate()
                .translationX(centerX + menuIV!!.width + menuIV!!.width / 4 - appbarTitleTV!!.width / 2 + appbarRL!!.width / 4)
                .start()
            containerCV!!.animate()
                .translationX(rootLayout!!.x - rootLayout!!.width / 8 - rootLayout!!.width / 2)
                .translationY(250f).setDuration(500).start()


        } else {
            appbarTitleTV!!.animate()
                .translationX(centerX + menuIV!!.width + menuIV!!.width / 4 + appbarTitleTV!!.width / 2 - appbarRL!!.width / 2)
                .start()
            containerCV!!.animate()
                .translationX(rootLayout!!.x + rootLayout!!.width / 8 + rootLayout!!.width / 2)
                .translationY(250f).setDuration(500).start()
        }
        Handler(Looper.getMainLooper()).postDelayed({ drawerOpened() }, 250)
    }

    private fun checkAr(): Boolean {
        Log.e("Animated Navigation", "checkAr ${Locale.getDefault().displayLanguage}")
        return Locale.getDefault().displayLanguage == "ar" || Locale.getDefault().displayLanguage == "العربية"
    }

    //set Attributes from xml
    private fun setAttributes(attrs: TypedArray) {
        setAppbarColor(
            attrs.getColor(
                R.styleable.AnimatedNavigationDrawer_appbarColor,
                appbarColor
            )
        )
        setAppbarTitleTextColor(
            attrs.getColor(
                R.styleable.AnimatedNavigationDrawer_appbarTitleTextColor,
                appbarTitleTextColor
            )
        )
        menuiconTintColor = attrs.getColor(
            R.styleable.AnimatedNavigationDrawer_HamMenuIconTintColor,
            menuIconTintColor

        )
        setItemsSemiTransparentColor(
            attrs.getColor(
                R.styleable.AnimatedNavigationDrawer_HamMenuItemSemiTransparentColor,
                menuItemSemiTransparentColor
            )
        )
        setNavigationDrawerBackgroundColor(
            attrs.getColor(
                R.styleable.AnimatedNavigationDrawer_navigationDrawerBackgroundColor,

                navigationDrawerBackgroundColor
            )
        )
        setPrimaryItemsTextColor(
            attrs.getColor(
                R.styleable.AnimatedNavigationDrawer_navigationDrawerBackgroundColor,
                primaryItemsTextColor
            )
        )
        setSecondaryItemsTextColor(
            attrs.getColor(
                R.styleable.AnimatedNavigationDrawer_secondaryMenuItemTextColor,
                secondaryItemsTextColor
            )
        )
        appbarTitleTextSize =
            attrs.getDimension(
                R.styleable.AnimatedNavigationDrawer_appbarTitleTextSize,
                20f
            )
        setPrimaryItemsTextSize(
            attrs.getDimension(
                R.styleable.AnimatedNavigationDrawer_primaryMenuItemTextSize,
                20f
            )
        )
        setSecondaryItemsTextSize(
            attrs.getDimension(
                R.styleable.AnimatedNavigationDrawer_secondaryMenuItemTextSize,
                20f
            )
        )
        menuIconSize = attrs.getDimension(
            R.styleable.AnimatedNavigationDrawer_HamMenuIconSize,
            20f
        )
    }

    //To change the AppBar Title

    fun setAppbarTitleTV(name: String?) {
        appbarTitleTV!!.text = name
    }

    //Adding menu to drawer
    fun addItems(menuItem: DrawerItems) {
        if (menuItemList != null) {
            menuItemList!!.add(menuItem)
        }
    }

    //Getting the list of Menu Items
    fun getItemsList(): List<DrawerItems>? {
        return menuItemList
    }

    //Setting the list of Menu Items
    fun setItemsList(menuItemList: MutableList<DrawerItems>?) {
        this.menuItemList = menuItemList
        initMenu()
    }

    /*
     *
     * Customization :)
     *
     */
    fun getAppbarColor(): Int {
        return appbarColor
    }

    fun setAppbarColor(appbarColor: Int) {
        this.appbarColor = appbarColor
        appbarRL!!.setBackgroundColor(appbarColor)
    }

    fun getAppbarTitleTextColor(): Int {
        return appbarTitleTextColor
    }

    fun setAppbarTitleTextColor(appbarTitleTextColor: Int) {
        this.appbarTitleTextColor = appbarTitleTextColor
        appbarTitleTV!!.setTextColor(appbarTitleTextColor)
    }

    var menuiconTintColor: Int
        get() = menuIconTintColor
        set(menuIconTintColor) {
            this.menuIconTintColor = menuIconTintColor
            menuIV!!.setColorFilter(menuIconTintColor)
        }


    fun getMenuIconSize(): Float {
        return menuIconSize
    }

    fun setMenuIconSize(menuIconSize: Float) {
        //Todo Change Icon Size
        this.menuIconSize = menuIconSize
    }

    fun getItemsSemiTransparentColor(): Int {
        return menuItemSemiTransparentColor
    }

    fun setItemsSemiTransparentColor(menuItemSemiTransparentColor: Int) {
        this.menuItemSemiTransparentColor = menuItemSemiTransparentColor
        invalidate()
    }

    fun getNavigationDrawerBackgroundColor(): Int {
        return navigationDrawerBackgroundColor
    }

    fun setNavigationDrawerBackgroundColor(navigationDrawerBackgroundColor: Int) {
        rootLayout!!.setBackgroundColor(navigationDrawerBackgroundColor)
        this.navigationDrawerBackgroundColor = navigationDrawerBackgroundColor
    }

    fun getPrimaryItemsTextColor(): Int {
        return primaryItemsTextColor
    }

    fun setPrimaryItemsTextColor(primaryItemsTextColor: Int) {
        this.primaryItemsTextColor = primaryItemsTextColor
        invalidate()
    }

    fun getSecondaryItemsTextColor(): Int {
        return secondaryItemsTextColor
    }

    fun setSecondaryItemsTextColor(secondaryItemsTextColor: Int) {
        this.secondaryItemsTextColor = secondaryItemsTextColor
        invalidate()
    }

    fun getPrimaryItemsTextSize(): Float {
        return primaryItemsTextSize
    }

    fun setPrimaryItemsTextSize(primaryItemsTextSize: Float) {
        this.primaryItemsTextSize = primaryItemsTextSize
        invalidate()
    }

    fun getSecondaryItemsTextSize(): Float {
        return secondaryItemsTextSize
    }

    fun setSecondaryItemsTextSize(secondaryItemsTextSize: Float) {
        this.secondaryItemsTextSize = secondaryItemsTextSize
        invalidate()
    }

    //to change the typeface of appbar title
    fun setAppbarTitleTypeface(titleTypeface: Typeface?) {
        appbarTitleTV!!.typeface = titleTypeface
    }

    companion object {
        //Indicates that any drawer is open. No animation is in progress.
        @IntDef(STATE_OPEN, STATE_CLOSED, STATE_OPENING, STATE_CLOSING)
        @kotlin.annotation.Retention
        annotation class State

        const val STATE_OPEN = 0

        //Indicates that any drawer is closed. No animation is in progress.
        const val STATE_CLOSED = 1

        //Indicates that a drawer is in the process of opening.
        const val STATE_OPENING = 2

        //Indicates that a drawer is in the process of closing.
        const val STATE_CLOSING = 3
    }
}
