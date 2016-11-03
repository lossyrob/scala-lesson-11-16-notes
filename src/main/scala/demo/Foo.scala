package demo

class Foo(val x: Int) {
  private val y: Double = 0.9 * x
  private def add(foo2: Foo): Foo =
    new Foo(x + foo2.x)

  def +(foo2: Foo): Foo =
    add(foo2)

  override def toString: String =
    s"Foo(${x})"

  def apply() = { println("Foo.apply") }
}

// Companion object
object Foo {
  def add(foo1: Foo, foo2: Foo): Foo =
    foo1.add(foo2)

  def apply(i: Int): Foo =
    new Foo(i)

  def apply(foo: Foo): Foo =
    new Foo((foo.x * foo.y).toInt)
}
