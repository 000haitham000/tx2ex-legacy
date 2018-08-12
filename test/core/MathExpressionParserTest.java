/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import exceptions.InvalidFormatException;
import exceptions.MisplacedTokensException;
import exceptions.TooManyDecimalPointsException;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import types.AbstractNode;
import types.AdditionBinaryOpNode;
import types.LiteralNode;
import types.MultiplicationBinaryOpNode;
import types.PowerNode;
import types.ProductNode;
import types.SummationNode;
import types.VariableNode;
import types.VectorElementNode;

/**
 *
 * @author seadahai
 */
public class MathExpressionParserTest {

    // The variables manager used throughout all the tests
    private static VariablesManager vm;

    @BeforeClass
    public static void setUpClass() {
        // Initialize the variable manager
        vm = new VariablesManager();
    }

    /**
     * Test of parse method, of class MathExpressionParser.
     */
    @Test
    public void testParse()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        System.out.println("Testing MathExpressionParser:parse()");
        double epsilon = 1e-10;
        // Test Case 1
        System.out.println("\tTest Case: 1");
        assertEquals(-67, MathExpressionParser.parse("20 * -+5 + 33", vm)
                .evaluate(), epsilon);
        // Test Case 2
        System.out.println("\tTest Case: 2");
        assertEquals(-67, MathExpressionParser.parse("20*-+5++33", vm)
                .evaluate(), epsilon);
        // Test Case 3
        System.out.println("\tTest Case: 3");
        assertEquals(21, MathExpressionParser.parse("+20-+-1", vm)
                .evaluate(), epsilon);
        // Test Case 4
        System.out.println("\tTest Case: 4");
        assertEquals(-90, MathExpressionParser.parse("((10*+-9))", vm)
                .evaluate(), epsilon);
        // Test Case 5
        System.out.println("\tTest Case: 5");
        assertEquals(155.70490196078433, MathExpressionParser.parse(
                "23/10.2 + 10.23*15", vm).evaluate(), epsilon);
        // Test Case 6
        System.out.println("\tTest Case: 6");
        assertEquals(0.2027100355086725, MathExpressionParser.parse(
                "tan(0.2)", vm).evaluate(), epsilon);
        // Test Case 7
        System.out.println("\tTest Case: 7");
        vm.setVector("x_$11", new double[]{10, 20, 30, 40, 50});
        // Notice that counting starts from 1
        assertEquals(90, MathExpressionParser.parse(
                "sum{k,2,4,x_$11[k]}", vm).evaluate(), epsilon);
        // Test Case 8
        System.out.println("\tTest Case: 8");
        vm.setVector("x_$11", new double[]{10, 20, 30, 40, 50});
        vm.set("_asd", 10.2);
        vm.set("i", 5);
        assertEquals(95.43693098384728, MathExpressionParser.parse(
                "23/(_asd + 10.23)*15 -  10/88*99-x_$11[i*2-9]/50 "
                + "+ sum{k,2,4,x_$11[k]+100*  tan(0.2)"
                + "-100*sin(0.2)/cos(0.2)}", vm).evaluate(), epsilon);
        // Test Case 9
        System.out.println("\tTest Case: 9");
        vm.set("i", 5);
        vm.setVector("x_$11", new double[]{10, 20, 30, 40, 50});
        assertEquals(0, MathExpressionParser.parse(
                "  1.02- x_$11[5]* prod{i,10 ,12.0,i+1} + 4+85794.98", vm)
                .evaluate(), epsilon);
        // Test Case 10
        System.out.println("\tTest Case: 10");
        // The number of odd integers between 1 and 5 inclusive.
        assertEquals(3, MathExpressionParser.parse(
                "Sum{ii, 1, 5, ii%  2}", vm)
                .evaluate(), epsilon);
        // Test Case 11
        System.out.println("\tTest Case: 11");
        assertEquals(1.0, MathExpressionParser.parse(
                "cos(acos(sin(1.5707963267948966)))", vm).evaluate(), epsilon);
        // Test Case 12
        System.out.println("\tTest Case: 12");
        assertEquals(-2, MathExpressionParser.parse(
                "-   --   2.0", vm).evaluate(), epsilon);
        // Test Case 13
        System.out.println("\tTest Case: 13");
        assertEquals(4, MathExpressionParser.parse(
                "sqrt(16.0)", vm).evaluate(), epsilon);
        // Test Case 14
        System.out.println("\tTest Case: 14");
        vm.setConstant("PI", Math.PI);
        vm.set("var", 5);
        assertEquals(31.41592653589793, MathExpressionParser.parse(
                "2 * var * PI", vm).evaluate(), epsilon);
        // Test Case 15
        System.out.println("\tTest Case: 15");
        vm.setVector("x", new double[]{10, 20, 30, 40, 50, 60, 70, 80, 90});
        vm.setConstant("pi", Math.PI);
        assertEquals(3.0193512E10, MathExpressionParser.parse(
                "0.5 * x[2] * x[3] * x[4] * (1 + 100 * (5 + sum{i,5,9, (x[i] - "
                + "0.5)^2 - cos(2 * pi * (x[i] - 0.5))}))", vm)
                .evaluate(), epsilon);
        // Test Case 16
        System.out.println("\tTest Case: 16");
        double[] x = {0.00464678000485, 0.27587023044768, 0.71549533066038,
            0.91117851440468, 0.55985687416083, 0.53741305862749,
            0.34872737808561, 0.93529514059711, 0.50314421329251,
            0.41581614753265, 0.86181838164073, 0.33368738365047,
            0.40812915550712, 0.24947594908016};
        vm.setVector("x", x);
        vm.setConstant("pi", Math.PI);
        assertEquals(-0.00647609305893118, MathExpressionParser.parse(
                "-pi/2 * sin(x[1] * pi / 2) * cos(x[2] * pi / 2) * cos(x[3] * "
                + "pi / 2) * sin(x[4] * pi / 2) * (1 + sum{i,5,14, "
                + "(x[i] - 0.5)^2})", vm).evaluate(), epsilon);
        // Test Case 17
        System.out.println("\tTest Case: 17");
        vm.set("var", -50.2);
        vm.set("my_var", -100);
        vm.set("happy_var", 88);
        vm.set("sad_var", -100.002);
        vm.set("cool_var", 2);
        assertEquals(-176, MathExpressionParser.parse(
                "min(var,my_var,  happy_var ,-happy_var*cool_var,  "
                + "sad_var, cool_var)", vm).evaluate(), epsilon);
        // Test Case 18
        System.out.println("\tTest Case: 18");
        vm.setVector("x", new double[]{100.5, -66.5, -200, 30.03});
        vm.set("y", -100);
        assertEquals(-266.5, MathExpressionParser.parse(
                "min(10,sum{i,2,3, x[i]},  y)", vm).evaluate(), epsilon);
        // Test Case 19
        System.out.println("\tTest Case: 19");
        vm.setVector("x", new double[]{0.000, 2.2, 0.5, -1});
        vm.set("y", 3);
        assertEquals(1.1, MathExpressionParser.parse(
                "min(10,prod{  i,2 , 3, x[i]},  y)", vm).evaluate(), epsilon);
        // Test Case 20
        System.out.println("\tTest Case: 20");
        vm.setVector("x", new double[]{10, 1, 1000, 100});
        assertEquals(102, MathExpressionParser.parse(
                "sum{i,1,3,min(x[i],x[i+1])}", vm).evaluate(), epsilon);
        // Test Case 21
        System.out.println("\tTest Case: 21");
        vm.setVector("x", new double[]{10, 5, 1000, 100});
        assertEquals(25, MathExpressionParser.parse(
                "prod{i,1,2,min(x[i],x[i+1],  x[i+2])}", vm).evaluate(), epsilon);
        // Test Case 22
        System.out.println("\tTest Case: 22");
        vm.set("var", -50.2);
        vm.set("my_var", -100);
        vm.set("happy_var", 88);
        vm.set("sad_var", -100.002);
        vm.set("cool_var", 2);
        assertEquals(176, MathExpressionParser.parse(
                "max(var,my_var,  happy_var ,happy_var*cool_var,  "
                + "sad_var, cool_var)", vm).evaluate(), epsilon);
        // Test Case 23
        System.out.println("\tTest Case: 23");
        vm.setVector("x", new double[]{100.5, 66.5, 200, 30.03});
        vm.set("y", -100);
        assertEquals(266.5, MathExpressionParser.parse(
                "max(10,sum{i,2,3, x[i]},  y)", vm).evaluate(), epsilon);
        // Test Case 24
        System.out.println("\tTest Case: 24");
        vm.setVector("x", new double[]{0.000, 2.2, 0.5, -1});
        vm.set("y", -3);
        assertEquals(1.1, MathExpressionParser.parse(
                "max(0.1,prod{  i,2 , 3, x[i]},  y)", vm).evaluate(), epsilon);
        // Test Case 25
        System.out.println("\tTest Case: 25");
        vm.setVector("x", new double[]{10, 1, 1000, 100});
        assertEquals(2010, MathExpressionParser.parse(
                "sum{i, 1, 3, max(x[i],x[i+1])}", vm).evaluate(), epsilon);
        // Test Case 26
        System.out.println("\tTest Case: 26");
        vm.setVector("x", new double[]{10, 5, 1000, 100});
        assertEquals(1000000, MathExpressionParser.parse(
                "prod{i,1,2,max(x[i],x[i+1],  x[i+2])}", vm).evaluate(), epsilon);
        // Test Case 27 (testing for inline variable definitions)
        System.out.println("\tTest Case: 27");
        vm.set("r", 5);
        vm.set("h", 7);
        assertEquals(298.3, MathExpressionParser.parse(
                "2*(pi=3.14)*r*h + pi*r^2", vm).evaluate(), epsilon);
        // Test Case 28
        System.out.println("\tTest Case: 28");
        assertEquals(11.119213641734133, MathExpressionParser.parse(
                "abs(tan(2.3) - 10)", vm).evaluate(), epsilon);
        // Test Case 29
        System.out.println("\tTest Case: 29");
        vm.set("A", .8);
        vm.set("B", .75);
        vm.set("C", .85);
        vm.setConstant("PI", Math.PI);
        vm.setVector("z", new double[]{1, 2, 3, 4});
        assertEquals(3.043851484882136, MathExpressionParser.parse(
                "(1.0/14.0)*(6 * (A + (min(0.0, floor((abs(z[3] - 0.35) /"
                + " abs(floor(0.35 - z[3]) + 0.35)) - B)) * A *"
                + " (B - (abs(z[3] - 0.35) / abs(floor(0.35 - z[3]) +"
                + " 0.35))) / B) - min(0.0, floor(C - (abs(z[3] - 0.35)"
                + " / abs(floor(0.35 - z[3]) + 0.35)))) * (1.0 - A) *"
                + " ((abs(z[3] - 0.35) / abs(floor(0.35 - z[3]) + 0.35))"
                + " - C) / (1.0 - C))^2+ 8 * (A + (min(0.0,"
                + " floor((abs(z[4] - 0.35) / abs(floor(0.35"
                + " - z[4]) + 0.35)) - B)) * A * (B - (abs(z[4] - 0.35)"
                + " / abs(floor(0.35 - z[4]) + 0.35))) / B) - min(0.0,"
                + " floor(C - (abs(z[4] - 0.35) / abs(floor(0.35 - z[4])"
                + " + 0.35)))) * (1.0 - A) * ((abs(z[4] - 0.35) /"
                + " abs(floor(0.35 - z[4]) + 0.35)) - C) /"
                + " (1.0 - C))^0.02)+ 2 *(1 - cos((z[1]^0.02)*PI /2))"
                + "*(1 - cos((z[2]^0.02)*PI /2))", vm)
                .evaluate(), epsilon);
    }

    /**
     * Test of summation-power precedence.
     */
    @Test
    public void testSummationPowerPrecedence1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Summation-Power Precedence 1:");
        assertEquals(81, MathExpressionParser.parse(
                "sum{i, 2, 4, i}^2", vm).evaluate(), epsilon);
    }

    /**
     * Test of summation-power precedence.
     */
    @Test
    public void testSummationPowerPrecedence2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Summation-Power Precedence 2:");
        assertEquals(29, MathExpressionParser.parse(
                "sum{i, 2, 4, i^2}", vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested curly bracket operators.
     */
    @Test
    public void testParsingNestedCurly1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested Curly Bracket Operators 1:");
        assertEquals(162, MathExpressionParser.parse(
                "sum{i, 2, 4, sum{j, 5, 7, i*j}}", vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested curly bracket operators.
     */
    @Test
    public void testParsingNestedCurly2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested Curly Bracket Operators 2:");
        assertEquals(972, MathExpressionParser.parse(
                "sum{i, 2, 4, sum{j, 5, 7, i*j} * 2} * 3", vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested curly bracket operators.
     */
    @Test
    public void testParsingNestedCurly3()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested Curly Bracket Operators 3:");
        assertEquals(46, MathExpressionParser.parse(
                "sum{k, 1, 2, sum{i, 2, 4, i} + sum{j, 2, 5, j}}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested curly bracket operators.
     */
    @Test
    public void testParsingNestedCurly4()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested Curly Bracket Operators 4:");
        assertEquals(76, MathExpressionParser.parse(
                "sum{k, 1, 2, prod{i, 2, 4, i} + sum{j, 2, 5, j}}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested curly bracket operators.
     */
    @Test
    public void testParsingNestedCurly5()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested Curly Bracket Operators 5:");
        assertEquals(1444, MathExpressionParser.parse(
                "prod{k, 1, 2, prod{i, 2, 4, i} + sum{j, 2, 5, j}}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested operators.
     */
    @Test
    public void testParsingNested1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested 1:");
        assertEquals(25, MathExpressionParser.parse(
                "max(prod{i, 2, 4, i}, sum{j, 7, 9, j} +1)",
                vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested operators.
     */
    @Test
    public void testParsingNested2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested 2:");
        assertEquals(24, MathExpressionParser.parse(
                "min(prod{i, 2, 4, i}, sum{j, 7, 9, j} +1)",
                vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested operators.
     */
    @Test
    public void testParsingNested3()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested 3:");
        assertEquals(23, MathExpressionParser.parse(
                "min(prod{i, 2, 4, i}, sum{j, 7, 9, j}-  1)",
                vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested operators.
     */
    @Test
    public void testParsingNested4()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested 4:");
        assertEquals(8, MathExpressionParser.parse(
                "prod{i, 3, 7, max(i%3, (i+1)%3)}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test of parsing nested operators.
     */
    @Test
    public void testParsingNested5()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting Nested 5:");
        assertEquals(8, MathExpressionParser.parse(
                "sum{i, 3, 7, max(i%3, (i+1)%3)}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test summation with five arguments.
     */
    @Test
    public void testFiveArgumentSummation1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument summation 1:");
        assertEquals(25, MathExpressionParser.parse(
                "sum{i, 1, 9, 2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test product with five arguments.
     */
    @Test
    public void testFiveArgumentProduct1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument product 1:");
        assertEquals(945, MathExpressionParser.parse(
                "prod{i, 1, 9, 2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test summation with five arguments.
     */
    @Test
    public void testFiveArgumentSummation2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument summation 2:");
        assertEquals(25, MathExpressionParser.parse(
                "sum{i, 1, 10, 2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test product with five arguments.
     */
    @Test
    public void testFiveArgumentProduct2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument product 2:");
        assertEquals(945, MathExpressionParser.parse(
                "prod{i, 1, 10, 2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test summation with five arguments.
     */
    @Test
    public void testFiveArgumentSummation3()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument summation 3:");
        assertEquals(25, MathExpressionParser.parse(
                "sum{i, 9, 1, -2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test product with five arguments.
     */
    @Test
    public void testFiveArgumentProduct3()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument product 3:");
        assertEquals(945, MathExpressionParser.parse(
                "prod{i, 9, 1, -2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test summation with five arguments.
     */
    @Test
    public void testFiveArgumentSummation4()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument summation 4:");
        assertEquals(0, MathExpressionParser.parse(
                "sum{i, 9, -9, -2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test product with five arguments.
     */
    @Test
    public void testFiveArgumentProduct4()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument product 4:");
        assertEquals(-893025, MathExpressionParser.parse(
                "prod{i, 9, -9, -2, i}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test summation with five arguments.
     */
    @Test
    public void testFiveArgumentSummation5()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument summation 5:");
        assertEquals(54, MathExpressionParser.parse(
                "sum{i, 9, 2, -3, i * sum{j, 1, 2, j}}",
                vm).evaluate(), epsilon);
    }

    /**
     * Test summation with five arguments.
     */
    @Test
    public void testFiveArgumentProduct5()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        double epsilon = 1e-10;
        System.out.println("\tTesting five argument product 5:");
        assertEquals(34992, MathExpressionParser.parse(
                "prod{i, 9, 2, -3, i * prod{j, 2, 3, j}}",
                vm).evaluate(), epsilon);

        AbstractNode node = MathExpressionParser.parse(
                "prod{i, 9, 2, -3, i * prod{j, 2, 3, j}}",
                vm);
        System.out.println(node.toString());
    }

    /**
     * Test of parse method, of class MathExpressionParser for invalid variable
     * names.
     */
    @Test(expected = MisplacedTokensException.class)
    public void testParseForInvalidVariableNames()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        System.out.println("Testing MathExpressionParser:parse() for invalid variable names");
        vm.set("1_asd", 10.2);
        MathExpressionParser.parse("1_asd", vm);
    }

    /**
     * Test of parse method, of class MathExpressionParser for empty
     * parentheses.
     */
    @Test(expected = InvalidFormatException.class)
    public void testParseForEmptyParentheses()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            MisplacedTokensException,
            InvalidFormatException,
            NoSuchMethodException,
            Throwable {
        System.out.println("Testing MathExpressionParser:parse() for empty parentheses");
        MathExpressionParser.parse("()", vm);
    }

    /**
     * Test the string representation of summation
     */
    @Test
    public void testSummationStringRepresentation1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            InvalidFormatException,
            Throwable {
        AbstractNode summation
                = MathExpressionParser.parse("sum{a, 1, 20, a^b}", vm);
        System.out.println(summation.toString());
        System.out.println("Testing summation string representation 1");
        assertEquals(
                removeSpaces("Sum{a=[1.0,20.0],(a^b)}"),
                removeSpaces(summation.toString()));
    }

    /**
     * Test the string representation of summation
     */
    @Test
    public void testSummationStringRepresentation2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            InvalidFormatException,
            Throwable {
        AbstractNode summation
                = MathExpressionParser.parse("sum{a, 1, 20, 2, a^b}", vm);
        System.out.println(summation.toString());
        System.out.println("Testing summation string representation 2");
        assertEquals(
                removeSpaces("Sum{a=[1.0,20.0],step=2.0,(a^b)}"),
                removeSpaces(summation.toString()));
    }

    /**
     * Test the string representation of product
     */
    @Test
    public void testProductStringRepresentation1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            InvalidFormatException,
            Throwable {
        AbstractNode summation
                = MathExpressionParser.parse("prod{a, 1, 20, a^b}", vm);
        System.out.println(summation.toString());
        System.out.println("Testing product string representation 1");
        assertEquals(
                removeSpaces("Prod{a=[1.0,20.0],(a^b)}"),
                removeSpaces(summation.toString()));
    }

    /**
     * Test the string representation of product
     */
    @Test
    public void testProductStringRepresentation2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            InvalidFormatException,
            Throwable {
        AbstractNode summation
                = MathExpressionParser.parse("prod{a, 1, 20, 2, a^b}", vm);
        System.out.println(summation.toString());
        System.out.println("Testing product string representation 2");
        assertEquals(
                removeSpaces("Prod{a=[1.0,20.0],step=2.0,(a^b)}"),
                removeSpaces(summation.toString()));
    }

    /**
     * Test the string representation of a nested expression
     */
    @Test
    public void testNestedStringRepresentation1()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            InvalidFormatException,
            Throwable {
        AbstractNode summation
                = MathExpressionParser.parse("sum{a, 10, 15, prod{b, 2, 5, 2, a^b}}", vm);
        System.out.println(summation.toString());
        System.out.println("Testing nested string representation 1");
        assertEquals(
                removeSpaces("Sum{a = [10.0, 15.0], Prod{b = [2.0, 5.0], step = 2.0, (a^b)}}"),
                removeSpaces(summation.toString()));
    }

    /**
     * Test the string representation of a nested expression
     */
    @Test
    public void testNestedStringRepresentation2()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            InvalidFormatException,
            Throwable {
        AbstractNode summation
                = MathExpressionParser.parse("prod{a, 10, 15, sum{b, 2, 5, 2, a^b}}", vm);
        System.out.println(summation.toString());
        System.out.println("Testing nested string representation 2");
        assertEquals(
                removeSpaces("Prod{a = [10.0, 15.0], Sum{b = [2.0, 5.0], step = 2.0, (a^b)}}"),
                removeSpaces(summation.toString()));
    }

    /**
     * Test the string representation of a nested expression
     */
    @Test
    public void testNestedStringRepresentation3()
            throws IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException,
            InvalidFormatException,
            Throwable {
        AbstractNode summation
                = MathExpressionParser.parse(
                        "prod{a, 15, 2, -3, sum{b, 2, 5, 2, a^b}}", vm);
        System.out.println(summation.toString());
        System.out.println("Testing nested string representation 3");
        assertEquals(
                removeSpaces(
                        "Prod{a = [15.0, 2.0], step = (-3.0), "
                        + "Sum{b = [2.0, 5.0], step = 2.0, (a^b)}}"),
                removeSpaces(summation.toString()));
    }

    /**
     * Test building expression tree using the low level approach (as opposed to
     * the high level approach of getting a tree by parsing a string
     * representing an expression)
     */
    @Test
    public void testTreeComposition() {
        double[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] y = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        VariablesManager varManager = new VariablesManager();
        varManager.setVector("x", x);
        varManager.setVector("y", y);
        varManager.set("pi", Math.PI);
        // Build expression tree
        AdditionBinaryOpNode plus = new AdditionBinaryOpNode(null, null);
        PowerNode power = new PowerNode(plus, null);
        VectorElementNode x_4 = new VectorElementNode(power, null, varManager, "x");
        LiteralNode lit_4 = new LiteralNode(x_4, null, 4);
        LiteralNode lit_5 = new LiteralNode(power, null, 5);
        MultiplicationBinaryOpNode times1 = new MultiplicationBinaryOpNode(plus, null);
        LiteralNode lit_13_05 = new LiteralNode(times1, null, 13.05);
        MultiplicationBinaryOpNode times2 = new MultiplicationBinaryOpNode(times1, null);
        LiteralNode lit_pi = new LiteralNode(times2, null, varManager.get("pi"));
        SummationNode sum = new SummationNode(times2, null, varManager, "i");
        LiteralNode sum_index_start = new LiteralNode(sum, null, 5);
        LiteralNode sum_index_end = new LiteralNode(sum, null, 7);
        ProductNode prod = new ProductNode(sum, null, varManager, "j");
        LiteralNode prod_index_start = new LiteralNode(prod, null, 1);
        LiteralNode prod_index_end = new LiteralNode(prod, null, 2);
        MultiplicationBinaryOpNode times3 = new MultiplicationBinaryOpNode(prod, null);
        VectorElementNode x_i = new VectorElementNode(times3, null, varManager, "x");
        VariableNode i = new VariableNode(x_i, null, varManager, "i");
        VectorElementNode y_j = new VectorElementNode(times3, null, varManager, "y");
        VariableNode j = new VariableNode(y_j, null, varManager, "j");
        // Evaluate expression tree
        assertEquals("((x[4.0] ^ 5.0) + (13.05 * (3.141592653589793 *"
                + " Sum{i=[5.0,7.0],Prod{j=[1.0,2.0],(x[i] * y[j])}})))",
                plus.toString());
        assertEquals(902975.2508456296, plus.evaluate(), 1e-10);
    }

    /**
     * A utility function used to return a white-space-less version of text.
     *
     * @param text the text whose white spaces need to be removed
     * @return a white-space-less version of (text)
     */
    private String removeSpaces(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }
}
