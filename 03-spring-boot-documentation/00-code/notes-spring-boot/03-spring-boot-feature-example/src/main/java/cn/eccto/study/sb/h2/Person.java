package cn.eccto.study.sb.h2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description
 *
 * @author EricChen 2019/12/12 11:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private long id;
    private String firstName;
    private String lastName;
    private String address;
}
