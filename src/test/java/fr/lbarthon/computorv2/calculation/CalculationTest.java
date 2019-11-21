package fr.lbarthon.computorv2.calculation;

import fr.lbarthon.computorv2.variables.Complex;
import org.junit.jupiter.api.Test;

class CalculationTest {

    @Test
    void complexAddition() {
        Complex n = new Complex(0D);
        Complex nbr1 = new Complex(5D, 0D);
        Complex nbr2 = new Complex(15D, 0D);
        Complex cplx1 = new Complex(0D, 12D);
        Complex cplx2 = new Complex(0D, 125D);
        Complex fat = new Complex(114D, 163D);
        n.add(nbr1);
        assert n.getReal().equals(5D);
        n.add(nbr2);
        assert n.getReal().equals(20D);
        n.add(cplx1);
        assert n.getImg().equals(12D);
        n.add(cplx2);
        assert n.getImg().equals(137D);
        n.add(fat);
        assert n.getReal().equals(134D);
        assert n.getImg().equals(300D);
    }

    @Test
    void complexSubstraction() {
        Complex n = new Complex(10D, 24D);
        Complex nbr1 = new Complex(5D, 0D);
        Complex nbr2 = new Complex(15D, 0D);
        Complex cplx1 = new Complex(0D, 12D);
        Complex cplx2 = new Complex(0D, 125D);
        Complex fat = new Complex(114D, 163D);
        n.sub(nbr1);
        assert n.getReal().equals(5D);
        n.sub(nbr2);
        assert n.getReal().equals(-10D);
        n.sub(cplx1);
        assert n.getImg().equals(12D);
        n.sub(cplx2);
        assert n.getImg().equals(-113D);
        n.sub(fat);
        assert n.getReal().equals(-124D);
        assert n.getImg().equals(-276D);
    }

    @Test
    void complexMult() {
        Complex n = new Complex(10D, 24D);
        Complex nbr1 = new Complex(5D, 0D);
        Complex nbr2 = new Complex(15D, 0D);
        Complex cplx1 = new Complex(0D, 3D);
        Complex cplx2 = new Complex(0D, 12D);
        Complex fat = new Complex(14D, 13D);
        n.mult(nbr1);
        assert n.getReal().equals(50D);
        assert n.getImg().equals(120D);
        n.mult(nbr2);
        assert n.getReal().equals(750D);
        assert n.getImg().equals(1800D);
        n.mult(cplx1);
        assert n.getReal().equals(-5400D);
        assert n.getImg().equals(2250D);
        n.mult(cplx2);
        assert n.getReal().equals(-27000D);
        assert n.getImg().equals(64800D);
        n.mult(fat);
        assert n.getReal().equals(464400D);
        assert n.getImg().equals(1258200D);
    }
}
