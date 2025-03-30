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

package com.coherentsolutions.yaf.zephyr.scale;

import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.zephyr.scale.constant.ZephyrScaleApiUrl;
import com.coherentsolutions.yaf.zephyr.scale.domain.*;
import com.coherentsolutions.yaf.zephyr.scale.exception.YafZephyrScaleException;
import com.coherentsolutions.yaf.zephyr.scale.query.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Client for interacting with the Zephyr Scale system.
 * <p>
 * This service provides methods to create folders and cycles in the Zephyr Scale system.
 * It uses {@link RestAssured} for making HTTP requests and {@link ZephyrScaleProperties}
 * to retrieve configuration properties.
 * </p>
 */
@Slf4j
@Getter
@Service
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".zephyr.scale.enabled", havingValue = "true")
public class ZephyrScaleClient {

    private final EntityRequest<Folder, FoldersReq> foldersRequest;
    private final EntityRequest<Cycle, CyclesReq> cyclesRequest;
    private final EntityRequest<TestExecution, TestExecutionReq> executionsRequest;
    private final ZephyrScaleProperties zephyrScaleProperties;
    private final RequestSpecification requestSpecification;

    /**
     * Constructs a new ZephyrScaleClient with the specified properties.
     *
     * @param zephyrScaleProperties the properties for configuring the Zephyr Scale client
     */
    @Autowired
    public ZephyrScaleClient(ZephyrScaleProperties zephyrScaleProperties) {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RequestSpecification specification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + zephyrScaleProperties.getApiKey())
                .setContentType(ContentType.JSON).setBaseUri(zephyrScaleProperties.getBaseUrl()).build();
        this.zephyrScaleProperties = zephyrScaleProperties;
        this.requestSpecification = specification;
        String projectKey = zephyrScaleProperties.getProjectKey();
        foldersRequest = new EntityRequest<>(specification, Folder.class, "folders", projectKey);
        cyclesRequest = new EntityRequest<>(specification, Cycle.class, "testcycles", projectKey);
        executionsRequest = new EntityRequest<>(specification, TestExecution.class, "testexecutions", projectKey);
    }

    /**
     * Creates a folder if it does not already exist.
     *
     * @param name     the name of the folder
     * @param parentId the parent ID of the folder
     * @param type     the type of the folder
     * @return the created or existing folder
     * @throws YafZephyrScaleException if the folder could not be found or created
     */
    public Folder createFolderIfNotExist(String name, Integer parentId, FolderType type) {
        EntityRequest<Folder, FoldersReq> foldersReq = getFoldersRequest();
        FoldersReq cycleFolderReq = new FoldersReq().setFolderType(type);
        Folder folder = foldersReq.find(cycleFolderReq, (f) -> f.getName().equals(name) && Objects.equals(f.getParentId(), parentId));

        if (folder == null) {
            folder = new Folder();
            folder.setName(name);
            folder.setParentId(parentId);
            folder.setFolderType(type);
            folder = foldersReq.create(folder);
        }

        if (folder.getId() == null) {
            throw new YafZephyrScaleException("Could not find/create folder with name " + name + " and parent id " + parentId);
        }

        return folder;
    }

    /**
     * Creates a test cycle if it does not already exist.
     *
     * @param cycleCreateDTO the data transfer object for creating the test cycle
     * @return the created or existing test cycle
     * @throws YafZephyrScaleException if the cycle could not be found or created
     */
    public Cycle createCycleIfNotExist(CycleCreateDTO cycleCreateDTO) {
        EntityRequest<Cycle, CyclesReq> cyclesReq = getCyclesRequest();
        CyclesReq newCyclesReq = new CyclesReq().setFolderId(cycleCreateDTO.getFolderId());
        Cycle cycle = cyclesReq.find(newCyclesReq, (c) -> c.getName().equals(cycleCreateDTO.getName()) && Objects.equals(c.getFolder().getId(), cycleCreateDTO.getFolderId()));

        if (Objects.isNull(cycle)) {
            cycle = cyclesReq.create(cycleCreateDTO);
        }

        if (Objects.isNull(cycle.getId())) {
            throw new YafZephyrScaleException("Could not find/create cycle with name " + cycleCreateDTO.getName() + " and folder id " + cycleCreateDTO.getFolderId());
        }

        return cyclesReq.get(cycle.getId());
    }

    /**
     * Creates an issue link in the Zephyr Scale system.
     * <p>
     * This method creates a link between a test execution and an issue in the Zephyr Scale system.
     * It uses the provided execution ID and issue ID to establish the link.
     * </p>
     *
     * @param executionId the ID of the test execution
     * @param issueId     the ID of the issue to link
     */
    public void createIssueLink(Integer executionId, Integer issueId) {
        String url = String.format(ZephyrScaleApiUrl.CREATE_ISSUE_LINK, executionId);
        EntityRequest<Link, LinkRequest> linkRequest = new EntityRequest<>(requestSpecification, Link.class, url, zephyrScaleProperties.getProjectKey());
        Link link = new Link();
        link.setIssueId(issueId);
        try {
            link = linkRequest.create(link);
            linkRequest.get(link.getId());
        } catch (Exception e) {
            log.error("Could not create link for issue [{}] and execution [{}]", issueId, executionId);
        }
    }
}
