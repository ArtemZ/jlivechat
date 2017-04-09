package ru.netdedicated.i18n;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by artemz on 21.01.17.
 */
public class I18nService {
    private Locale locale;

    public I18nService(Locale locale) {
        this.locale = locale;
    }
    public String getStringForCode(String code, String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        if (bundle == null) {
            throw new NullPointerException("Cant load resource bundle messages for locale " + locale );
        }
        MessageFormat formatter = new MessageFormat(bundle.getString(code));
        return formatter.format(args);

    }
    public String getStringForCode(String code){
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        if (bundle == null) {
            throw new NullPointerException("Cant load resource bundle messages for locale " + locale );
        }
        return bundle.getString(code);
    }
}
