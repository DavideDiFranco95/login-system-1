package co.develhope.loginSystem1.users.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES",
            inverseJoinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            joinColumns = {
                    @JoinColumn(name = "ROLE_ID")})
    private Set<User> users;
}
