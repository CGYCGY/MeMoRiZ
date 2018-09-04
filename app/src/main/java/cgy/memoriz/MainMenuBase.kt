package cgy.memoriz


import android.animation.ValueAnimator
import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import cgy.memoriz.fragment.StudentMainMenu
import cgy.memoriz.fragment.second.SecondPage
import cgy.memoriz.others.CustomColor
import cgy.memoriz.others.DialogFactory
import kotlinx.android.synthetic.main.main_app_bar.*
import kotlinx.android.synthetic.main.main_navigation.*

//extend this to create the drawer
open class MainMenuBase : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val LOG_TAG = "BaseMainActivity"
    private var toggle: ActionBarDrawerToggle? = null
    private var dialogFactory = DialogFactory()
    private lateinit var progressDialog: CustomColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_navigation)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*Set your ProgressBar color here.*/
        progressDialog = CustomColor(this, resources.getColor(R.color.grey_background_dark))
        onCreateDrawerToggle()

    }

    private fun onCreateDrawerToggle() {
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle!!)
        toggle!!.syncState()

        /* Some logic to handle toolbar icon .
        * If the toolbar  icon is arrow , den back to previous fragment
        * If the toolbar is hamburger , den open navigation drawer.*/
        toolbar.setNavigationOnClickListener {
            if (SharedPref.arrow) {
                super.onBackPressed()
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        nav_view.setNavigationItemSelectedListener(this)
    }

    /*  Logic to handle your phone back button
     *  If the drawer open , then when u press backbutton on your phone screnn , the drawer will close
     *  Else , if your toolbar icon is arrow , den it back to previous fragment
     *  Else , pop out dialog ask u want to quit or not.*/
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (!SharedPref.arrow) {
            dialogFactory.createTwoButtonDialog(this, "ALERT!", "Do you want to proceed to Log Out??",
                    DialogInterface.OnClickListener { dialog, which -> finish() }).show()
        } else {
            super.onBackPressed()
        }
    }

    /* For handle navigation drawer item .
    *  if want add more menu , add at activity_main_drawer.
    *  can also add icon , for now i no add because lazy.
    *  this navigationItem click can copy paste n put in your main Activity.*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Setting -> {
                replaceContainerFragment(SecondPage())
            }
            R.id.Exit -> {
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceContainerFragment(fragment: Fragment) {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commit()
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "fragment=[$fragment]", e)
            }
        }
    }

    fun switchToDashboard(){
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replaceContainerFragment(StudentMainMenu())
    }

    fun cleanAndSwitchFragment(fragment: Fragment){
        switchToDashboard()
        replaceContainerFragment(fragment)
    }

    fun updateToolbarIconState(isArrow: Boolean) {
        if (isArrow) {
            changeToolbarIconToBackArrow()
        } else {
            changeToolbarIconToMenu()
        }
    }

    fun changeToolbarIconToBackArrow() {
        SharedPref.arrow = true
        animateIcon(0, 1, 600)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun changeToolbarIconToMenu() {
        SharedPref.arrow = false
        animateIcon(1, 0, 600)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun animateIcon(start: Int, end: Int, duration: Int) {
        if (toggle != null) {
            val anim = ValueAnimator.ofFloat(start.toFloat(), end.toFloat())
            anim.addUpdateListener { animation ->
                val slideOffset = animation?.animatedValue as Float
                toggle!!.onDrawerSlide(drawer_layout, slideOffset)
            };
            anim.interpolator = DecelerateInterpolator()
            anim.duration = duration.toLong()
            anim.start()
        }
    }

    fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.setCancelable(false)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Loading...")
            progressDialog.show();
        }
    }

    fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss()
        }
    }

    fun switchFragmentWithNonBackStack(fragment:Fragment){
        try {
            val fragmentManager = getSupportFragmentManager()
            fragmentManager.popBackStackImmediate()
            replaceContainerFragment(fragment)
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "fragment=[$fragment]", e)
            }
        }
    }

}
