




class Test {


  def ret[A,B]( a:A )(f: A => B):Unit = {

    f(a)
    Unit
  }

  def save = {


  // Jak kurwa to sparametryzować?????

  // to pozwala przypisac funkcje do innej zmiennej

  // No ale na funckjach się tak nie da, ale niby się da na metodach, to może tal
  ret _

  }
}

  val t = new Test()

t.ret("Dupa")( r => {
  println("R:" + r)
  "HUJ"
} )


val s = t.save

s( (r:String) => {
  println("R:" + r)
  "HUJ"
} )

