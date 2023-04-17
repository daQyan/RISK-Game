package ece651.RISC.shared;

public class Unit {
    private int myType;
    public Unit() {
        this.myType = 0;
    }
    public Unit(int Type) {
        this.myType = Type;
    }

    public void upgradeUnit(int resource){
        //for checker:
        //use checker to check if the resource is enough to upgrade
        //check if upgraded unit has higher precedence than the territory does
        //check if upgraded unit will have precedence larger than 6(0 at first)

    }
    public Boolean equals(Unit rhs){
        return this.myType == rhs.myType;
    }
}
