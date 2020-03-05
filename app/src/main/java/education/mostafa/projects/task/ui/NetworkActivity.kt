package education.mostafa.projects.task.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import education.mostafa.projects.task.PresneterImplement.NetworkImplementer
import education.mostafa.projects.task.R
import education.mostafa.projects.task.preseneter.NetworkPersenter
import education.mostafa.projects.task.views.NetworkView

class NetworkActivity : AppCompatActivity(), NetworkView {

    lateinit var message_txt: TextView
    lateinit var loading: LinearLayout

    lateinit var networkPresenter: NetworkPersenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        initViews()
        initObjects()
        showLoading()
        getMessage()
    }

    override fun setMessage(msg: String) {
        hideLoading()
        message_txt.setText(msg)
        Toast.makeText(this , msg + ("\uD83D\uDC4B")  , Toast.LENGTH_SHORT).show()
    }

    override fun setError(error: String) {
        hideLoading()
        message_txt.setText(error)
        Toast.makeText(this , error + ("\uD83D\uDEAB")  , Toast.LENGTH_SHORT).show()
    }

    override fun initObjects() {
        networkPresenter = NetworkImplementer(this)
        networkPresenter.setView(this)
    }

    override fun getMessage() {
        networkPresenter.getMessage()
    }

    override fun initViews() {
        message_txt = findViewById(R.id.message_txt)
        loading = findViewById(R.id.loading_lyt)
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }
}
