package com.fastcode.example.domain.core.writer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.time.*;
import com.fastcode.example.domain.core.post.Post;
import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.querydsl.core.annotations.Config;
import org.hibernate.annotations.TypeDefs;


@Entity
@Config(defaultVariableName = "writerEntity")
@Table(name = "writer")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({
}) 
public class Writer extends AbstractEntity {

    @Basic
    @Column(name = "email", nullable = false,length =45)
    private String email;

    @Basic
    @Column(name = "first_name", nullable = false,length =45)
    private String firstName;

    @Id
    @EqualsAndHashCode.Include() 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Basic
    @Column(name = "last_name", nullable = false,length =45)
    private String lastName;

    @Basic
    @Column(name = "username", nullable = true,length =50)
    private String username;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> postsSet = new HashSet<Post>();
    
    public void addPosts(Post posts) {        
    	postsSet.add(posts);

        posts.setWriter(this);
    }
    public void removePosts(Post posts) {
        postsSet.remove(posts);
        
        posts.setWriter(null);
    }
    

}



