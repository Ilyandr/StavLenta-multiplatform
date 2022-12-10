package gcu.production.stavlenta.android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.feature.other.completableRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
           withContext(Dispatchers.IO) {
               CommonSDK.restAuthRepository.login("doc77776@gmail.com", "288923").completableRequest({
                   Toast.makeText(this@MainActivity, "Opana, all good: 200", Toast.LENGTH_LONG).show()
               }, { code ->
                   Toast.makeText(this@MainActivity, "Opana, error: $code", Toast.LENGTH_LONG).show()
               })
           }.apply {
          //     Log.e("HTTP", this)
           }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")


    }
}
