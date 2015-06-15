package com.messente.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SmsLengthCalculator {
    
    private static final int GSM_CHARSET_7BIT = 0;
    private static final int GSM_CHARSET_UNICODE = 2;
    private static final char GSM_7BIT_ESC = '\u001b';
    
    private static final Set<String> GSM7BIT = new HashSet<>(Arrays.asList(
        new String[] {
            "@", "£", "$", "¥", "è", "é", "ù", "ì", "ò", "Ç", "\n", "Ø", "ø", "\r", "Å", "å",
            "Δ", "_", "Φ", "Γ", "Λ", "Ω", "Π", "Ψ", "Σ", "Θ", "Ξ", "\u001b", "Æ", "æ", "ß", "É",
            " ", "!", "'", "#", "¤", "%", "&", "\"", "(", ")", "*", "+", ",", "-", ".", "/",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?",
            "¡", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Ä", "Ö", "Ñ", "Ü", "§",
            "¿", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "ä", "ö", "ñ", "ü", "à"
        }
    ));
    
    private static final Set<String> GSM7BITEXT = new HashSet<>(Arrays.asList(
        new String[] {
            "\f", "^", "{", "}", "\\", "[", "~", "]", "|", "€"
        }
    ));
        
    public int getCharset(String content) {

        for (int i = 0; i < content.length(); i++) {
            if (!GSM7BIT.contains(Character.toString(content.charAt(i)))) {
                if (!GSM7BITEXT.contains(Character.toString(content.charAt(i)))) {
                    return GSM_CHARSET_UNICODE;
                }
            }
        }
        
        return GSM_CHARSET_7BIT;

    }
    
    private int getPartCount7bit(String content) {
	
	StringBuilder content7bit = new StringBuilder();

        // Add escape characters for extended charset
        for (int i = 0; i < content.length(); i++) {
            if (!GSM7BITEXT.contains(content.charAt(i)+"")) {
                content7bit.append(content.charAt(i));
            } else {
                content7bit.append('\u001b');
                content7bit.append(content.charAt(i));
            }
        }
                
        if (content7bit.length() <= 160) {

            return 1;

        } else {

            // Start counting the number of messages
            int parts = (int)Math.ceil(content7bit.length() / 153.0);
            int free_chars = content7bit.length() - (int)Math.floor(content7bit.length() / 153.0)*153;

            // We have enough free characters left, don't care about escape character at the end of sms part
            if (free_chars >= parts-1) {
                return parts;
            }

            // Reset counter
            parts = 0;
            while (content7bit.length() > 0) {

                // Advance sms counter
                parts++;

                // Check for trailing escape character
                if (content7bit.length() >= 152 && content7bit.charAt(152) == GSM_7BIT_ESC) {
                    content7bit.delete(0, 152);
                } else {
                    content7bit.delete(0, 153);
                }

            }

            return parts;

        }
                
    }
    
    public int getPartCount(String content) {

        int charset = this.getCharset(content);

        if (charset == GSM_CHARSET_7BIT) {

            return this.getPartCount7bit(content);

        } else if (charset == GSM_CHARSET_UNICODE) {

            if (content.length() <= 70) {
                return 1;
            } else {
                return (int)Math.ceil(content.length() / 67.0);
            }

        }

        return -1;

    } // getPartCount

} // SmsLengthCalculator
