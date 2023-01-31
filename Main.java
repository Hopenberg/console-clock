import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int length = 71;
        final int height = 51;
        // jeśli będziemy chcieli zmieniać długość symbol'u, to będziemy musieli wykonać operacje na length i height i innych
        String symbol = "*";
        String spaceSymbol = " ";

        //center axis
        final int verticalCenter = (int) Math.ceil(length / 2.0);
        final int horizontalCenter = (int) Math.ceil(height / 2.0);

        final Field[][] field = new Field[height][length];

        double second = 0;
        while (true) {
            for (int i = 0; i < 50; ++i) System.out.println();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < length; j++) {
                    draw(field, i, j, verticalCenter, horizontalCenter, symbol, spaceSymbol, second);
                }
            }
            drawField(field);
            System.out.println(second);
            second++;
            if (second == 60) {
                second = 0;
            }

            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void drawField(Field[][] field) {
        for (Field[] row : field) {
            for (Field col : row) {
                System.out.print(col.getSymbol());
            }
            System.out.print('\n');
        }
    }

    private static void draw(
            Field[][] field,
            int i,
            int j,
            int verticalCenter,
            int horizontalCenter,
            String symbol,
            String spaceSymbol,
            double second
    ) {
        boolean symbolPrinted = false;
        //indeks: sekundy, wartość: mnożnik
        double[] xMultipliers = {0, 1/7.0, 2/7.0, 3/7.0, 4/7.0, 5/7.0, 6/7.0, 1, 2, 3, 4, 5, 6, 7, /* i: 14 */ 8};
        double[] xMultipliersQ13 = {1/7.0, 2/7.0, 3/7.0, 4/7.0, 5/7.0, 6/7.0, 1, 2, 3, 4, 5, 6, 7, /* i: 14 */ 8, 1};
        double[] yMultipliers = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1, /* i: 14 */ 1};
        double[] yMultipliersQ13 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1, /* i: 14 */ 0};
        int quarter = getQuarterFromSecond(second);

        int x = j - verticalCenter;
        int y = horizontalCenter - i;



        // rysowanie wskazówek
        if (getRulesForCoordinatesForQuarter(x, y, quarter)) {

            double yMultiplier = 0;
            double xMultiplier = 0;

            int indexForMultipliers = 14 - (int) second % 15;
            xMultiplier = xMultipliers[indexForMultipliers];
            yMultiplier = yMultipliers[indexForMultipliers];
            if (quarter == 1 || quarter == 3) {
                indexForMultipliers = (int) second % 15;
                xMultiplier = xMultipliersQ13[indexForMultipliers];
                yMultiplier = yMultipliersQ13[indexForMultipliers];
            }

            if (
                    yMultiplier * y
                            == applyMinusBasedOnQuarter(quarter) * xMultiplier * x
            ) {
                field[i][j] = new Field();
                symbolPrinted = true;
            }
        }


        // rysowanie óś
        if (
                (i == horizontalCenter && j % 2 == 0)
                || (j == verticalCenter && i % 2 == 0)
        ) {
            field[i][j] = new Field();
            symbolPrinted = true;
        }

        // wypełnianie przestrzeni
        if (!symbolPrinted) {
            field[i][j] = new Field("  ");
        }
    }

    private static int applyMinusBasedOnQuarter(int quarter) {
        if (quarter == 1 || quarter == 3) {
            return -1;
        } else {
            return 1;
        }
    }

    private static boolean getRulesForCoordinatesForQuarter(int x, int y, int quarter) throws IllegalArgumentException {
        if (quarter == 0) {
            return x >= 0 && y >= 0;
        } else if (quarter == 1) {
            return x >= 0 && y <= 0;
        } else if (quarter == 2) {
            return x <= 0 && y <= 0;
        } else if (quarter == 3) {
            return x <= 0 && y >= 0;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static int getQuarterFromSecond(double second) throws IllegalArgumentException {
        if (second >= 0 && second <= 59) {
            return (int) Math.floor(second / 15.0);
        } else {
            throw new IllegalArgumentException();
        }
    }
}