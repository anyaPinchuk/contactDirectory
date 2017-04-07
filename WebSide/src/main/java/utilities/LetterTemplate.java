package utilities;

import org.apache.commons.collections4.CollectionUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LetterTemplate {
    private ST template;
    private String name;
    private String content;
    private static STGroup group = new STGroupFile("templates.stg");

    public LetterTemplate(String name) {
        template = group.getInstanceOf(name);
        this.name = name;
    }

    public static List<LetterTemplate> getTemplates() {
        List<LetterTemplate> templates = new ArrayList<>();
        Set<String> names = group.getTemplateNames();
        if (!CollectionUtils.isEmpty(names))
            names.forEach(name -> {
                LetterTemplate letterTemplate = new LetterTemplate(name.substring(1));
                ST template = letterTemplate.getTemplate();
                Map<String, Object> attributes = template.getAttributes();
                attributes.forEach((key, value) -> {
                    switch (key) {
                        case "name":
                            template.add("name", "<name>");
                            break;
                        case "surname":
                            template.add("surname", "<surname>");
                            break;
                    }
                });
                letterTemplate.setContent(template.render());
                templates.add(letterTemplate);
            });
        return templates;
    }

    public ST getTemplate() {
        return template;
    }

    public void setTemplate(ST template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
