package com.coherentsolutions.yaf.core.api.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ApiPropertiesManager {

    Map<String, ApiProperties> props;

}
