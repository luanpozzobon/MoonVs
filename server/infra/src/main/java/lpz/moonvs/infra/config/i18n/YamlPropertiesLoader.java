package lpz.moonvs.infra.config.i18n;

import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.PropertiesPersister;

import java.io.*;
import java.util.Properties;

public class YamlPropertiesLoader implements PropertiesPersister {
    @Override
    public void load(Properties props, InputStream is) throws IOException {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new InputStreamResource(is));
        props.putAll(yaml.getObject());
    }

    @Override
    public void load(Properties props, Reader reader) throws IOException {
        final InputStream is = ReaderInputStream.builder().setReader(reader).get();
        this.load(props, is);
    }

    @Override
    public void store(Properties props, OutputStream os, String header) throws IOException {
        throw new UnsupportedOperationException("Storing is not supported by YamlPropertiesLoader");
    }

    @Override
    public void store(Properties props, Writer writer, String header) throws IOException {
        throw new UnsupportedOperationException("Storing is not supported by YamlPropertiesLoader");
    }

    @Override
    public void loadFromXml(Properties props, InputStream is) throws IOException {
        throw new UnsupportedOperationException("Loading from XML is not supported by YamlPropertiesLoader");
    }

    @Override
    public void storeToXml(Properties props, OutputStream os, String header) throws IOException {
        throw new UnsupportedOperationException("Storing to XML is not supported by YamlPropertiesLoader");
    }

    @Override
    public void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException {
        throw new UnsupportedOperationException("Storing to XML is not supported by YamlPropertiesLoader");
    }
}
