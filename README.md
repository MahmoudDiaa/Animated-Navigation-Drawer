# Animated-Navigation-Drawer

![application](https://github.com/MahmoudDiaa/Animated-Navigation-Drawer/blob/master/application.gif)

[![](https://jitpack.io/v/MahmoudDiaa/Animated-Navigation-Drawer.svg)](https://jitpack.io/#MahmoudDiaa/Animated-Navigation-Drawer)


# To add my library to your android project

## Add it in your root build.gradle at the end of repositories:

allprojects
{
		
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
		
	}


# Add the dependency

	dependencies {
	       implementation 'com.github.MahmoudDiaa:Animated-Navigation-Drawer:1.2.0'
	}
	
	
	
### Usage

Drop the Custom Navigation Drawer in your XML layout as is shown below:
```xml
    <com.diaa.customAnimatedNavigationDrawer.widget.AnimatedNavigationDrawer
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/navigationDrawer">
        <FrameLayout
            android:id="@+id/frameLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </com.diaa.customAnimatedNavigationDrawer.widget.AnimatedNavigationDrawer>
```
And then in your Activity or fragment
```kotlin
        class test : AppCompatActivity() {
    var animatedNavigationDrawer: AnimatedNavigationDrawer? = null
    var fragmentClass: Class<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animatedNavigationDrawer = findViewById(R.id.navigationDrawer)

        //Creating a list of menu Items
        val menuItems: MutableList<DrawerItems> = ArrayList()

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and then the second parameter is the image which will be the background of the menu item.
        menuItems.add(DrawerItems("Home", R.drawable.news_bg))
        menuItems.add(DrawerItems("Setting", R.drawable.feed_bg))
        menuItems.add(DrawerItems("Messages", R.drawable.message_bg))

        //then add them to navigation drawer
        animatedNavigationDrawer!!.setItemsList(menuItems)
        fragmentClass = NewsFragment::class.java
        try {
            fragment = fragmentClass!!.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.frameLayout, fragment).commit()
        }
        animatedNavigationDrawer!!.setOnMenuItemClickListener(object : OnItemsClickListener {
            override fun onItemsClicked(position: Int) {
                when (position) {
                    0 -> {
                        fragmentClass = Home::class.java
                    }
                    1 -> {
                        fragmentClass = Setting::class.java
                    }
                    2 -> {
                        fragmentClass = Messages::class.java
                    }
                }


                //Listener for drawer events such as opening and closing.
                animatedNavigationDrawer!!.setDrawerListener(object : AnimatedNavigationDrawer.DrawerListener {
                    override fun onDrawerOpened() {}
                    override fun onDrawerOpening() {}
                    override fun onDrawerClosing() {
                        println("Drawer closed")
                        try {
                            fragment =
                                fragmentClass!!.newInstance() as Fragment
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        if (fragment != null) {
                            val fragmentManager =
                                supportFragmentManager
                            fragmentManager.beginTransaction().setCustomAnimations(
                                android.R.animator.fade_in,
                                android.R.animator.fade_out
                            ).replace(R.id.frameLayout, fragment).commit()
                        }
                    }

                    override fun onDrawerClosed() {}
                    override fun onDrawerStateChanged(newState: Int) {
                        Log.i("", "State $newState")
                    }
                })
            }
        })
    }

    companion object {
        var fragment: Fragment? = null
    }
}
```
```  Java

public class test extends AppCompatActivity {
    AnimatedNavigationDrawer animatedNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animatedNavigationDrawer = findViewById(R.id.navigationDrawer);

        //Creating a list of menu Items

        List<DrawerItems> menuItems = new ArrayList<>();

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and then the second parameter is the image which will be the background of the menu item.

        menuItems.add(new DrawerItems("Home",R.drawable.news_bg));
        menuItems.add(new DrawerItems("Setting",R.drawable.feed_bg));
        menuItems.add(new DrawerItems("Messages",R.drawable.message_bg));

        //then add them to navigation drawer

        animatedNavigationDrawer.setItemsList(menuItems);
        fragmentClass =  NewsFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }

        animatedNavigationDrawer.setOnMenuItemClickListener(position -> {
            switch (position){
                case 0:{
                    fragmentClass = Home.class;
                    break;
                }
                case 1:{
                    fragmentClass = Setting.class;
                    break;
                }
                case 2:{
                    fragmentClass = Messages.class;
                    break;
                }

            }



            //Listener for drawer events such as opening and closing.
            animatedNavigationDrawer.setDrawerListener(new AnimatedNavigationDrawer.DrawerListener() {

                @Override
                public void onDrawerOpened() {

                }

                @Override
                public void onDrawerOpening(){

                }

                @Override
                public void onDrawerClosing(){
                    System.out.println("Drawer closed");

                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                    }
                }

                @Override
                public void onDrawerClosed() {

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    Log.i("","State "+newState);
                }
            });
        });

    }

}
```

### Customization


```xml
    <com.diaa.customAnimatedNavigationDrawer.widget.AnimatedNavigationDrawer
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:navigationDrawerBackgroundColor="#151515"
        app:appbarTitleTextColor="@android:color/white"
        app:HamMenuIconTintColor="@android:color/white"
        app:appbarColor="@android:color/black"
        app:primaryMenuItemTextColor="@android:color/white"
        app:secondaryMenuItemTextColor="@android:color/white"
        app:appbarTitleTextSize="7sp"
        app:primaryMenuItemTextSize="7sp"
        app:secondaryMenuItemTextSize="7sp"
        android:id="@+id/navigationDrawer">
        <FrameLayout
            android:id="@+id/frameLayout"
            android:background="@android:color/black"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </com.diaa.customAnimatedNavigationDrawer.widget.AnimatedNavigationDrawer>
	
	
```
# License


Copyright 2016 Andrea Maglie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
