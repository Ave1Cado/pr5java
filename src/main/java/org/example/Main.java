package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final int TAPE_LENGTH_PER_PART = 2;
    private static final Scanner scanner = new Scanner(System.in);
    private static int breakCount = 0;
    private static final List<String> inventory = new ArrayList<>();

    public static void main(String[] args) {
        Zavod zavod = new Zavod("Маяк",
                "\tЗАО «ЗАВОД СПЕЦИАЛЬНОГО МАШИНОСТРОЕНИЯ «МАЯК» специализируется на производстве крупных (до 40 тонн) и \nмелких  литых изделий из cтали, чугуна, алюминия и его сплавов, а также меди и сплавов меди. Завод \nпроизводит изделия для энергетической, нефтехимической, металлургической промышленности, а также для \nмашиностроительной и деревообрабатывающих отраслей промышленного производства",
                "Литье и сплавы",
                "\tУровень «ЗАВОДА СПЕЦИАЛЬНОГО МАШИНОСТРОЕНИЯ «МАЯК» и качество производимой им продукции подтверждены \nсертификатом о соответствии системы менеджмента и качества организации требованиям по \nГОСТ Р ИСО 9001-2008: №РОСС RU.ЦШ00.К01865");

        for (int i = 0; i < 3; i++) {
            perekur();
        }

        ZavodManager zavodManager = new ZavodManager();
        zavodManager.orderMaterial();
        zavodManager.checkInventory();
        zavodManager.startProduction();

        resetBreakCount();
        displayBreakCount();
    }

    public static void perekur() {
        breakCount++;

        if (breakCount < 6) {
            System.out.println("Работник ушел на перекур. Количество перекуров: " + breakCount);
        } else {
            breakCount--;
            double random = Math.random();
            if (random < 0.3) {
                System.out.println("Работник вернулся с перекура пьяным. Придется закрыть завод на сегодня.");
            } else {
                System.out.println("Максимальное количество перекуров: " + breakCount + ". Сегодняшние перекуры израсходованы");
            }
            breakCount = 0;
        }
    }

    public static void resetBreakCount() {
        breakCount = 0;
    }

    public static void displayBreakCount() {
        System.out.println("Количество перекуров сброшено. Текущее количество перекуров: " + breakCount);
    }
}

class Zavod {
    private final String nameOfZavod;
    private final String descriptionOZ;
    private final String specializationOZ;
    private final String levelOZ;

    public Zavod(String nameOfZavod, String descriptionOZ, String specializationOZ, String levelOZ) {
        this.nameOfZavod = nameOfZavod;
        this.descriptionOZ = descriptionOZ;
        this.specializationOZ = specializationOZ;
        this.levelOZ = levelOZ;
        displayInfo();
    }

    public void displayInfo() {
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\tЗавод  " + nameOfZavod + ".\n\n" + descriptionOZ + ".\n\n" + levelOZ + ".\n\nНаша специализация: " + specializationOZ);
        System.out.println("----------------------------------------------------------------------------------------------------------");
    }
}

class ZavodManager {
    private final List<String> inventory = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public void orderMaterial() {
        int quantity = getValidInput(scanner, "Введите количество материала для заказа: ");
        System.out.print("Введите тип материала для заказа: ");
        String materialType = scanner.next();
        String detailInfo = materialType + ":" + quantity;
        inventory.add(detailInfo);
        System.out.println("Вы заказали " + quantity + " единиц материала типа " + materialType + ". Теперь на складе " + quantity + " единиц этого материала.");
    }

    public void checkInventory() {
        System.out.println("Проверка инвентаря:");
        System.out.println("На складе есть следующие детали:");
        for (String detailInfo : inventory) {
            String[] parts = detailInfo.split(":");
            System.out.println(parts[0] + ": " + parts[1] + " единиц");
        }
    }

    public void startProduction() {
        System.out.println("Выберите форму детали для литья:");
        String shape = scanner.next();
        System.out.println("Введите материал для производства:");
        String material = scanner.next();
        int quantity = getValidInput(scanner, "Введите количество производимых деталей: ");

        boolean materialAvailable = false;
        for (String item : inventory) {
            String[] parts = item.split(":");
            if (parts[0].equalsIgnoreCase(material) && Integer.parseInt(parts[1]) >= quantity) {
                materialAvailable = true;
                break;
            }
        }

        if (materialAvailable) {
            String detailInfo = shape + " " + material + ":" + quantity;
            inventory.add(detailInfo);
            System.out.println("Произведено и добавлено на склад " + quantity + " деталей формы " + shape + " из материала " + material + ".");
            double totalLength = calculateTapeLength(quantity);
            System.out.println("Общая длина ленты после создания детали: " + totalLength + " метров");
        } else {
            System.out.println("Недостаточно материала " + material + " на складе для производства.");
        }
    }

    private double calculateTapeLength(int quantity) {
        return quantity * Main.TAPE_LENGTH_PER_PART;
    }

    private int getValidInput(Scanner scanner, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            String userInput = scanner.next();
            if (isNumeric(userInput)) {
                input = Integer.parseInt(userInput);
                if (input > 0) {
                    break;
                } else {
                    System.out.println("Пожалуйста, введите положительное целое число.");
                }
            } else {
                System.out.println("Пожалуйста, введите корректное значение.");
            }
        }
        return input;
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
