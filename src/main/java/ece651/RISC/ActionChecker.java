package ece651.RISC;

public abstract class ActionChecker<T> {
    //Subclasses will override this method to specify how they check their own rule.
    protected abstract String checkMyRule(Territory sourceTerritory, Territory targetTerritory, int Units);

}


