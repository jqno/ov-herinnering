package nl.jqno.ovherinnering

import _root_.android.app.Activity
import _root_.android.view.View
import _root_.android.view.View.OnClickListener

trait FindView extends Activity {
  def findView[WidgetType <: View](id: Int): WidgetType = findViewById(id).asInstanceOf[WidgetType]
}

class ViewWithOnClick(view: View) {
  def onClick(action: View => Any) {
    view.setOnClickListener(new View.OnClickListener() {
      def onClick(v: View) { action(v) }
    })
  }
}

object FindView extends Activity {
  implicit def addOnClickToViews(view: View) = new ViewWithOnClick(view)
}
