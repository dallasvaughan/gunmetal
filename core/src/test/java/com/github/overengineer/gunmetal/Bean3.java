package com.github.overengineer.gunmetal;

import com.github.overengineer.gunmetal.metadata.Inject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reesbyars
 * Date: 5/12/13
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class Bean3 implements IBean {

    @Inject
    public void setAspects(List<Integer> integers) {
        System.out.println(integers);
    }

    @Inject
    public void setIBean2(IBean2 iBean2) {
        System.out.println(iBean2);
    }

    @Override
    public void stuff() {
        //
    }
}
