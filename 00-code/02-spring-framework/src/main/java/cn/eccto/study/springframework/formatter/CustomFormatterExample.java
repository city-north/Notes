package cn.eccto.study.springframework.formatter;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.Formatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.text.ParseException;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * 自定义{@link Formatter} 实例
 * <p>
 * 1. 自定义实体类 {@link Employee}
 * 2. 自定义实体类格式化器 {@link EmployeeFormatter}
 * 3. 自定义 {@link ConversionService} 的默认实现类 {@link DefaultFormattingConversionService}
 * 4. 使用自定义实现类添加自定义的格式化器进行格式化
 *
 * @author EricChen 2019/11/04 15:16
 */
public class CustomFormatterExample {
    public static void main(String[] args) {
        DefaultFormattingConversionService service =
                new DefaultFormattingConversionService();

        service.addFormatter(new EmployeeFormatter());

        Employee employee = new Employee("Joe", "IT", "123-456-7890");
        String string = service.convert(employee, String.class);
        System.out.println(string);

        Employee e = service.convert(string, Employee.class);
        System.out.println(e);

    }

    /**
     * 新建一个 Formatter 并实现 Formatter 接口
     */
    private static class EmployeeFormatter implements Formatter<Employee> {

        /**
         * 将字符串解析成为一个 {@link Employee}
         *
         * @param text   要解析的字符串
         * @param locale 本地对象
         * @return
         * @throws ParseException
         */
        @Override
        public Employee parse(String text,
                              Locale locale) throws ParseException {

            String[] split = text.split(",");
            if (split.length != 3) {
                throw new ParseException("The Employee string format " +
                        "should be in this format: Mike, Account, 111-111-1111",
                        split.length);
            }
            Employee employee = new Employee(split[0].trim(),
                    split[1].trim(), split[2].trim());
            return employee;
        }

        @Override
        public String print(Employee employee, Locale locale) {
            return new StringJoiner(", ")
                    .add(employee.getName())
                    .add(employee.getDept())
                    .add(employee.getPhoneNumber())
                    .toString();

        }
    }

    private static class Employee {
        private String name;
        private String dept;
        private String phoneNumber;

        public Employee(String name, String dept, String phoneNumber) {
            this.name = name;
            this.dept = dept;
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public String getDept() {
            return dept;
        }


        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
}