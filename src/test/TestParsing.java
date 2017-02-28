/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import types.AbstractNode;
import core.MathExpressionParser;
import exceptions.MisplacedTokensException;
import core.VariablesManager;
import exceptions.NonIntegralIndexException;
import exceptions.TooManyDecimalPointsException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haitham
 */
public class TestParsing {

    public static void main(String[] args) throws TooManyDecimalPointsException, MisplacedTokensException, Throwable {
        try {
            // Set variables
            VariablesManager vm = new VariablesManager();
            // Parse expression (create parse tree)
            // CASE 01
//            AbstractNode parseTree = MathExpressionParser.parse("20 * -+5 + 33", vm); // -67
            // CASE 02
//            AbstractNode parseTree = MathExpressionParser.parse("20*-+5++33", vm); // -67 This expression is not even supported by Java because of the two consecutive plus '+' signs.
            // CASE 03
//            AbstractNode parseTree = MathExpressionParser.parse("+20-+-1", vm); // 21
            // CASE 04
//            AbstractNode parseTree = MathExpressionParser.parse("((10*+-9))", vm); // -90
            // CASE 05
//            AbstractNode parseTree = MathExpressionParser.parse("23/10.2 + 10.23*15", vm); // 155.70490196078433
            // CASE 06
//            AbstractNode parseTree = MathExpressionParser.parse("tan(0.2)", vm); // 0.2027100355086725
            // CASE 07
//            vm.setVector("x_$11", new double[]{10, 20, 30, 40, 50});
//            AbstractNode parseTree = MathExpressionParser.parse("sum{k,2,4,x_$11[k]}", vm); // 90 Notice that counting starts from 1
            // CASE 08
//            vm.set("1_asd", 10.2); // Notice that an invalid variable name can be added to the variables manager, but it will not work during parsing. (should be fixed to throw an error here instead)
//            AbstractNode parseTree = MathExpressionParser.parse("1_asd", vm); // <Error> invalid variable name
            // CASE 09
//            vm.setVector("x_$11", new double[]{10, 20, 30, 40, 50});
//            vm.set("_asd", 10.2); 
//            vm.set("i", 5);
//            AbstractNode parseTree = MathExpressionParser.parse(
//                    "23/(_asd + 10.23)*15 -  10/88*99-x_$11[i*2-9]/50 "
//                            + "+ sum{k,2,4,x_$11[k]+100*  tan(0.2)"
//                            + "-100*sin(0.2)/cos(0.2)}", vm); // 95.43693098384728
            // CASE 10
//            vm.set("i", 5);
//            vm.setVector("x_$11", new double[]{10, 20, 30, 40, 50});
//            AbstractNode parseTree = MathExpressionParser.parse("  1.02- x_$11[5]* prod{i,10 ,12.0,i+1} + 4+85794.98", vm); // Zero
            // CASE 11
//            AbstractNode parseTree = MathExpressionParser.parse("Sum{ii, 1, 5, ii%  2}", vm); // 3 The number of odd integers between 1 and 5 inclusive.
            // CASE 12
//            AbstractNode parseTree = MathExpressionParser.parse("cos(acos(sin(1.5707963267948966)))", vm); // 1.0 For testing nested trigonometric functions
            // CASE 13
//            AbstractNode parseTree = MathExpressionParser.parse("()", vm); // <Error> For testing empty parentheses
            // CASE 14
//            AbstractNode parseTree = MathExpressionParser.parse("-   --   2.0", vm); // -2 For testing nested trigonometric functions
            // CASE 15
//            AbstractNode parseTree = MathExpressionParser.parse("sqrt(16.0)", vm); // -2 For testing nested trigonometric functions
            // CASE 16
//            vm.setConstant("PI", Math.PI);
//            vm.set("var", 5);
//            AbstractNode parseTree = MathExpressionParser.parse("2 * var * PI", vm); // -2 For testing nested trigonometric functions
            // CASE 17
//            vm.setVector("x", new double[]{10, 20, 30, 40, 50, 60, 70, 80, 90});
//            vm.setConstant("pi", Math.PI);
//            AbstractNode parseTree = MathExpressionParser.parse("0.5 * x[2] * x[3] * x[4] * (1 + 100 * (5 + sum{i,5,9, (x[i] - 0.5)^2 - cos(2 * pi * (x[i] - 0.5))}))", vm);
            // CASE 18
//            double[] x = {0.00464678000485, 0.27587023044768, 0.71549533066038, 0.91117851440468, 0.55985687416083, 0.53741305862749, 0.34872737808561, 0.93529514059711, 0.50314421329251, 0.41581614753265, 0.86181838164073, 0.33368738365047, 0.40812915550712, 0.24947594908016};
//            vm.setVector("x", x);
//            vm.setConstant("pi", Math.PI);
//            AbstractNode parseTree1 = MathExpressionParser.parse("-pi/2 * sin(x[1] * pi / 2) * cos(x[2] * pi / 2) * cos(x[3] * pi / 2) * sin(x[4] * pi / 2) * (1 + sum{i,5,14, (x[i] - 0.5)^2})", vm);
//            AbstractNode parseTree2 = MathExpressionParser.parse("-pi/2 * cos(x[1] * pi / 2) * sin(x[2] * pi / 2) * cos(x[3] * pi / 2) * sin(x[4] * pi / 2) * (1 + sum{i,5,14, (x[i] - 0.5)^2})", vm);
//            AbstractNode parseTree3 = MathExpressionParser.parse("-pi/2 * cos(x[1] * pi / 2) * cos(x[2] * pi / 2) * sin(x[3] * pi / 2) * sin(x[4] * pi / 2) * (1 + sum{i,5,14, (x[i] - 0.5)^2})", vm);
//            AbstractNode parseTree4 = MathExpressionParser.parse("+pi/2 * cos(x[1] * pi / 2) * cos(x[2] * pi / 2) * cos(x[3] * pi / 2) * cos(x[4] * pi / 2) * (1 + sum{i,5,14, (x[i] - 0.5)^2})", vm);
//            // Evaluate parse tree and print 
//            System.out.println("Value-1 = " + parseTree1.evaluate());
//            System.out.println("Value-2 = " + parseTree2.evaluate());
//            System.out.println("Value-3 = " + parseTree3.evaluate());
//            System.out.println("Value-4 = " + parseTree4.evaluate());
            // CASE 19
//            vm.set("var", -50.2);
//            vm.set("my_var", -100);
//            vm.set("happy_var", 88);
//            vm.set("sad_var", -100.002);
//            vm.set("cool_var", 2);
//            AbstractNode parseTree = MathExpressionParser.parse("min(var,my_var,  happy_var ,-happy_var*cool_var,  sad_var, cool_var)", vm);
            // CASE 20
//            vm.setVector("x", new double[]{100.5, -66.5, -200, 30.03});
//            vm.set("y", -100);
//            AbstractNode parseTree = MathExpressionParser.parse("min(10,sum{i,2,3, x[i]},  y)", vm);
            // CASE 21
//            vm.setVector("x", new double[]{0.000, 2.2, 0.5, -1});
//            vm.set("y", 3);
//            AbstractNode parseTree = MathExpressionParser.parse("min(10,prod{  i,2 , 3, x[i]},  y)", vm); // 1.1
            // CASE 22
//            vm.setVector("x", new double[]{10, 1, 1000, 100});
//            AbstractNode parseTree = MathExpressionParser.parse("sum{i,1,3,min(x[i],x[i+1])}", vm); // 1 + 1 + 100 = 102
            // CASE 23
//            vm.setVector("x", new double[]{10, 5, 1000, 100});
//            AbstractNode parseTree = MathExpressionParser.parse("prod{i,1,2,min(x[i],x[i+1],  x[i+2])}", vm); // 5 * 5 = 25
            // CASE 24
//            vm.set("var", -50.2);
//            vm.set("my_var", -100);
//            vm.set("happy_var", 88);
//            vm.set("sad_var", -100.002);
//            vm.set("cool_var", 2);
//            AbstractNode parseTree = MathExpressionParser.parse("max(var,my_var,  happy_var ,happy_var*cool_var,  sad_var, cool_var)", vm);
            // CASE 25
//            vm.setVector("x", new double[]{100.5, 66.5, 200, 30.03});
//            vm.set("y", -100);
//            AbstractNode parseTree = MathExpressionParser.parse("max(10,sum{i,2,3, x[i]},  y)", vm);
            // CASE 26
//            vm.setVector("x", new double[]{0.000, 2.2, 0.5, -1});
//            vm.set("y", -3);
//            AbstractNode parseTree = MathExpressionParser.parse("max(0.1,prod{  i,2 , 3, x[i]},  y)", vm); // 1.1
            // CASE 22
//            vm.setVector("x", new double[]{10, 1, 1000, 100});
//            AbstractNode parseTree = MathExpressionParser.parse("sum{i, 1, 3, max(x[i],x[i+1])}", vm); // 10 + 1000 + 1000 = 2010
            // CASE 23
//            vm.setVector("x", new double[]{10, 5, 1000, 100});
//            AbstractNode parseTree = MathExpressionParser.parse("prod{i,1,2,max(x[i],x[i+1],  x[i+2])}", vm); // 1000 * 1000 = 1000000
            // CASE 24
            //MathExpressionParser.parse("pi = 3.14", vm).evaluate();
            // CASE 25
//            vm.set("r", 5);
//            vm.set("h", 7);
//            AbstractNode parseTree = MathExpressionParser.parse("2*(pi=3.14)*r*h + pi*r^2", vm);
            // CASE 26
//            AbstractNode parseTree = MathExpressionParser.parse("abs(tan(2.3) - 10)", vm);
            // CASE 27
            vm.set("A", .8);
            vm.set("B", .75);
            vm.set("C", .85);
            vm.setConstant("PI", Math.PI);
            vm.setVector("z", new double[]{1,2,3,4});
            AbstractNode parseTree = MathExpressionParser.parse("(1.0/14.0)*(6 * (A + (min(0.0, floor((abs(z[3] - 0.35) / abs(floor(0.35 - z[3]) + 0.35)) - B)) * A * (B - (abs(z[3] - 0.35) / abs(floor(0.35 - z[3]) + 0.35))) / B) - min(0.0, floor(C - (abs(z[3] - 0.35) / abs(floor(0.35 - z[3]) + 0.35)))) * (1.0 - A) * ((abs(z[3] - 0.35) / abs(floor(0.35 - z[3]) + 0.35)) - C) / (1.0 - C))^2+ 8 * (A + (min(0.0, floor((abs(z[4] - 0.35) / abs(floor(0.35 - z[4]) + 0.35)) - B)) * A * (B - (abs(z[4] - 0.35) / abs(floor(0.35 - z[4]) + 0.35))) / B) - min(0.0, floor(C - (abs(z[4] - 0.35) / abs(floor(0.35 - z[4]) + 0.35)))) * (1.0 - A) * ((abs(z[4] - 0.35) / abs(floor(0.35 - z[4]) + 0.35)) - C) / (1.0 - C))^0.02)+ 2 *(1 - cos((z[1]^0.02)*PI /2))*(1 - cos((z[2]^0.02)*PI /2))", vm);

            System.out.println("Result = " + parseTree.evaluate());
            // Display precedence table
            MathExpressionParser.displayLevels();
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NonIntegralIndexException ex) {
            Logger.getLogger(TestParsing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
