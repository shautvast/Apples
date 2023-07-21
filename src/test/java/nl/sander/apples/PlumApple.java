package nl.sander.apples;

public class PlumApple extends BaseApple<Plum> {
    public Result compare(Plum left, Plum right) {
        Result core = Apples.compare(left.core(), right.core());
        Result peel = Apples.compare(left.peel(), right.peel());
        Result juicy = Apples.compare(left.juicy(), right.juicy());
        Result price = Apples.compare(left.price(), right.price());
        Result number = Apples.compare(left.number(), right.number());
        Result storage = Apples.compare(left.storage(), right.storage());

        return Result.merge(core, peel, juicy, price, number, storage);
    }
}
