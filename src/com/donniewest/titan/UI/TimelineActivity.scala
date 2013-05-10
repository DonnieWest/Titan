package com.donniewest.titan.UI

import android.os.Bundle
import org.scaloid.common.SActivity
import com.actionbarsherlock.app.SherlockActivity
import android.util.Log
import concurrent.ops._
import com.donniewest.titan.R

class TimelineActivity extends SherlockActivity with SActivity {
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      Log.e("Intentional", "Do I at least get here?")
      val data = getIntent.getData.getQueryParameter("code")
      Log.e("Intentional", "The data is " + data)
      spawn {
        Log.e("Intentional", "Entering Spawn!")
        Log.e("Intentional", "Service finished!")
      }
      Log.e("Intentional", "got here...")
      setContentView(R.layout.main)
    }
}