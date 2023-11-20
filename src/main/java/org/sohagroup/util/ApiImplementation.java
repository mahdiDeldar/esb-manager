package org.sohagroup.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.sohagroup.domain.Flow;
import org.sohagroup.enumeration.*;
import org.sohagroup.model.FlowNodeResponse;
import org.sohagroup.model.FlowResponse;
import org.sohagroup.model.NodeAttribute;
import org.sohagroup.repository.FlowRepository;
import org.sohagroup.util.Feign.FlowEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ApiImplementation {
    @Autowired
    private HttpClient thttpClient;
    @Autowired
    private FlowEndpoint tFlowEndpoint;
    @Autowired
    private FlowRepository tflowRepository;
    private static HttpClient httpClient;
    private static FlowEndpoint flowEndpoint;
    private static FlowRepository flowRepository;

    @PostConstruct
    public void init() {
        this.httpClient = thttpClient;
        this.flowEndpoint = tFlowEndpoint;
        this.flowRepository = tflowRepository;
    }

    public static HttpResponse apiRequestGenerate(ApiMethodTypeEnum apiMethodTypeEnum, ApiRequestType apiRequestType, String url, @Nullable Map<String, Object> header,
                                                  @Nullable Map<String, Object> param, @Nullable Map<String, Object> body) throws IOException, URISyntaxException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        HttpResponse response = null;
        if (!param.isEmpty()) {
            Set<String> paramKeyList = param.keySet();
            paramKeyList.stream().forEach(key -> {
                builder.queryParam(key, param.get(key));
            });
        }

        if (apiMethodTypeEnum.equals(ApiMethodTypeEnum.GET) && apiRequestType.equals(ApiRequestType.REST)) {
            HttpGet request = new HttpGet(builder.toUriString());
            if (header != null) {
                Set<String> headerKeyList = header.keySet();
                headerKeyList.stream().forEach(head -> {
                    request.setHeader(head, header.get(head).toString());
                });
            }
            response = httpClient.execute(request);
        }
        if (apiMethodTypeEnum.equals(ApiMethodTypeEnum.POST) && apiRequestType.equals(ApiRequestType.REST)) {
            HttpPost request = new HttpPost();
            ObjectMapper objectMapper = new ObjectMapper();
            String jacksonData = objectMapper.writeValueAsString(body);
            StringEntity entity = new StringEntity(jacksonData);
            entity.setContentType("application/json");
            request.setEntity(entity);
            request.setURI(new URI(builder.toUriString()));
            response = httpClient.execute(request);
        }
        return response;
    }

    public static ResponseEntity<JsonNode> mainMethod(HttpServletRequest requestUrl, Map<String, Object> param, // don't use primitive types
                                                      Map<String, Object> body) throws IOException, URISyntaxException, RuntimeException, JsonProcessingException {
        String url = requestUrl.getRequestURI();
        ObjectMapper mapper = new ObjectMapper();
        Flow flow = flowRepository.findFlowByEndPoint(url).orElseThrow();
        List<FlowNodeResponse> flowNodeResponses = flowEndpoint.getFlowNodeListByFlowId(1, flow.getId());
        List<NodeAttribute> requestNodeAttributeList = flowEndpoint.getNodeAttributeByNodeId(flowNodeResponses.get(0).getId(), NodeAttributeType.request.getValue());
        List<NodeAttribute> responseNodeAttributeList = flowEndpoint.getNodeAttributeByNodeId(flowNodeResponses.get(0).getId(), NodeAttributeType.response.getValue());
        Map<String, Object> requestParam = new HashMap<>();
        Map<String, Object> requestHeader = new HashMap<>();
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> forcedAttributeNull = new HashMap<>();
        JsonNode node = null;
        requestNodeAttributeList.stream().filter(nodeCategory -> nodeCategory.getCategory().equals(NodeAttributeCategory.param.getValue())).forEach(attribute -> {
            if (param.containsKey(attribute.getKey())) {
                requestParam.put(attribute.getKey(), param.get(attribute.getKey()));
            } else if (attribute.getOptional().equals(Boolean.FALSE)) {
                forcedAttributeNull.put("param", attribute.getKey());
            }
        });
        requestNodeAttributeList.stream().filter(nodeCategory -> nodeCategory.getCategory().equals(NodeAttributeCategory.header.getValue())).forEach(attribute -> {
            if (param.containsKey(attribute.getKey())) {
                requestHeader.put(attribute.getKey(), param.get(attribute.getKey()));
            } else if (attribute.getOptional().equals(Boolean.FALSE)) {
                forcedAttributeNull.put("header", attribute.getKey());
            }
        });
        requestNodeAttributeList.stream().filter(nodeCategory -> nodeCategory.getCategory().equals(NodeAttributeCategory.body.getValue())).forEach(attribute -> {
            if (body.containsKey(attribute.getKey())) {
                requestBody.put(attribute.getKey(), body.get(attribute.getKey()));
            } else if (attribute.getOptional().equals(Boolean.FALSE)) {
                forcedAttributeNull.put("body", attribute.getKey());
            }
        });
        if (!forcedAttributeNull.isEmpty()) {
            return new ResponseEntity<>(node, HttpStatus.valueOf(400));
        }

        String nodeUrl = flowNodeResponses.get(0).getUrl();
        HttpResponse
            response = apiRequestGenerate(ApiMethodTypeEnum.valueOf(flowNodeResponses.get(0).getMethod().toUpperCase()), ApiRequestType.REST, nodeUrl, requestHeader, requestParam, body);
        JsonNode jsonNode = mapper.readTree(EntityUtils.toString(response.getEntity()));
        List<ObjectNode> objectNodeList = new ArrayList<>();

        if (response.getStatusLine().getStatusCode() != 200) {
            JsonNode jsonNodeException = mapper.readTree(EntityUtils.toString(response.getEntity()));
            return new ResponseEntity<>(jsonNodeException, HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
        }
        if (flowNodeResponses.get(0).getNodeResponseType().equals(NodeResponseType.List)) {
            ArrayNode jsonNodeList = (ArrayNode) jsonNode;

            jsonNodeList.forEach(jsonNode1 -> {
                ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
                responseNodeAttributeList.stream().forEach(field -> {
                    responseNode.put(field.getKey(), jsonNode1.get(field.getKey()));
                });
                objectNodeList.add(responseNode);
            });
            node = mapper.convertValue(objectNodeList, JsonNode.class);
        } else {
            ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
            responseNodeAttributeList.forEach(field -> {
                responseNode.put(field.getKey(), jsonNode.get(field.getKey()));
            });
            node = mapper.convertValue(responseNode, JsonNode.class);
        }
        return new ResponseEntity<>(node, HttpStatus.OK);
    }
}
