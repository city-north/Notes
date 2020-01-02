package vip.ericchen.study.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import vip.ericchen.study.transaction.dao.CustomRepository;
import vip.ericchen.study.transaction.domain.Customer;

import java.util.List;

/**
 * Spring 事务代码手动执行方式,不使用注解的情况下回滚
 *
 * @author EricChen 2020/01/01 18:38
 */
@Service
public class CustomerServiceInCode {

    @Autowired
    private PlatformTransactionManager platformtransactionManager;
    @Autowired
    private CustomRepository customRepository;

    public void save(Customer customer) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = platformtransactionManager.getTransaction(transactionDefinition);
        try {
            customRepository.save(customer);
            platformtransactionManager.commit(transaction);
        } catch (Exception e) {
            if (!transaction.isCompleted()){
                platformtransactionManager.rollback(transaction);
            }
        }

    }

    public List<Customer> findAll(){
        return customRepository.findAll();
    }
}
