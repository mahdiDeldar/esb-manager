package org.sohagroup.util.Feign;

import feign.Headers;
import org.sohagroup.model.FlowNodeResponse;
import org.sohagroup.model.FlowResponse;
import org.sohagroup.model.NodeAttribute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "flowEndpoint", url = "${setting.url.flow}")
public interface FlowEndpoint {

    @GetMapping("nodes")
    @Headers("Content-Type: application/json")
    List<FlowNodeResponse> getFlowNode(@RequestParam("order.equals") int order);

    @GetMapping("flows")
    @Headers("Content-Type: application/json")
    List<FlowResponse> getFlowList();

    @GetMapping("nodes")
    @Headers("Content-Type: application/json")
    List<FlowNodeResponse> getFlowNodeListByFlowId(@RequestParam("order.greaterThan") int order, @RequestParam("flowId.equals") long flowId);

    @GetMapping("node-attributes")
    @Headers("Content-Type: application/json")
    List<NodeAttribute> getNodeAttributeByNodeId(@RequestParam("nodeId.equals") int nodeId, @RequestParam("type.equals") String attributeType);
//http://flow-managment.apps.sohagroup.org/api/node-attributes?nodeId.equals=3001&type.equals=REQUEST&category.equals=REQUEST_HEADER
}
