package education.mostafa.projects.task.views

import android.os.Bundle

interface MapsView {
    fun initView()
    fun initObjects()
    fun initClient()
    fun setUpMap(bundle: Bundle?)
    fun initMap()
}