package com.donniewest.titan

import android.support.v4.app.Fragment
import android.view.{ViewGroup, LayoutInflater}
import android.os.Bundle


class Post_Feed_Fragment extends Fragment {

  override def onCreateView (inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle) = {

    inflater.inflate(R.layout.post_feed, container, false)

  }

}
