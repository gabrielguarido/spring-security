package com.springsecurity.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Map the 'permission' table in the database.
 *
 * @author Gabriel Oliveira
 */
@Entity
@Table(name = "permission", uniqueConstraints = {@UniqueConstraint(columnNames = {"description"})})
public class Permission {

    @Id
    @Column(name = "permission_id")
    private Integer id;

    @NotEmpty
    @Size(max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Set<Profile> profiles = new HashSet<>();

    @PreRemove
    private void removePermissions() {
        for (Profile profile : profiles) {
            profile.getPermissions().remove(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", profiles=" + profiles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return getId().equals(that.getId()) &&
                getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }

}
