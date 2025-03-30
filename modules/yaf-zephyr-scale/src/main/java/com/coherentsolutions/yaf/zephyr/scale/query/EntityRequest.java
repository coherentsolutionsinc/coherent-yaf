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

package com.coherentsolutions.yaf.zephyr.scale.query;

import com.coherentsolutions.yaf.zephyr.scale.domain.ZephyrScaleEntity;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;

/**
 * Represents a request to an entity with methods to find, get, and create entities.
 * <p>
 * This class provides methods to query entities with pagination, find entities by key or name,
 * get an entity by ID, and create a new entity. It uses RestAssured for making HTTP requests.
 * </p>
 *
 * @param <T> the type of the entity
 * @param <K> the type of the paging request
 */
public class EntityRequest<T extends ZephyrScaleEntity, K extends PagingReq> {

    private final RequestSpecification specification;
    private final Class entityClass;
    private final String projectKey;
    private final String url;

    /**
     * Constructs a new EntityRequest with the specified parameters.
     *
     * @param specification the request specification
     * @param entityClass   the class of the entity
     * @param url           the URL for the request
     * @param projectKey    the project key
     */
    public EntityRequest(RequestSpecification specification, Class entityClass, String url, String projectKey) {
        this.specification = specification;
        this.entityClass = entityClass;
        this.url = url;
        this.projectKey = projectKey;
    }

    /**
     * Queries entities with the given paging request.
     *
     * @param pagingReq the paging request
     * @return a list of entities
     */
    @SuppressWarnings("unchecked")
    public List<T> query(K pagingReq) {
        Map<String, String> params = pagingReq.getQueryParams();
        if (projectKey != null) {
            params.put("projectKey", projectKey);
        }
        return given(specification).params(params).get(url).jsonPath().getList("values", entityClass);
    }

    /**
     * Finds an entity by its key.
     *
     * @param pagingReq the paging request
     * @param key       the key of the entity
     * @return the entity with the given key, or null if not found
     */
    public T findByKey(K pagingReq, String key) {
        return find(pagingReq, (f) -> StringUtils.equalsIgnoreCase(f.getKey(), key));
    }

    /**
     * Finds an entity by its name.
     *
     * @param pagingReq the paging request
     * @param name      the name of the entity
     * @return the entity with the given name, or null if not found
     */
    public T findByName(K pagingReq, String name) {
        return find(pagingReq, (f) -> StringUtils.equalsIgnoreCase(f.getName(), name));
    }

    /**
     * Finds an entity that matches the given predicate.
     *
     * @param pagingReq the paging request
     * @param p         the predicate to match
     * @return the entity that matches the predicate, or null if not found
     */
    public T find(K pagingReq, Predicate<T> p) {
        T res;
        List<T> list;
        do {
            try {
                list = query(pagingReq);
                res = list.stream().filter(p).findFirst().orElse(null);
                pagingReq.setStartAt(pagingReq.getStartAt() + pagingReq.getMaxResults());
            } catch (Exception ex) {
                // workaround to handle out of bounds reqs
                return null;
            }
        } while (res == null && !list.isEmpty());
        return res;
    }

    /**
     * Gets an entity by its ID.
     *
     * @param id the ID of the entity
     * @return the entity with the given ID
     */
    @SuppressWarnings("unchecked")
    public T get(Integer id) {
        return (T) given(specification).get(url + "/" + id).as(entityClass);
    }

    /**
     * Creates a new entity.
     *
     * @param obj the entity to create
     * @return the created entity with its ID set
     */
    public T create(T obj) {
        obj.setProjectKey(projectKey);
        int id = given(specification).body(obj).post(url).jsonPath().getInt("id");
        obj.setId(id);
        return obj;
    }

    /**
     * Updates an existing entity.
     * <p>
     * This method updates the provided entity by sending a PUT request to the specified URL.
     * It sets the project key on the entity before making the request. If the update request
     * fails, it throws a {@link RuntimeException}.
     * </p>
     *
     * @param obj the entity to update
     * @return the updated entity
     * @throws RuntimeException if the update request fails
     */
    public String update(T obj) {
        return given(specification).body(obj).put(url).asString();
    }

}
