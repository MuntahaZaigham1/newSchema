package com.fastcode.example.domain.core.post;

import javax.persistence.*;
import java.time.*;
import com.fastcode.example.domain.core.writer.Writer;
import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.querydsl.core.annotations.Config;
import org.hibernate.annotations.TypeDefs;


@Entity
@Config(defaultVariableName = "postEntity")
@Table(name = "post")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({
}) 
public class Post extends AbstractEntity {

    @Basic
    @Column(name = "body", nullable = false)
    private String body;

    @Id
    @EqualsAndHashCode.Include() 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Basic
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Writer writer;


}



