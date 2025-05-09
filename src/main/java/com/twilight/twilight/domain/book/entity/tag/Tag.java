package com.twilight.twilight.domain.book.entity.tag;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Tag {

    @Id
    @Column(name = "tag_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag_type", nullable = false)
    private TagType tagType;

    public enum TagType {
        CATEGORY, // 대분류
        THEME,    // 중분류
        EMOTION   // 감성 태그
    }

}
