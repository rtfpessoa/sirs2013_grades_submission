package util

import java.security.MessageDigest
import java.math.BigInteger

object MD5 {

  def hash(s: String): String = {
    val m = MessageDigest.getInstance("MD5")
    val b = s.getBytes("UTF-8")

    m.update(b, 0, b.length)
    new BigInteger(1, m.digest()).toString(16)
  }

}
