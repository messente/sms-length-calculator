# SMS Length Calculator
Free SMS length calculator for different programming languages.

Checks if a message can be sent as GSM 7bit charset or if Unicode charset must be used.

## PHP
Optimized for reasonable speed. Initial version should allow processing ~20 000 messages in a second on a modern server.

```PHP
$SmsCalc = new SmsLengthCalculator();
$text = "The morpheme can be reduplicated to emphasize the meaning of the word";
$sms_parts = $SmsCalc->getPartCount($text);
echo "Message with text [".$text."] is sent as [".$sms_parts." SMS]\n";
// Output: Message with text [The morpheme can be reduplicated to emphasize the meaning of the word] is sent as [1 SMS]
```
