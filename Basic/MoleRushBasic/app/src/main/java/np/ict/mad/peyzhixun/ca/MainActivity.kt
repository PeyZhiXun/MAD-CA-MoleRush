package np.ict.mad.peyzhixun.ca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import np.ict.mad.peyzhixun.ca.feature.game.GameScreen
import np.ict.mad.peyzhixun.ca.ui.theme.ContinuousAssessmentPeyZhiXunTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContinuousAssessmentPeyZhiXunTheme {
                GameScreen()
            }
        }
    }
}
