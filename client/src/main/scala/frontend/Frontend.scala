package Frontend

import org.scalajs.dom

import scala.scalajs.js

object Frontend extends js.JSApp {
  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = "Edgar"
  }
}
