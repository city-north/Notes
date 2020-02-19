package vip.ericchen.study.mybatis.entity;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/18 19:53
 */
@Data
public class User {
    private Integer id;
    private String name;
    private Integer age;

    private List<Dept> deptList;
}
