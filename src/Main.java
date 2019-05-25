import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.joining;

public class Main {

    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.print("CRC (0) / Paritate bidimensionala (1): ");
        String outputMessage = scanner.nextInt() == 0 ? crc(): paritateBidimensionala();
        System.out.print(String.format("Output message: %s", outputMessage));
    }

    static String crc() {
        System.out.print("M(x): ");

        String mxInput = scanner.next();

        if (mxInput.startsWith("0")) {
            cancelWithMessage("M(x) should start with '1'. Exiting...");
        }

        BigInteger mx = new BigInteger(mxInput, 2);

        System.out.print("C(x): ");
        String cxInput = scanner.next();

        if (cxInput.startsWith("0")) {
            cancelWithMessage("C(x) should start with '1'. Exiting...");
        }

        BigInteger cx = new BigInteger(cxInput, 2);

        BigInteger tx = new BigInteger(mx.toString(2) + String.join("", Collections.nCopies(cxInput.length() - 1, "0")), 2);

        System.out.println("T(x): " + tx.toString(2));

        BigInteger rest = xorRemainder(tx, cx);
        System.out.println("Rest: " + rest.toString(2));

        BigInteger finale = xorSubtract(tx, rest);

//        return finale.toString(2);

        return finale.divide(rest).toString(2);

    }

    static String paritateBidimensionala() {
        System.out.print("M(x): ");

        String mx = scanner.next();

        if (!(mx.startsWith("1") && mx.length() % 7 == 0)) {
            cancelWithMessage("M(x) should start with '1' and have a number of letters divisible with 7.");
        }

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < mx.length() / 7; i++) {
            lines.add(mx.substring(7 * i, 7 * i + 7));
        }

        String firstOutputPart = lines.stream() // divide by 7 otherwise it wont work
                .map(line -> line + (line.replace("0", "").length() % 2 == 0 ? "0" : "1"))
                .collect(joining());

        List<String> columns = new ArrayList<>();

        for (int columnIndex = 0; columnIndex < 7; columnIndex++) {
            String column = "";
            for (int i = 0; i < lines.size(); i++) {
                column += lines.get(i).charAt(columnIndex);
            }
            columns.add(column);
        }

        String secondOutputPart = columns.stream()
                                .map(column -> column.replace("0", "").length() % 2 == 0 ? "0" : "1")
                                .collect(joining());

        return firstOutputPart + secondOutputPart;
    }

    static void cancelWithMessage(String message) {
        System.out.println(message);
        System.exit(0);
    }

    static BigInteger xorRemainder(BigInteger value, BigInteger divider) {
        if (value.toString(2).length() < divider.toString(2).length()) {
            return value;
        } else {
            BigInteger dividableValue = new BigInteger(value.toString(2).substring(0, divider.toString(2).length()), 2);
            BigInteger xor = dividableValue.xor(divider);
            BigInteger newValue = new BigInteger(xor.toString(2) + value.toString(2).substring(divider.toString(2).length()), 2);
            return xorRemainder(newValue, divider);
        }
    }

    static BigInteger xorSubtract(BigInteger value, BigInteger subtractor) {
        String binaryValue = value.toString(2);
        String binarySubtractor = subtractor.toString(2);

        BigInteger subtractable = new BigInteger(binaryValue.substring(binaryValue.length() - binarySubtractor.length()), 2);
        BigInteger subtracted = subtractable.xor(subtractor);

        return new BigInteger(binaryValue.substring(0, binaryValue.length() - binarySubtractor.length()) + subtracted.toString(2), 2);
    }
}
