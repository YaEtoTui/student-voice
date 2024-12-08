package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "links")
public class Link extends AbstractEntity {

    @Column(name = "course_id", unique = true, nullable = false)
    private Long courseId;

    @Column(name = "path_link", unique = true, nullable = false)
    private String pathLink;

    @Column(name = "is_constantly")
    private Boolean isConstantly;
}