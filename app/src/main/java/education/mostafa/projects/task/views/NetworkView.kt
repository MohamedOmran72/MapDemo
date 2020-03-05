package education.mostafa.projects.task.views

interface NetworkView {
    fun setMessage(msg:String)
    fun setError(error: String)
    fun initObjects()
    fun getMessage()
    fun initViews()
    fun showLoading()
    fun hideLoading()
}