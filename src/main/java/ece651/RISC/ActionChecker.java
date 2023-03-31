package ece651.RISC;

public abstract class ActionChecker<T> {
    private final ActionChecker next;
    public ActionChecker(ActionChecker<T> next){
        this.next = next;
    }
    //Subclasses will override this method to specify how they check their own rule.
    protected abstract String checkMyRule(Territory sourceTerritory, Territory targetTerritory, int Units);

    //use tail recursion, but the "this" is changing with each recursive call
    //Subclasses will generally NOT override this method (none of them should)
    public String checkAction(Territory sourceTerritory, Territory targetTerritory, int Units){
        //if we fail our own rule: stop the action which is illegal
        if(checkMyRule(sourceTerritory, targetTerritory, Units) != null){
            return checkMyRule(sourceTerritory, targetTerritory, Units);
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkAction(sourceTerritory, targetTerritory, Units);
        }
        //if there are no more rules, then the action is legal
        return null;
    }

}


