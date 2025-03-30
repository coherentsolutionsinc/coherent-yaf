/*
 * MIT License
 *
 * Copyright (c) 2021 - 2024 Coherent Solutions Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.coherentsolutions.yaf.report.template;


import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.core.l10n.L10nService;
import com.coherentsolutions.yaf.core.template.TemplateService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.coherentsolutions.yaf.core.consts.Consts.UTF8;

/**
 * The type Velocity template service.
 */
@Service("velocity")
public class VelocityTemplateService implements TemplateService {

    /**
     * The constant TEMPLATE_EXT.
     */
    public static final String TEMPLATE_EXT = ".vm";

    /**
     * The Velocity engine.
     */
    VelocityEngine velocityEngine;

    /**
     * The Localization service.
     */
    @Autowired
    L10nService localizationService;

    /**
     * The Templates cache.
     */
    Map<String, Template> templatesCache = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Velocity template service.
     */
    public VelocityTemplateService() {
        Properties properties = new Properties();
        properties.setProperty("input.encoding", UTF8);
        properties.setProperty("output.encoding", UTF8);
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine = new VelocityEngine(properties);
    }

    /**
     * Gets template by content.
     *
     * @param templateString the template string
     * @return the template by content
     * @throws DataYafException the data yaf exception
     */
    protected Template getTemplateByContent(String templateString) throws DataYafException {
        Template template = templatesCache.get(templateString);
        if (template == null) {
            try {
                RuntimeServices rs = RuntimeSingleton.getRuntimeServices();
                StringReader sr = new StringReader(templateString);
                SimpleNode sn = rs.parse(sr, "CustomTemplate" + Math.random());
                template = new Template();
                template.setRuntimeServices(rs);
                template.setData(sn);
                template.initDocument();
                templatesCache.put(templateString, template);
            } catch (ParseException e) {
                throw new DataYafException("Unable to process template", e);
            }
        }
        return template;
    }

    /**
     * Process template string.
     *
     * @param template the template
     * @param ctx      the ctx
     * @return the string
     */
    protected String processTemplate(Template template, Map<String, Object> ctx) {
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext(ctx);
        context.put("l10n", localizationService);
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    @Override
    public String processTemplateString(String templateString, Map<String, Object> ctx) throws DataYafException {
        return processTemplate(getTemplateByContent(templateString), ctx);
    }

    @Override
    public String processTemplate(String templateName, Map<String, Object> ctx) throws DataYafException {
        if (!templateName.endsWith(TEMPLATE_EXT)) {
            templateName += TEMPLATE_EXT;
        }
        return processTemplate(velocityEngine.getTemplate(templateName), ctx);
    }

}
