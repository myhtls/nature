package org.nature.platform.localization.i18n;

import javax.validation.MessageInterpolator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomInterpolator implements MessageInterpolator {

    private static final boolean DEBUG = false;
    private static final Logger logger = Logger.getLogger(CustomInterpolator.class.getName());

    public CustomInterpolator() {
        if (DEBUG) {
            logger.log(Level.INFO, "Construct a new instance of {0} ", this.getClass().getName());
        }
    }

    private static final Pattern PATTERN = Pattern.compile("\\{.*?\\}");

    protected static String replaceParameters(String message, Context context) {

        Matcher matcher = PATTERN.matcher(message);
        List<String> messageAttributes = new ArrayList<String>();

        while (matcher.find()) {
            messageAttributes.add(matcher.group());
        }

        if (context != null && context.getConstraintDescriptor() != null && context.getConstraintDescriptor().getAttributes() != null) {
            for (String attribute : messageAttributes) {
                Object value = context.getConstraintDescriptor().getAttributes().get(attribute.replace("{", "").replace("}", ""));
                if (value != null) {
                    message = message.replace(attribute, value.toString());
                }
            }
        }

        return message;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return getMessage(messageTemplate, context);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return getMessage(messageTemplate, context);

    }

    public String getMessage(String messageTemplate, Context context) {

        if (DEBUG) {
            logger.log(Level.INFO, "Get message for template {0} ", messageTemplate);
        }

        String message = "";

        if (messageTemplate != null && !messageTemplate.trim().isEmpty()) {
            message = I18N.get(messageTemplate);
        }

        message = replaceParameters(message, context);

        return message;
    }
}
