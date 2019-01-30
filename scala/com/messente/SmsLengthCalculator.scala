object SmsLengthCalculator {

  val GSM_CHARSET_7BIT = 0
  val GSM_CHARSET_UNICODE = 2
  val GSM_7BIT_ESC = '\u001b'

  val GSM7BIT_EXT: Set[String] = Set("\f", "^", "{", "}", "\\", "[", "~", "]", "|", "€")

  val GSM7BIT: Set[String] = Set(
    "@", "£", "$", "¥", "è", "é", "ù", "ì", "ò", "Ç", "\n", "Ø", "ø", "\r", "Å", "å",
    "Δ", "_", "Φ", "Γ", "Λ", "Ω", "Π", "Ψ", "Σ", "Θ", "Ξ", "\u001b", "Æ", "æ", "ß", "É",
    " ", "!", "'", "#", "¤", "%", "&", "\"", "(", ")", "*", "+", ",", "-", ".", "/",
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?",
    "¡", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
    "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Ä", "Ö", "Ñ", "Ü", "§",
    "¿", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
    "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "ä", "ö", "ñ", "ü", "à"
  )

  private[this] def getCharset(message: String): Int = {

    val groupText = GSM7BIT ++ GSM7BIT_EXT
    val messageList = message.toList.map(_.toString)

    messageList.toSet.diff(groupText).isEmpty match {
      case true => GSM_CHARSET_7BIT
      case false => GSM_CHARSET_UNICODE
    }

  }

  private[this] def getPartCount7bit(message: String): Int = {
    val content7bit = message.toList.map(_.toString)
      .map(x => if (GSM7BIT_EXT.contains(x)) GSM_7BIT_ESC + x else x)
      .map(_.toString)
      .mkString("")

    (content7bit.length <= 160) match {
      case true => 1
      case false =>

        val parts = Math.ceil(content7bit.length / 153.0).toInt
        val freeParts = content7bit.length - Math.floor(content7bit.length / 153.0) * 153

        (freeParts >= parts - 1) match {
          case true => parts
          case false => split(content7bit, 0)
        }
    }
  }

  private[this] def split(message: String, length: Int): Int = {

    val list = message.grouped(153).toList

    list.lastOption match {
      case Some(x) => (x.length == 1 && x == GSM_7BIT_ESC) match {
        case true => list.length - 1
        case false => list.length
      }
      case None => list.length
    }
  }

  def getPartCount(message: String): Int = {
    getCharset(message) match {
      case GSM_CHARSET_7BIT => getPartCount7bit(message)
      case GSM_CHARSET_UNICODE =>
        (message.length <= 70) match {
          case true => 1
          case false => Math.ceil(message.length / 67.0).toInt
        }

      case _ => -1
    }
  }
}
