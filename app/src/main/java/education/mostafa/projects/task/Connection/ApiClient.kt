package mostafa.projects.kotlinretrofit.Connection

import education.mostafa.projects.task.helper.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient{
    companion object{
        fun getRetrofitClient ():Retrofit{
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }

    }
}