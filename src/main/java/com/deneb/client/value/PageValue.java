package com.deneb.client.value;

import com.deneb.client.utils.LambdaUtil;

import java.util.function.Predicate;

public class PageValue {
    private final MValue value;
    public PageValue(MValue value){
        this.value = value;
    }
    public MValue getValue(){
        return value;
    }
    public static class Page {
        private final MValue value;
        private final String page;
        private final LambdaUtil.Bool p;

        public Page(MValue value, String page) {
            this.page = page;
            this.value = value;
            this.p = new LambdaUtil.Bool().set(p -> value.getToggledMode().getName().equals(page));
        }

        public Page(MValue value, int index) {
            this.page = value.getModes().get(index - 1).getName();
            this.value = value;
            this.p = new LambdaUtil.Bool().set(p -> value.getToggledMode().getName().equals(page));
        }

        public boolean b() {
            return p.get();
        }

        public Predicate<Object> p() {
            return p.p();
        }

        public MValue getPage() {
            return value;
        }
    }
}
