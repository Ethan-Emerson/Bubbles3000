
public class Push extends Feature {
    double radius;
    double amount;
    boolean on;

    public Push(double radius, double amount, boolean on)
    {
        this.radius = radius;
        this.amount = amount;
        this.on = on;
    }

    public void doThing()
    {
        //Do the thing
    }

    public double getRadius()
    {
        return radius;
    }

    public boolean getOn()
    {
        return on;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setOn(boolean on){
        this.on = on;
    }

}