package demo

import geotrellis.raster._

object Bar {
  val isTrue = true
  def isFalse = !true

  def dangerous(x: Double): String =
    try {
      if(x > 1000) {
        throw new Exception("Too large!")
      } else {
        x.toString
      }
    } catch {
      case e: Exception =>
        println("Too large")
        throw e
    }
}

/** TODO:
  * trait
  * abstract class
  * case class
  */

object Main {
  lazy val someHeavyWeightThingThatYouMayNotUse = ???

  lazy val anotherSentence = {
    println("Another sentence!")
    "Another one"
  }

  def workWithClasses(): Unit = {
    val foo = Foo(1)
    val foo2 = Foo(foo)
    println(foo + foo)
    foo()
    println(Bar.dangerous(10))
    println(Bar.dangerous(Int.MaxValue))
  }

  def helloSentence(b: Boolean): String =
    if(b) {
      println("It's true!")
      "Hello GeoTrellis"
    } else {
      println("It's not true!")
      "Bye GeoTrellis"
    }

  def save(): Unit = { println("does stuff") }

  def main(args: Array[String]): Unit = {
    workWithClasses()
    save()
    println(anotherSentence)
    println(anotherSentence)
    println(helloSentence(Bar.isTrue))
    println(helloSentence(Bar.isFalse))
  }
}
