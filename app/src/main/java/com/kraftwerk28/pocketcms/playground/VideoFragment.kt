package com.kraftwerk28.pocketcms.playground

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_video, container, false)

    override fun onStart() {
        super.onStart()
        imageButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_videoFragment_to_startFragment2)
        )
    }
}
