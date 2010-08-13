package ro.gagarin.model 

case class CE(id:Long)

object CommonCe { 
  
case object ADMIN_CE extends CE(1)
case object USER_CE extends CE(2)
case object ROLE_CE extends CE(3)
case object GROUP_CE extends CE(4)
case object PERMISSION_CE extends CE(5)


}