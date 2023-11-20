package org.sohagroup.util;

import feign.Param;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import org.sohagroup.model.FlowNodeResponse;
import org.sohagroup.model.FlowResponse;
import org.sohagroup.util.Feign.FlowEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Map;

@Component
public class ObjectDynamicControllerGenerator {
    private FlowEndpoint flowEndpoint;


    public Class<?> detectFieldClass(String fieldType) {
        Class<?> clazz = String.class;
        if (fieldType.equals("string")) {
            clazz = String.class;
        }
        if (fieldType.equals("long")) {
            clazz = Long.class;
        }
        if (fieldType.equals("double")) {
            clazz = Double.class;
        }
        return clazz;
    }


//    private static String setterName(String fieldName) {
//        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//    }
//
//    private static String getterName(String fieldName) {
//        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//    }

//    public Object generateModelClass(ClassController classController) throws InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
//        List<ClassField> classFieldList = new ArrayList<>();
//        classFieldList = classFieldRepository.findAllByClassController(classController);
//
//        DynamicType.Builder<?> builder = new ByteBuddy()
//            .subclass(Object.class)
//            .name(classController.getClassName());
//        for (ClassField classField : classFieldList) {
//            String fieldName = classField.getFieldName();
//            builder = builder.defineField(fieldName, detectFieldClass(classField.getFieldType().getValue()), Visibility.PRIVATE)
//                .defineMethod(getterName(fieldName), detectFieldClass(classField.getFieldType().getValue()), Visibility.PUBLIC)
//                .intercept(FieldAccessor.ofBeanProperty())
//                .defineMethod(setterName(fieldName), Void.TYPE, Visibility.PUBLIC)
//                .withParameter(detectFieldClass(classField.getFieldType().getValue()), fieldName)
//                .intercept(FieldAccessor.ofBeanProperty());
//        }
//        if (classController.getClassType() != null) {
//            Class<? extends Annotation> classType = RestController.class;
//            classType = classController.getClassType().getValue().equals("RestController") ? RestController.class
//                : classController.getClassType().getValue().equals("Entity") ? Entity.class
//                : Component.class;
//            builder.annotateType(AnnotationDescription.Builder
//                .ofType(classType) // don't use `request` mapping here
//                .build());
//        }
//        builder.make()
//            .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
//            .getLoaded()
//            .newInstance();
//
//        return builder;
//    }
//
    public Object generateUserController(FlowResponse flowResponse) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        Class<? extends Annotation> classType = RestController.class;
//        classType = classController.getClassType().getValue().equals("RestController") ? RestController.class : Component.class;


        Object UserController = new ByteBuddy()
            .subclass(Object.class)
            .name(flowResponse.getName())
            .annotateType(AnnotationDescription.Builder
                .ofType(classType) // don't use `request` mapping here
                .build())

            .defineMethod("mainMethod", ResponseEntity.class, Modifier.PUBLIC)
            .withParameter(HttpServletRequest.class, "requestUrl")

            .withParameter(Map.class, "param")
            .annotateParameter(AnnotationDescription.Builder
                .ofType(RequestParam.class)
//                .define("name", "param")
                .build())
            .annotateParameter(AnnotationDescription.Builder.ofType(Nullable.class).build())

            .withParameter(Map.class, "body")
            .annotateParameter(AnnotationDescription.Builder
                .ofType(RequestBody.class)
                .build())
            .annotateParameter(AnnotationDescription.Builder.ofType(Nullable.class).build())

            .intercept(MethodDelegation.to(ApiImplementation.class))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded()
            .newInstance();
        return UserController;
    }
}
