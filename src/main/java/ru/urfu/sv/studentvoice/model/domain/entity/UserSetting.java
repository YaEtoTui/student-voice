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
@Table(name = "user_setting")
public class UserSetting extends AbstractEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "settings_id", nullable = false)
    private Long settingsId;

    @Column(name = "available", nullable = false)
    private boolean available;
}