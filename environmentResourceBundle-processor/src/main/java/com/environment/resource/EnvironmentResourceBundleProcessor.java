package com.environment.resource;

import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class EnvironmentResourceBundleProcessor extends AbstractProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentResourceBundle.class);

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(EnvironmentResourceBundle.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ResourceClassGenerator generator = new ResourceClassGenerator();

        for (Element element : roundEnv.getElementsAnnotatedWith(EnvironmentResourceBundle.class)) {

            String[] resourceNames = element.getAnnotation(EnvironmentResourceBundle.class).names();
            for (String eachResourceName : resourceNames) {
                try {
                    JavaFileObject file = processingEnv.getFiler().createSourceFile(eachResourceName);
                    try (Writer sourceWriter = file.openWriter()) {
                        sourceWriter.write(generator.getClassString(eachResourceName));
                    } catch (Exception e) {
                        logger.error("Failed to write", e);
                    }
                } catch (Exception e) {
                    logger.error("Failed to write", e);
                }
            }

        }

        return true;
    }

}
