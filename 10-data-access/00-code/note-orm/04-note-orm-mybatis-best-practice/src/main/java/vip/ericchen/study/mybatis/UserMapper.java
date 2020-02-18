package vip.ericchen.study.mybatis;

import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/18 19:51
 */
interface UserMapper {

    User selectUser(@Param("id") int id);
}
