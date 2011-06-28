package nl.jqno.ovherinnering

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.view.View
import _root_.android.widget.Button
import _root_.android.widget.EditText
import _root_.android.widget.Toast
import FindView._

class MainActivity extends Activity with FindView {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    findView[Button](R.id.main_ok).onClick { _ =>
      val city = findView[EditText](R.id.main_city)
      Toast.makeText(this, city.getText.toString, Toast.LENGTH_LONG).show
    }
  }
}
