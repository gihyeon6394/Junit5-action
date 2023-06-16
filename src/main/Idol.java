public class Idol {


    private final String memberName;
    private final int age;

    public static class Builder {
        private final String memberName;
        private final int age;
        private int isLeader = 0;

        public Builder(String memberName, int age) {
            this.memberName = memberName;
            this.age = age;
        }

        public Builder isLeader(int isLeader) {
            this.isLeader = isLeader;
            return this;
        }

        public Idol build() {
            return new Idol(this);
        }
    }

    public Idol(Builder builder) {
        this.memberName = builder.memberName;
        this.age = builder.age;

    }

    public String getMemberName() {
        return memberName;
    }

    public int getAge() {
        return age;
    }
}
