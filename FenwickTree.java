import java.util.Scanner; // единственный класс, который использовала для чтения ввода с клавиатуры

public class FenwickTree {
    private int[] tree;
    private final int[] inputArr;
    private int n;

    // константа для разделительной линии (чтобы не нагромождать)
    private static final String SEPARATOR = "------------------------------";

    public FenwickTree(int size) {
        this.n = size;
        this.tree = new int[n + 1];
        this.inputArr = new int[n + 1];
    }

    // Метод для вывода наших заголовков
    private void printHeader(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println(title.toUpperCase());
        System.out.println(SEPARATOR);
    }

    // построение самого дерева Фенвика
    public void build(int[] arr) {
        printHeader("построение дерева фенвика");

        for (int i = 0; i < n; i++) {
            inputArr[i] = arr[i];
        }

        for (int i = 0; i <= n; i++) {
            tree[i] = 0;
        }
        for (int i = 0; i < n; i++) {
            int j = i + 1;
            while (j <= n) {
                tree[j] += inputArr[i];
                j += j & -j;
            }
        }

        printState();
        System.out.println("Дерево успешно построено!");
    }
    // здесь мы обновляем элемент
    public void update(int index, int delta) {
        printHeader("обновление элемента");

        System.out.println("ДО обновления:");
        printState();

        int oldValue = inputArr[index];
        System.out.printf("Обновление: arr[%d] = %d + %d = %d\n\n",
                index, oldValue, delta, oldValue + delta);

        inputArr[index] += delta;

        int i = index + 1;
        while (i <= n) {
            tree[i] += delta;
            i += i & -i;
        }

        printHeader("после обновления");
        printState();
    }

    // префиксная сумма от 0 до index
    public int prefixSum(int index) {
        printHeader("вычисление префиксной суммы");
        printState();

        int sum = 0;
        int i = index + 1;

        System.out.print("Вычисление: prefix(" + index + ") = ");

        boolean first = true;
        while (i > 0) {
            if (!first) System.out.print(" + ");
            System.out.print("tree[" + i + "](" + tree[i] + ")");
            sum += tree[i];
            first = false;
            i = i - (i & -i);
        }
        System.out.println(" = " + sum);

        // обязательно выполняем проверку
        System.out.print("Проверка: ");
        int checkSum = 0;
        for (int j = 0; j <= index; j++) {
            checkSum += inputArr[j];
            System.out.print(inputArr[j]);
            if (j < index) System.out.print(" + ");
        }
        System.out.println(" = " + checkSum);

        return sum;
    }

    // сумма на отрезке [left, right]
    public int rangeSum(int left, int right) {
        printHeader("сумма на отрезке [" + left + ", " + right + "]");
        printState();

        System.out.printf("Формула: sum[%d,%d] = prefix(%d) - prefix(%d)\n",
                left, right, right, left - 1);

        int prefixRight = 0;
        int i = right + 1;
        while (i > 0) {
            prefixRight += tree[i];
            i = i - (i & -i);
        }

        int prefixLeftMinus = 0;
        if (left > 0) {
            i = left;
            while (i > 0) {
                prefixLeftMinus += tree[i];
                i = i - (i & -i);
            }
        }

        int result = prefixRight - prefixLeftMinus;

        System.out.printf("prefix(%d) = %d\n", right, prefixRight);
        System.out.printf("prefix(%d) = %d\n", left - 1, prefixLeftMinus);
        System.out.printf("Результат: %d - %d = %d\n",
                prefixRight, prefixLeftMinus, result);

        System.out.print("Проверка: ");
        int directSum = 0;
        for (int j = left; j <= right; j++) {
            directSum += inputArr[j];
            System.out.print(inputArr[j]);
            if (j < right) System.out.print(" + ");
        }
        System.out.println(" = " + directSum);

        return result;
    }

    // вывод текущего состояния
    public void printState() {
        System.out.print("Индексы массива:  ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();

        System.out.print("Исходный массив:  ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d ", inputArr[i]);
        }
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.print("Индексы дерева:   ");
        for (int i = 1; i <= n; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();

        System.out.print("Дерево Фенвика:   ");
        for (int i = 1; i <= n; i++) {
            System.out.printf("%2d ", tree[i]);
        }
        System.out.println();
        System.out.println(SEPARATOR);
    }

    // главное меню (для удобства сделала также, как и в 1 labe)
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FenwickTree fenwick = null;

        System.out.println("Привет! давай построим дерево Фенвика вместе :)");

        while (true) {
            System.out.println("\n" + SEPARATOR);
            System.out.println("ГЛАВНОЕ МЕНЮ");
            System.out.println(SEPARATOR);
            System.out.println("1. Построить дерево из массива");
            System.out.println("2. Обновить элемент");
            System.out.println("3. Найти префиксную сумму");
            System.out.println("4. Найти сумму на отрезке");
            System.out.println("5. Показать текущее состояние");
            System.out.println("0. Выход");
            System.out.println(SEPARATOR);
            System.out.print("Выберите пункт меню: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите размер массива: ");
                    int size = scanner.nextInt();
                    fenwick = new FenwickTree(size);

                    System.out.print("Введите " + size + " элементов массива: ");
                    int[] arr = new int[size];
                    for (int i = 0; i < size; i++) {
                        arr[i] = scanner.nextInt();
                    }
                    fenwick.build(arr);
                    break;

                case 2:
                    if (fenwick == null) {
                        System.out.println("Сначала постройте дерево!");
                        break;
                    }
                    System.out.print("Введите индекс для обновления (0-" + (fenwick.n - 1) + "): ");
                    int updateIndex = scanner.nextInt();
                    System.out.print("Введите значение для добавления: ");
                    int delta = scanner.nextInt();
                    fenwick.update(updateIndex, delta);
                    break;

                case 3:
                    if (fenwick == null) {
                        System.out.println("Сначала постройте дерево!");
                        break;
                    }
                    System.out.print("Введите индекс для префиксной суммы (0-" + (fenwick.n - 1) + "): ");
                    int prefixIndex = scanner.nextInt();
                    fenwick.prefixSum(prefixIndex);
                    break;

                case 4:
                    if (fenwick == null) {
                        System.out.println("Сначала постройте дерево!");
                        break;
                    }
                    System.out.print("Введите левую границу (0-" + (fenwick.n - 1) + "): ");
                    int left = scanner.nextInt();
                    System.out.print("Введите правую границу (" + left + "-" + (fenwick.n - 1) + "): ");
                    int right = scanner.nextInt();
                    fenwick.rangeSum(left, right);
                    break;

                case 5:
                    if (fenwick == null) {
                        System.out.println("Сначала постройте дерево!");
                        break;
                    }
                    fenwick.printHeader("текущее состояние");
                    fenwick.printState();
                    break;

                case 0:
                    System.out.println("Пока! Увидимся позже!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
}