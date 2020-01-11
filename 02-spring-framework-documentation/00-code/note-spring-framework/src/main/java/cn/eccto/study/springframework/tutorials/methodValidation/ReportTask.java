package cn.eccto.study.springframework.tutorials.methodValidation;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * description
 *
 * @author EricChen 2019/11/22 22:56
 */
@Validated
@Component
public class ReportTask {
    public @Pattern(regexp = "[0-3]")
    String createReport(@NotNull @Size(min = 3, max = 20) String name,
                        @NotNull @FutureOrPresent LocalDateTime startDate) {
        return "-1";
    }
}