type Handled[S,R] = S => R


def handledDefault[S,R](v:S) = v.asInstanceOf[R]

def myFunc[S,R](v:S, handled: Handled[S,R] = handledDefault[S,R] _ ):R = {

  handled(v)

}

myFunc[Int,String](1)