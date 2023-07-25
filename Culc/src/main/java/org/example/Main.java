package org.example;

import java.util.Scanner;

    public class Main {

        public static void main(String[] args) throws Exception {
            //Предлагаем пользователю ввести в консоль 2 числа.
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите 2 числа для их подсчета, например арабскими цифрами -> 2 + 3 или римскими -> IV - I");
            String input = scanner.nextLine();
            //Передаем числа в метод calc.
            System.out.println(calc(input));
        }

        public static String calc(String input) throws Exception {
            int number1; //Первое вводимое число.
            int number2; //Второе вводимое число.
            boolean isRom; //Переменная для выставления "Флага".
            String operation; // переменная в которой будут храниться арифметические операции (+ - * /).

            //Убираем из строки пробелы, если пользователь написал, например 2 + 2, а не 2+2.
            input = input.replaceAll("\\s", "");
            //Убираем из строки арифметические операции, чтобы проверить, что пользователь ввел 2 операнда с помощью регулярного выражения.
            String [] operands = input.split("[+\\-*/]");
            //Если пользователь ввел не 2 операнда, то выбрасываем исключение.
            if (operands.length !=2) throw new Exception("Вы ввели больше двух чисел или использовали неверную арифметическую операцию");
            //Определяем какую операцию ввел пользователь с помощью метода detectOperation и заносим эту операцию в переменную operation.
            operation = Counting.detectOperation(input);

            //Если оба числа римские
            if (RomanAndArabian.isRoman(operands[0]) && RomanAndArabian.isRoman(operands[1])) {
                //конвертируем оба числа в арабские для вычисления действия.
                number1 = RomanAndArabian.convertToArab(operands[0]);
                number2 = RomanAndArabian.convertToArab(operands[1]);
                isRom = true;
            }

            //Если оба числа арабские.
            else if (!RomanAndArabian.isRoman(operands[0]) && !RomanAndArabian.isRoman(operands[1])) {
                number1 = Integer.parseInt(operands[0]);
                number2 = Integer.parseInt(operands[1]);
                isRom = false;
            }

            //Если одни число римское, а другое - арабское.
            else {
                throw new Exception("Числа должны быть в одном формате, только арабские (2 - 1) или только римские числа (I + V)");
            }
            //Проверяем, попадают ли введенные числа в диапазон от 1 до 10.
            if (number1 > 10 || number2 > 10) {
                throw new Exception("Числа должны быть от 1 до 10");
            }
            int arab = Counting.calculation(number1, number2, operation);
            if (isRom) {
                //если римское число получилось меньше либо равно нулю, генерируем ошибку.
                if (arab <= 0) {
                    throw new Exception("Римское число должно быть больше нуля");
                }
                //конвертируем результат операции из арабского в римское.
                input = RomanAndArabian.convertToRom(arab);
            } else {
                //Конвертируем арабское число в тип String.
                input = String.valueOf(arab);
            }
            return input;
        }
    }

    class Numbers{
    /* Строковый массив с римскими цифрами от 0 до 100.
    Согласно ТЗ было сказано, что вводимые числа должны быть от 1 до 10 включительно, максимальное число,
    которое может получиться в ходе арифметической операции это 100, именно поэтому строковый массив был заполнен
    римскими цифрами до 100 (С). При этом данный строковый массив также является массивом соответствий между
    арабскими и римскими цифрами, поэтому довольно легко было реализовать методы convertToArab и convertToRom.
    */
    static String[] romArray = new String[]{"0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI",
            "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV",
            "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI",
            "XXXVII", "XXXVIII", "XXXIX", "XL", "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII",
            "XLIX", "L", "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX", "LXI", "LXII",
            "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX", "LXXI", "LXXII", "LXXIII", "LXXIV",
            "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX", "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV",
            "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC", "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII",
            "XCVIII", "XCIX", "C"};
    }

    class Counting {
    /*В метод detectOperation мы передаем исходную операцию, которую пользователь вводил в консоль
      Проверяем, если переданная строчка содержит знак +, то возвращаем знак + и т.д.
      Если переданная строка содержит знаки недопустимые знаки, например %, то возвращаем null.
    */
        public static String detectOperation(String input) {
                if (input.contains("+")) return "+";
                else if (input.contains("-")) return "-";
                else if (input.contains("*")) return "*";
                else if (input.contains("/")) return "/";
                else return null;
        }

        /*Метод calculation принимает 2 числа и арифметическую операцию.
        * Проверяется, если знак арифметический знак был +, то возвращаем a+b и т.д.
        * */
        public static int calculation(int a, int b, String operation) {
            return switch (operation) {
                case "+" -> a + b;
                case "-" -> a - b;
                case "*" -> a * b;
                default -> a / b;
            };
        }
    }

    class RomanAndArabian {

        //Метод проверяет, являются ли числа, которые ввел пользователь римскими.
        public static boolean isRoman(String val){
            for (String s : Numbers.romArray) {
                if (val.equals(s)) {
                    return true;
                }
            }
            return false;
        }

        /*Метод переводит римские цифры в арабские
        * Метод ищет римское число в строковом массиве romArray, после нахождения числа берет ее индекс,
        * так как индекс римской V в массиве - это арабское число 5.
        * */
        public static int convertToArab(String roman) {
            for (int i = 0; i < Numbers.romArray.length; i++) {
                if (roman.equals(Numbers.romArray[i])) {
                    return i;
                }
            }
            return 0;
        }

        /*Метод переводит арабские цифры в римские
          Арабское число является индексом для римского числа, поэтому просто берем римское число
          из массива romArray и находим верное число.
         */
        public static String convertToRom(int arabian) {
            return Numbers.romArray[arabian];
        }
    }