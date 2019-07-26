package com.environment.resource

class ResourceClassGenerator {
    fun getClassString(resourceName: String): String {
        return """
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class $resourceName extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        Control propertiesFileOnlyControl = new Control() {
            @Override
            public List<String> getFormats(String baseName) {
                return Arrays.asList("java.properties");
            }
        };
        ResourceBundle.clearCache();
        ResourceBundle propertiesFileBundle = ResourceBundle.getBundle("$resourceName", propertiesFileOnlyControl);
        List<String[]> list = Collections.list(propertiesFileBundle.getKeys()).stream()
                .map(it -> {
                    String value = propertiesFileBundle.getString(it);
                    Pattern p = Pattern.compile("(\\${'$'}\\{.+?})");
                    Matcher m = p.matcher(value);
                    StringBuffer s = new StringBuffer();
                    while (m.find()) {
                        String variable = System.getenv(stripTemplateCharacters(m.group(1)));
                        if (variable == null) {
                            variable = System.getProperty(stripTemplateCharacters(m.group(1)));
                        }
                        if (variable != null) {
                            m.appendReplacement(s, variable);
                        }
                    }
                    m.appendTail(s);
                   return new String[] {it, s.toString()};
                })
                .collect(Collectors.toList());
        return list.toArray(new String[list.size()][]);
    }

    private String stripTemplateCharacters(String text) {
        return text.substring(2, text.length()-1);
    }

}
    """
    }
}