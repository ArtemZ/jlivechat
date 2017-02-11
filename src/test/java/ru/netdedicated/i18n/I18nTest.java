package ru.netdedicated.i18n;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by artemz on 21.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class I18nTest {
    @Test
    public void testFormattingMessage(){
        I18nService i18nService = new I18nService(new Locale("en"));
        String phrase = i18nService.getStringForCode("operator.new.support.request", new String[] { "test" } );
        assert phrase.contains("test");
    }
}
