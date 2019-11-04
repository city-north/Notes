package cn.eccto.study.springframework.formatter.info;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;

/**
 * description
 *
 * @author EricChen 2019/11/04 15:31
 */
public class Order{
    @NumberFormat(style = NumberFormat.Style.PERCENT)
    private Double price;

    @DateTimeFormat(pattern = "yyyy")
    private Date date;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
