package np.ict.mad.peyzhixun.ca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import np.ict.mad.peyzhixun.ca.navigation.AppNav
import np.ict.mad.peyzhixun.ca.ui.theme.ContinuousAssessmentPeyZhiXunTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContinuousAssessmentPeyZhiXunTheme {
                val navController = rememberNavController()
                AppNav(navController)
            }
        }
    }
}
