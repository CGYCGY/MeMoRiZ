package cgy.memoriz.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import cgy.memoriz.MainMenuBase

open class MainActivityBaseFragment : Fragment() {

    /*
    * This class main function is allow other fragment use some common .
    * you just need use the following , other no touch nwm. no need chg implementation just used
    * 1 . showProgressDialog(), hideProgressDialog()
    * 2. showToastMessage(your message)
    * 3. switchFragment (your next fragment)
    * 4. setTitle ("your toolbar title")
    * No need care too much how to implement. The function is well done.
    * Just remember extend all your fragment to this class.
    * you can also start new activity also in fragment
    * */

    private val LOG_TAG = "MainBaseFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

     protected fun showProgressDialog(){
        try {
            getBaseActivity()!!.showProgressDialog()
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "showProgressDialog", e)
            }
        }

    }
     protected fun hideProgressDialog(){
        try {
            getBaseActivity()!!.hideProgressDialog()
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "hideProgressDialog", e)
            }
        }
    }

    protected fun showToastMessage(message : String){
        try {
            getBaseActivity()!!.showToastMessage(message)
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "hideProgressDialog", e)
            }
        }
    }

    protected fun switchFragment(fragment: MainActivityBaseFragment) {
        try {
            getBaseActivity()!!.replaceContainerFragment(fragment)
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "fragment=[$fragment]", e)
            }
        }
    }

    protected fun updateToolbarIconState(isArrow: Boolean) {
        try {
            getBaseActivity()!!.updateToolbarIconState(isArrow)
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "isArrow=[$isArrow]", e)
            }
        }
    }

    protected fun setTitle(title: String) {
        try {
            getBaseActivity()!!.title = title
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "title=[$title]", e)
            }
        }
    }

    private fun getBaseActivity(): MainMenuBase? {
        try {
            return super.getActivity() as MainMenuBase
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.WARN)) {
                Log.w(LOG_TAG, "getBaseActivity failed")
            }
        }
        return null
    }
}
