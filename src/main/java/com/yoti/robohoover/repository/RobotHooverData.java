package com.yoti.robohoover.repository;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "robothoover_request_response")
public class RobotHooverData implements Serializable {
    private static final long serialVersionUID = -32111223344L;
    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    public RobotHooverData(String request, String response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public String toString() {
        return "RobotHooverData{" +
                "id=" + id +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
