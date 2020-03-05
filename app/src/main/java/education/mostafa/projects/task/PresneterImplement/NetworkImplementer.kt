package education.mostafa.projects.task.PresneterImplement

import android.content.Context
import education.mostafa.projects.task.preseneter.NetworkPersenter
import education.mostafa.projects.task.views.NetworkView
import mostafa.projects.kotlinretrofit.Connection.ApiClient
import mostafa.projects.kotlinretrofit.Connection.MyServices
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkImplementer(context: Context) : NetworkPersenter {
    lateinit var netView: NetworkView

    override fun getMessage() {
        val service = ApiClient.getRetrofitClient().create(MyServices::class.java)
        val call = service.getMessage()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response?.isSuccessful!!) {
                    val response = response.body()!!
                    netView.setMessage(response.string())
                }else{
                    netView.setError(response.errorBody().toString())
                }

            }
        })

    }

    override fun setView(networkView: NetworkView) {
        netView = networkView
    }
}