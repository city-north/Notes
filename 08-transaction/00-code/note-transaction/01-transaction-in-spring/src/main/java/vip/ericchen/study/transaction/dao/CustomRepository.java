package vip.ericchen.study.transaction.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vip.ericchen.study.transaction.domain.Customer;

/**
 * description
 *
 * @author EricChen 2020/01/01 21:05
 */
public interface CustomRepository extends JpaRepository<Customer,Long> {

}
