package cn.eccto.study.java.basic.serializable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/06/12 09:35
 */
public class ReferenceDto implements Externalizable {

    private String name;
    private int age;

    public ReferenceDto() {
    }

    public ReferenceDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public ReferenceDto setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public ReferenceDto setAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(age);
        out.writeChars(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.age = in.readInt();
        this.name  = in.readLine();
    }
}
