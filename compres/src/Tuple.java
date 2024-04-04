public class Tuple implements Comparable<Tuple> {
    Integer originalIndex;  // хранит оригинальный индекс суффикса
    Integer firstHalf;      // хранит ранг первой половины суффикса
    Integer secondHalf;     // хранит ранг второй половины суффикса


    @Override
    public int compareTo(Tuple tuple) {
        return this.firstHalf.equals(tuple.firstHalf)
                ? this.secondHalf.compareTo(tuple.secondHalf)
                : this.firstHalf.compareTo(tuple.firstHalf);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "originalIndex=" + originalIndex +
                ", firstHalf=" + firstHalf +
                ", secondHalf=" + secondHalf +
                '}';
    }
}