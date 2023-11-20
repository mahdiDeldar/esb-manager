package org.sohagroup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sohagroup.enumeration.NodeResponseType;

public class FlowNodeResponse {
    private int id;
    private  String url;
    private String method;
    @JsonProperty("flow")
    private FlowResponse flowResponse;
    @JsonProperty("returnType")
    private NodeResponseType nodeResponseType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public FlowResponse getFlowResponse() {
        return flowResponse;
    }

    public void setFlowResponse(FlowResponse flowResponse) {
        this.flowResponse = flowResponse;
    }

    public NodeResponseType getNodeResponseType() {
        return nodeResponseType;
    }

    public void setNodeResponseType(NodeResponseType nodeResponseType) {
        this.nodeResponseType = nodeResponseType;
    }

    @Override
    public String toString() {
        return "FlowNodeResponse{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", method='" + method + '\'' +
            ", flowResponse=" + flowResponse +
            '}';
    }
}
