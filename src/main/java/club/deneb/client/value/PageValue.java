package club.deneb.client.value;

import club.deneb.client.utils.LambdaUtil;

import java.util.function.Predicate;

public class PageValue {
    private final ModeValue value;
    public PageValue(ModeValue value){
        this.value = value;
    }
    public ModeValue getValue(){
        return value;
    }
    public static class Page {
        private final ModeValue value;
        private final String page;
        private final LambdaUtil.Bool p;

        public Page(ModeValue value, String page) {
            this.page = page;
            this.value = value;
            this.p = new LambdaUtil.Bool().set(p -> value.getToggledMode().getName().equals(page));
        }

        public Page(ModeValue value, int index) {
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

        public ModeValue getPage() {
            return value;
        }
    }
}
