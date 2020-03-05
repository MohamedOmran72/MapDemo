package mostafa.projects.kotlinretrofit.Connection

import android.telecom.Call
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyServices {

    @GET("test.php")
    fun getMessage(): retrofit2.Call<ResponseBody>

}
