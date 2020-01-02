package vip.ericchen.study.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.ericchen.study.transaction.dao.CustomRepository;
import vip.ericchen.study.transaction.domain.Customer;

import java.util.List;

/**
 * Spring 事务代码手动执行方式
 *
 * @author EricChen 2020/01/01 18:38
 */
@Service
public class CustomerServiceInAnno {

    @Autowired
    private CustomRepository customRepository;

    @Transactional
    public void save(Customer customer) {
        customRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customRepository.findAll();
    }
}
