package org.sohagroup.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sohagroup.domain.Flow;
import org.sohagroup.model.FlowNodeResponse;
import org.sohagroup.repository.FlowRepository;
import org.sohagroup.util.Feign.FlowEndpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ObjectDynamicControllerRegister {
    private final ObjectDynamicControllerGenerator controllerGenerator;
    private final RequestMappingHandlerMapping handlerMapping;
    private final FlowRepository flowRepository;
    private FlowEndpoint flowEndpoint;
    private static final Logger log = LoggerFactory.getLogger(ObjectDynamicControllerRegister.class);

    public ObjectDynamicControllerRegister(ObjectDynamicControllerGenerator controllerGenerator, @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
                                           DataSource dataSource, FlowRepository flowRepository, FlowEndpoint flowEndpoint) {
        this.controllerGenerator = controllerGenerator;
        this.handlerMapping = handlerMapping;
        this.flowRepository = flowRepository;
        this.flowEndpoint = flowEndpoint;
    }

    @PostConstruct
    public void registerApiDeployTime() {
        List<FlowNodeResponse> flowNodeResponses = flowEndpoint.getFlowNode(1);
        flowNodeResponses.stream().forEach(flowNodeResponse -> {
            createApi(flowNodeResponse);
        });
    }

    //    @PostConstruct
    public Flow createApi(FlowNodeResponse flowNodeResponse) {
        RequestMethod method = RequestMethod.DELETE;
        method = flowNodeResponse.getMethod().equals("GET") ? RequestMethod.GET : RequestMethod.POST;

        Flow saveFlow;
        try {
            Object userController = controllerGenerator.generateUserController(flowNodeResponse.getFlowResponse());
            handlerMapping.registerMapping(
                RequestMappingInfo.paths(flowNodeResponse.getUrl())
                    .methods(method)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build(),
                userController,
                userController.getClass()
                    .getMethod("mainMethod", HttpServletRequest.class, Map.class, Map.class));
            Flow flow = new Flow();
            flow.setId(flowNodeResponse.getFlowResponse().getId());
            flow.setName(flowNodeResponse.getFlowResponse().getName());
            flow.setMethod(flowNodeResponse.getMethod());
            flow.setEndPoint(flowNodeResponse.getUrl());

            saveFlow = flowRepository.save(flow);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return saveFlow;
    }
}
