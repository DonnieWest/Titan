
package com.donniewest.titan

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.FROYO
import android.app.Instrumentation
import android.content.Context

import com.github.kevinsawicki.http.HttpRequest
import com.orm.SugarApp

class TitanApplication(context: Context, instrumentation: Instrumentation) extends SugarApp {

    def this() = {
      this
      if (SDK_INT <= FROYO) HttpRequest.keepAlive(false)

    }

    def this(context: Context) = {

      this
      attachBaseContext(context)
    }
    def this(context: Context, instrumentation: Instrumentation) = {
        this
        attachBaseContext(instrumentation.getTargetContext())
    }
}