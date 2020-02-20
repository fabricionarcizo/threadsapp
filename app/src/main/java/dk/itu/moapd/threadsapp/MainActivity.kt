package dk.itu.moapd.threadsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var mThreadFragment: Fragment
    private lateinit var mHandlerFragment: Fragment
    private lateinit var mAsyncTaskFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mThreadFragment = ThreadFragment()
        mHandlerFragment = HandlerFragment()
        mAsyncTaskFragment = AsyncTaskFragment()

        val fragment =
            supportFragmentManager
                .findFragmentById(R.id.fragment)
        if (fragment == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment, mThreadFragment)
                .commit()
    }

    fun selectFragment(view: View) {
        val fragment = when (view.id) {
            R.id.threadButton -> mThreadFragment
            R.id.handlerButton -> mHandlerFragment
            else -> mAsyncTaskFragment
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, fragment)
            .setTransition(FragmentTransaction
                .TRANSIT_FRAGMENT_FADE)
            .commit()
    }

}
