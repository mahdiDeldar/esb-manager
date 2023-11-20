package org.sohagroup.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "flow")
public class Flow {
//    private static final String SEQUENCE_NAME = "FLOW_SEQ";
    @Id
//    @GeneratedValue(generator = SEQUENCE_NAME)
//    @GenericGenerator(name = SEQUENCE_NAME, strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
//        parameters = {
//            @org.hibernate.annotations.Parameter(name = "sequence_name", value = SEQUENCE_NAME),
//            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
//            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
//        }
//    )
    private long id;
    @Column(name = "name",unique=true)
    @NotNull
    private String name;
    @NotNull
    @Column(name = "end_point", unique=true)
    private String endPoint;
    @NotNull
    @Column(name = "method")
    private String method;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
