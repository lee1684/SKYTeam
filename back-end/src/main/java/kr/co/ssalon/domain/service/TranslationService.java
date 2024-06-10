package kr.co.ssalon.domain.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final Translate translate;

    public String translateText(String text, String sourceLang, String targetLang) {
        if (!containsKorean(text)) {
            return text;
        }

        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLang),
                Translate.TranslateOption.targetLanguage(targetLang)
        );
        return translation.getTranslatedText();
    }

    public static boolean containsKorean(String text) {
        for (char c : text.toCharArray()) {
            if (c >= '\uAC00' && c <= '\uD7A3') {
                return true;
            }
        }
        return false;
    }
}
