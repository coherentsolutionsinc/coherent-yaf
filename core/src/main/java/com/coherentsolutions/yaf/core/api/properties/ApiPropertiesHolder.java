package com.coherentsolutions.yaf.core.api.properties;

import com.coherentsolutions.yaf.core.consts.Consts;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@Data
@ConfigurationProperties(prefix = Consts.FRAMEWORK_NAME + ".api")
public class ApiPropertiesHolder extends HashMap<String, ApiPropsWrapper> {

}
