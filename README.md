# SMS Length Calculator
Free SMS length calculator for different programming languages.

Checks if a message can be sent as GSM 7bit charset or if Unicode charset must be used.

## Scala
Usage example
```Scala
val txt: String = "The morpheme can be reduplicated to emphasize the meaning of the word";
val smsParts: Int  = SmsLengthCalculator.getPartCount(txt);
println("Message with text ["+txt+"] is sent as ["+smsParts+" SMS]");
```

## Java
Usage example
```Java
SmsLengthCalculator calc = new SmsLengthCalculator();
String txt = "The morpheme can be reduplicated to emphasize the meaning of the word";
int sms_parts = calc.getPartCount(txt);
System.out.println("Message with text ["+txt+"] is sent as ["+sms_parts+" SMS]");
```


## PHP
Usage example
```PHP
$SmsCalc = new SmsLengthCalculator();
$text = "The morpheme can be reduplicated to emphasize the meaning of the word";
$sms_parts = $SmsCalc->getPartCount($text);
echo "Message with text [".$text."] is sent as [".$sms_parts." SMS]\n";
// Output: Message with text [The morpheme can be reduplicated to emphasize the meaning of the word] is sent as [1 SMS]
```
