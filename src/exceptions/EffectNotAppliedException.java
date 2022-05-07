package exceptions;

// this only exists because only duration stacks, remove if this is actually wrong
// (idk if we're even allowed to add more exceptions)
public class EffectNotAppliedException extends GameActionException{
    public EffectNotAppliedException(){
        super();
    }
    public EffectNotAppliedException(String s){
        super(s);
    }
}
