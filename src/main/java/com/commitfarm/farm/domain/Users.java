package com.commitfarm.farm.domain;

import com.commitfarm.farm.domain.enumClass.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    private String email;


    private boolean isAdmin;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Member> members;

    @JsonIgnore
    @OneToMany(mappedBy = "reporter")
    private List<Ticket> reportedTickets;


    @JsonIgnore
    @OneToMany(mappedBy = "developer")
    private List<Ticket> assignedTickets;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

}