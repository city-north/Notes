package vip.ericchen.study.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * description
 *
 * @author EricChen 2020/01/01 21:03
 */
@Data
@Builder
@Entity(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    public Long id;

    @Column(name = "user_name",unique = true)
    private String userName;

    private String password;

    private String role;

}
