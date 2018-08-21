package com.eric.designpattern.StructuralPatterns.BP;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public abstract class Abstraction {
    private Implementor implementor;

    public Implementor getImplementor() {
        return implementor;
    }

    public void setImplementor(Implementor implementor) {
        this.implementor = implementor;
    }

    protected void operation(){
        implementor.operation();
    }
}
