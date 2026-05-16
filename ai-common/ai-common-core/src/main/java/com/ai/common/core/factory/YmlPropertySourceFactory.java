package com.ai.common.core.factory;

import com.ai.common.core.constant.Constant;
import com.ai.common.core.utils.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Objects;

/**
 * YML配置源工厂
 *
 * @author root 2026-05-16 16:04
 */
public class YmlPropertySourceFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String sourceName = resource.getResource().getFilename();
        if (StringUtils.isNotBlank(sourceName) && StringUtils.endsWithAny(sourceName, Constant.DOT_YML, Constant.DOT_YAML)) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return new PropertiesPropertySource(sourceName, Objects.requireNonNull(factory.getObject()));
        }
        return super.createPropertySource(name, resource);
    }
}
