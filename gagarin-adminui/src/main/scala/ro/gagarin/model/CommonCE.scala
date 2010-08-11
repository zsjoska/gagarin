package ro.gagarin.model 

case class CommonCE(id:Long) extends Enumeration{
}

case object ADMIN_CE extends CommonCE(1)
case object USER_CE extends CommonCE(2)
case object GROUP_CE extends CommonCE(4)
case object PERMISSION_CE extends CommonCE(5)

