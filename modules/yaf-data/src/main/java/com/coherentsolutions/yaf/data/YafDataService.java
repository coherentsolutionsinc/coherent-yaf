package com.coherentsolutions.yaf.data;

import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.BeanInitYafException;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.data.reader.DataReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class YafDataService {

    @Autowired
    YafBeanUtils beanUtils;

    @Autowired
    TestExecutionContext testExecutionContext;


    public <T> T createNewDataBean(Class<T> fieldType) {
        return this.createNewDataBean(fieldType, null);
    }

    public <T> T createNewDataBean(Class<T> fieldType, YafData data) {
        try {
            if (data == null) {
                data = beanUtils.getAnnotation(fieldType, YafData.class);
            }
            DataReader dataReader = beanUtils.getBean(data.reader());
            return (T) dataReader.readData(data, fieldType, testExecutionContext);
        } catch (Exception e) {
            throw new BeanInitYafException(e.getMessage(), e);
        }
    }

}
