package org.nature.platform.localization.i18n;

import java.util.Locale;

/**
 *
 * @author htl
 */
public class HopeResourceBundle {

    public static final String CORE_BUNDLE_PATH = "org.nature.messages";

    public static String get(String key) {
        return ResourceBundleUtils.get(key, CORE_BUNDLE_PATH);
    }
    
    public static String get(String key, Locale locale) {
        return ResourceBundleUtils.get(key, CORE_BUNDLE_PATH, HopeResourceBundle.class.getClassLoader(), locale);
    }

    public static String get(String key, Locale locale, Object... array) {
        return ResourceBundleUtils.get(key, CORE_BUNDLE_PATH, HopeResourceBundle.class.getClassLoader(), locale, array);
    }

    public static String get(String key, Object... array) {
        return ResourceBundleUtils.get(key, CORE_BUNDLE_PATH, HopeResourceBundle.class.getClassLoader(), array);
    }
}
