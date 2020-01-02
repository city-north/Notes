package vip.ericchen.study.transaction;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import vip.ericchen.study.transaction.domain.Customer;
import vip.ericchen.study.transaction.service.CustomerServiceInAnno;
import vip.ericchen.study.transaction.service.CustomerServiceInCode;

import java.util.List;

@SpringBootApplication
public class SpringTransactionApplication {


    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringTransactionApplication.class)
                .web(WebApplicationType.NONE);
        ConfigurableApplicationContext context = builder.run();
        tryTransactionInCode(context);
//		tryTransactionInAnno(context);

    }

    private static void tryTransactionInCode(ConfigurableApplicationContext context) {
        CustomerServiceInCode customerServiceInCode = context.getBean(CustomerServiceInCode.class);
        Customer build = Customer.builder()
//                .id(1L)
                .password("!@3")
                .role("admin")
                .userName("eric")
                .build();
        customerServiceInCode.save(build);
		Customer build2 = Customer.builder()
//                .id(1L)
				.password("!@3")
				.role("admin")
				.userName("eric")
				.build();
        customerServiceInCode.save(build2);
        List<Customer> all = customerServiceInCode.findAll();
        System.out.println(all);
    }

	private static void tryTransactionInAnno(ConfigurableApplicationContext context) {
		CustomerServiceInAnno customerServiceInCode = context.getBean(CustomerServiceInAnno.class);
		Customer build = Customer.builder()
				.id(1L)
				.password("!@3")
				.role("admin")
				.userName("eric")
				.build();
		customerServiceInCode.save(build);
		List<Customer> all = customerServiceInCode.findAll();
		System.out.println(all);
	}
}
