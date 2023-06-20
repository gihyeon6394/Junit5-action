import java.time.LocalDate;

public class Idol {


    private final String memberName;
    private final int age;

    private int isLeader = 0;

    private LocalDate birthDate;

    public static class Builder {
        private final String memberName;
        private final int age;
        private int isLeader = 0;

        private LocalDate birthDate;

        public Builder(String memberName, int age) {
            this.memberName = memberName;
            this.age = age;
        }

        public Builder isLeader(int isLeader) {
            this.isLeader = isLeader;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Idol build() {
            return new Idol(this);
        }

    }

    public Idol(Builder builder) {
        this.memberName = builder.memberName;
        this.age = builder.age;
        this.isLeader = builder.isLeader;
        this.birthDate = builder.birthDate;

    }

    public String getMemberName() {
        return memberName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Idol{" +
                "memberName='" + memberName + '\'' +
                ", age=" + age +
                ", isLeader=" + isLeader +
                ", birthDate=" + birthDate +
                '}';
    }
}
