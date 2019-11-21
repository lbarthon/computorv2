package fr.lbarthon.computorv2.validator;

import fr.lbarthon.computorv2.Validator;
import fr.lbarthon.computorv2.exceptions.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class ValidatorTest {

    @Test
    void bracketsValidTest() {
        String[] valid = new String[]{
                "(( salut ))",
                "",
                "()",
                "         (           )            ",
                "(((((((())))))))"
        };

        for (String str : valid) {
            try {
                new Validator(str).brackets();
            } catch (ParseException e) {
                fail("Brackets validation shouldn't fail");
            }

            str = str.replaceAll("\\(", "[")
                    .replaceAll("\\)", "]");

            try {
                new Validator(str).brackets('[', ']');
            } catch (ParseException e) {
                fail("Brackets validation shouldn't fail");
            }
        }
    }

    @Test
    void bracketsInvalidTest() {
        String[] invalid = new String[]{
                "(( salut )))",
                "(       )      (",
                "() () ()  ( () ()",
                ") () (",
                "   (     )      (     ) )"
        };

        for (String str : invalid) {
            try {
                new Validator(str).brackets();
                fail("Brackets validation should fail");
            } catch (ParseException e) {}


            str = str.replaceAll("\\(", "[")
                    .replaceAll("\\)", "]");

            try {
                new Validator(str).brackets('[', ']');
                fail("Brackets validation should fail");
            } catch (ParseException e) {}
        }
    }

    @Test
    void matricesValidTest() {
        String[] valid = new String[] {
                "[[1,2];[3,4]]",
                "[[1,2];[3,4];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6]]",
                "[[1]]",
                "[[1];[2];[3];[4]]",
                "[[1,2,3,4,5,6,7,8,9,10]]"
        };

        for (String str : valid) {
            try {
                new Validator(str)
                        .brackets('[', ']')
                        .matrix();
            } catch (ParseException e) {
                fail("Matrix validation shouldn't fail");
            }
        }
    }

    @Test
    void matricesInvalidTest() {
        String[] invalid = new String[] {
                "[[1,2];[3,4][]]",
                "[[1,2];[3,4];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6];[5,6]]",
                "[[]]"
        };

        for (String str : invalid) {
            try {
                new Validator(str)
                        .brackets('[', ']')
                        .matrix();
                fail("Matrix validation should fail");
            } catch (ParseException e) {}
        }
    }
}
