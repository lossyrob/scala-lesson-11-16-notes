package demo

import org.scalatest._

class MainSpec extends FunSpec with Matchers {
  describe("Main") {
    it("should have correct hello sentence") {
      Main.helloSentence(true) should be ("Hello GeoTrellis")
    }
  }
}
