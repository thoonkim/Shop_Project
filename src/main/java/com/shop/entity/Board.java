package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "s_board")
@Getter
@Setter
@ToString
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id")
    private Long b_id;

    @Column(name = "b_writer", nullable = false, length = 100)
    private String b_writer;

    @Column(name = "b_title", nullable = false, length = 100)
    private String b_title;

    @Column(name = "b_contents", nullable = false, length = 512)
    private String b_contents;

    @CreationTimestamp
    @Column(name = "b_created_date")
    private LocalDateTime b_created_date;

    @UpdateTimestamp
    @Column(name = "b_update_date")
    private LocalDateTime b_update_date;



}
