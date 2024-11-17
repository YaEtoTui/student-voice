package ru.urfu.sv.studentvoice.model.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@Getter
@ToString
public class Professor {
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID professorId;

//    @Column(name = "username", nullable = false)
    private String username;

//    @Column(name = "full_name", nullable = false)
    @Setter
    private String fullName;

    public Professor(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }
}
