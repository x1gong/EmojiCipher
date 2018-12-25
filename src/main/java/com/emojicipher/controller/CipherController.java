package com.emojicipher.controller;

import com.emojicipher.MainApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CipherController {

    private static final String TEXT_KEY = "text";
    private static final String RESULT_KEY = "result";
    private static final int MAX_LENGTH = 10000;

    @ResponseBody
    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public Map<String, String> handleEncrypt(@RequestBody Map<String, String> map) {
        if (! map.containsKey(TEXT_KEY)) {
            return null;
        }
        String originalText = map.get(TEXT_KEY);
        if (originalText.length() > MAX_LENGTH) {
            return null;
        }
        try {
            String emojiText = MainApplication.getEmoji().encryptToEmoji(originalText);
            Map<String, String> result = new HashMap<>();
            result.put(RESULT_KEY, emojiText);
            return result;
        } catch (Exception e) {
            return null;
        }
    }


    @ResponseBody
    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    public Map<String, String> handleDecrypt(@RequestBody Map<String, String> map) {
        if (! map.containsKey(TEXT_KEY)) {
            return null;
        }
        String emojiText = map.get(TEXT_KEY);
        if (emojiText.length() > 8 * MAX_LENGTH) {
            return null;
        }
        try {
            String originalText = MainApplication.getEmoji().decryptEmoji(emojiText);
            Map<String, String> result = new HashMap<>();
            result.put(RESULT_KEY, originalText);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
