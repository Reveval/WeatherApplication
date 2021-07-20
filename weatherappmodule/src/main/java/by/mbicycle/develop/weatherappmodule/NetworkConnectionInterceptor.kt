package by.mbicycle.develop.weatherappmodule

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun intercept(chain: Interceptor.Chain) : Response {
        if (!netIsConnected()) {
            showMessageToUser(context.getString(R.string.message_no_connection))
        }

        val builder = chain.request().newBuilder()
        val response = chain.proceed(builder.build())

        context.apply {
            when(response.code()) {
                AUTHORIZATION_FAILED,
                DO_NOT_HAVE_PERMISSION_ACCU_API,
                NUMBER_OF_REQUEST_EXCEEDED_OPEN_API -> {
                    showMessageToUser(getString(R.string.message_number_of_requests_exceeded))
                }

                NOT_FOUND -> showMessageToUser(getString(R.string.message_not_found))
                INTERNAL_SERVER_ERROR, SERVICE_UNAVAILABLE -> {
                    showMessageToUser(getString(R.string.message_servers_errors))
                }
            }
        }

        return response
    }

    private fun netIsConnected() : Boolean {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                return cm.getNetworkCapabilities(cm.activeNetwork)?.
                    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                cm.allNetworks.forEach { network ->
                    val nInfo = cm.getNetworkInfo(network)
                    return nInfo != null && nInfo.isConnected
                }
            }

            else -> {
                cm.allNetworkInfo.forEach { netInfo ->
                    return netInfo != null && netInfo.isConnected
                }
            }
        }
        return false
    }

    private fun showMessageToUser(message: String) {
        ContextCompat.getMainExecutor(context).execute {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private companion object {
        const val AUTHORIZATION_FAILED = 401
        const val DO_NOT_HAVE_PERMISSION_ACCU_API = 403
        const val NOT_FOUND = 404
        const val NUMBER_OF_REQUEST_EXCEEDED_OPEN_API = 429
        const val INTERNAL_SERVER_ERROR = 500
        const val SERVICE_UNAVAILABLE = 503
    }
}