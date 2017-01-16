package org.nature.platform.localization.i18n;

import org.nature.platform.utils.HumaniseCamelCase;
import org.nature.platform.utils.StringTool;


import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Logger;

/**
 *
 * @author htl
 */
public class I18N {

    private static final Logger logger = Logger.getLogger(I18N.class.getName()); 
    public static String BUNDLE = null;
    
    public static String get(String key) {
        if ( BUNDLE == null) {
            return key;
        }
        return ResourceBundleUtils.get(key, BUNDLE, Thread.currentThread().getContextClassLoader(), (Object[]) null);
    }
    
    public static String get(String key, Object... parameters) {
        if ( BUNDLE == null) {
            return key;
        }
        return ResourceBundleUtils.get(key, BUNDLE, Thread.currentThread().getContextClassLoader(), parameters);
    }

    public static String getDatePattern() {
        return ((SimpleDateFormat) DateFormat.getDateInstance(SimpleDateFormat.SHORT, getLocale())).toPattern();
    }

    /**
     *
     * @param clazz
     * @param fieldName
     * 
     * @return the attribute name from configured resourcebundle the message
     * for: simple name (FirstLetter lowercase) + "." + property. Example: Class
     * Person and attribute name - person.name
     */
    public static String getAttributeName(Class clazz, String fieldName) {
        String property = StringTool.getLowerFirstLetter(clazz.getSimpleName()) + "." + fieldName;
        String value = I18N.get(property);
        //try to find in superclass
        if ((value == null || value.isEmpty() || value.equals(property)) && clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            return getAttributeName(clazz.getSuperclass(), fieldName);
        }
        if (value != null && value.equals(property)) {
            return new HumaniseCamelCase().humanise(fieldName);
        }
        return value;
    }

    public static Locale getLocale() {
        //for JSF Context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            if (facesContext.getViewRoot() != null) {
                return facesContext.getViewRoot().getLocale();
            }
            if (facesContext.getApplication() != null && facesContext.getApplication().getDefaultLocale() != null) {
                return facesContext.getApplication().getDefaultLocale();
            }
        }
        return ResourceBundleUtils.PT_BR;
    }
}
