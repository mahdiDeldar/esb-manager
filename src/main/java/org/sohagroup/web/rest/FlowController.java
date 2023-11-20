package org.sohagroup.web.rest;

import org.sohagroup.domain.Flow;
import org.sohagroup.model.FlowNodeResponse;
import org.sohagroup.util.ObjectDynamicControllerRegister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowController {
    private final ObjectDynamicControllerRegister objectDynamicControllerRegister;

    public FlowController(ObjectDynamicControllerRegister objectDynamicControllerRegister) {
        this.objectDynamicControllerRegister = objectDynamicControllerRegister;
    }

    @PostMapping("/api/flow")
    public ResponseEntity<Flow> saveFlow(@RequestBody FlowNodeResponse flowNodeResponse) {
        Flow flow = objectDynamicControllerRegister.createApi(flowNodeResponse);
        return new ResponseEntity<Flow>(flow, HttpStatus.OK);
    }
}
