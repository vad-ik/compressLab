public class BWTtranspositionObject implements Comparable<BWTtranspositionObject> {
    Integer num;
    Character character;
    public BWTtranspositionObject(Integer num, Character character) {
        this.num = num;
        this.character = character;
    }
    public Integer getNum() {
        return num;
    }
    public Character getCharacter() {
        return character;
    }
    @Override
    public int compareTo(BWTtranspositionObject bwtObject) {
        return (((int)this.character) - ((int)bwtObject.getCharacter()));
    }
    @Override
    public String toString() {
        return  num +" " + character;
    }
}
