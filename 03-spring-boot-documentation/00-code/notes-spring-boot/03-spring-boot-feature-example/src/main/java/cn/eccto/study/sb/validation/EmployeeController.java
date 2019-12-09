package cn.eccto.study.sb.validation;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author EricChen 2019/12/09 23:45
 */
@Controller
public class EmployeeController {
    private static List<Employee> employeeList = new ArrayList<>();

    @PostMapping("/")
    @ResponseBody
    public Object handlePostRequest(@Valid Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return allErrors;
        }
        employeeList.add(employee);
        return "success";
    }
}
