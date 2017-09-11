type Handled[S,R] = S => R


def defaultHandle[S]: Handled[S,S] = identity

def myFunc[S <:Any,R <: Any](v:S, handled: Handled[S,R] = defaultHandle(v)): R = {


  implicitly[Default[S,R]].apply()


}