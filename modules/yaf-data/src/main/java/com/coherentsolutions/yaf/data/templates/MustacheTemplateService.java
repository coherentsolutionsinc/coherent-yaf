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

package com.coherentsolutions.yaf.data.templates;


import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.core.l10n.L10nService;
import com.coherentsolutions.yaf.core.template.TemplateConfig;
import com.coherentsolutions.yaf.core.template.TemplateService;
import com.coherentsolutions.yaf.core.template.TemplateValue;
import com.coherentsolutions.yaf.core.template.TemplateValues;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import com.github.javafaker.Faker;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Mustache template service.
 */
@Service
@Primary
@Slf4j
public class MustacheTemplateService implements TemplateService, InitializingBean {

    /**
     * The Configs.
     */
    @Autowired
    List<TemplateConfig> configs;

    /**
     * The Localization service.
     */
    @Autowired
    L10nService localizationService;

    /**
     * The Faker.
     */
    @Autowired
    Faker faker;

    /**
     * The Loaders.
     */
    List<Mustache.TemplateLoader> loaders;

    /**
     * The Consts ctx.
     */
    Map<String, Object> constsCtx;

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    @Override
    public void afterPropertiesSet() throws Exception {
        initLoaders();
    }

    /**
     * Init loaders.
     */
    protected void initLoaders() {
        loaders = configs.stream().map(c -> new MustacheResourceTemplateLoader(c.getPrefix(), c.getSuffix()))
                .collect(Collectors.toList());
        constsCtx = new HashMap<>();

        Mustache.Lambda l10n = (frag, out) -> {
            String key = frag.execute();
            out.write(localizationService.getMessage(key));
        };
        constsCtx.put("l10n", l10n);
        constsCtx.put("faker", faker);
    }

    /**
     * Gets template.
     *
     * @param name the name
     * @return the template
     */
    protected LoaderMatchResult getTemplate(String name) {
        for (Mustache.TemplateLoader loader : loaders) {
            try {
                Reader reader = loader.getTemplate(name);
                return new LoaderMatchResult(loader, reader);
            } catch (Exception e) {
                log.error("Error while loading template " + name + " with loader " + loader, e);
            }
        }
        return null;
    }

    public String processTemplateString(String templateString, Map<String, Object> ctx) throws DataYafException {
        Template tmpl = Mustache.compiler().compile(templateString);
        return execTemplate(tmpl, ctx);
    }

    public String processTemplate(String templateName, Map<String, Object> ctx) throws DataYafException {
        LoaderMatchResult loaderMatchResult = getTemplate(templateName);
        if (loaderMatchResult == null) {
            throw new DataYafException("Unable to find template " + templateName);
        }
        // TODO cache?
        Template tmpl = Mustache.compiler().emptyStringIsFalse(true).defaultValue("")
                .withLoader(loaderMatchResult.getLoader()).compile(loaderMatchResult.getReader());

        return execTemplate(tmpl, ctx);
    }

    private String execTemplate(Template template, Map<String, Object> ctx) {
        ctx.putAll(constsCtx);
        TestExecutionContext testExecutionContext = beanUtils.tec();
        if (testExecutionContext != null && testExecutionContext.getTestInfo() != null) {

            List<Annotation> annotations = testExecutionContext.getTestInfo().getAnnotations();

            List<TemplateValue> templateValues = YafReflectionUtils.getAllAnnotationsByType(annotations,
                    TemplateValue.class, TemplateValues.class);

            templateValues.forEach(a -> ctx.put(a.key(), a.value()));

            ctx.put("tctx", testExecutionContext);
        }
        return template.execute(ctx);
    }

    /**
     * The type Loader match result.
     */
    @Data
    @AllArgsConstructor
    protected class LoaderMatchResult {
        /**
         * The Loader.
         */
        Mustache.TemplateLoader loader;
        /**
         * The Reader.
         */
        Reader reader;
    }

}
