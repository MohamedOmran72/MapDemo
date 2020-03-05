package education.mostafa.projects.task.preseneter

import education.mostafa.projects.task.views.NetworkView

interface NetworkPersenter {
    fun getMessage()
    fun setView(networkView: NetworkView)

}