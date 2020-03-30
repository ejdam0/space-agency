package pl.strzelecki.spaceagency.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class CustomConfigurationFactory extends ConfigurationFactory {

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {

        AppenderComponentBuilder console = builder.newAppender("Stdout", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);

        LayoutComponentBuilder standardLayout = builder.newLayout("PatternLayout");
        standardLayout.addAttribute("pattern", "%d [%t] %C{} |%-5level| %msg%n%throwable");

        console.add(standardLayout);
        builder.add(console);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.ERROR);
        LoggerComponentBuilder additionalLoggerInfo = builder.newLogger("pl", Level.INFO);
        LoggerComponentBuilder additionalLoggerTrace = builder.newLogger("pl", Level.TRACE);

        additionalLoggerInfo.addAttribute("additivity", false);
        additionalLoggerTrace.addAttribute("additivity", false);

        rootLogger.add(builder.newAppenderRef("Stdout"));
        additionalLoggerInfo.add(builder.newAppenderRef("Stdout"));
        additionalLoggerTrace.add(builder.newAppenderRef("Stdout"));

        builder.add(rootLogger);
        builder.add(additionalLoggerInfo);
        builder.add(additionalLoggerTrace);
        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{"*"};
    }
}