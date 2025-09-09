import java.util.ArrayList;
import java.util.Scanner;

class Stack {
    private ArrayList<String> items = new ArrayList<>();

    public void push(String item) {
        items.add(item);
        System.out.println("Pushed: " + item);
    }

    public String pop() {
        if (items.isEmpty()) {
            System.out.println("Stack kosong! Tidak bisa pop.");
            return null;
        }
        String removed = items.remove(items.size() - 1);
        System.out.println("Popped: " + removed);
        return removed;
    }

    public void swap(int index1, int index2) {
        if (index1 >= 0 && index1 < items.size() && index2 >= 0 && index2 < items.size()) {
            String temp = items.get(index1);
            items.set(index1, items.get(index2));
            items.set(index2, temp);
            System.out.println("Swapped: " + items);
        } else {
            System.out.println("Indeks tidak valid untuk swap.");
        }
    }

    public void show() {
        System.out.println("Stack: " + items);
    }
}

class Queue {
    private ArrayList<String> items = new ArrayList<>();

    public void enqueue(String item) {
        items.add(item);
        System.out.println("Enqueued: " + item);
    }

    public String dequeue() {
        if (items.isEmpty()) {
            System.out.println("Queue kosong! Tidak bisa dequeue.");
            return null;
        }
        String removed = items.remove(0);
        System.out.println("Dequeued: " + removed);
        return removed;
    }

    public void swap(int index1, int index2) {
        if (index1 >= 0 && index1 < items.size() && index2 >= 0 && index2 < items.size()) {
            String temp = items.get(index1);
            items.set(index1, items.get(index2));
            items.set(index2, temp);
            System.out.println("Swapped: " + items);
        } else {
            System.out.println("Indeks tidak valid untuk swap.");
        }
    }

    public void show() {
        System.out.println("Queue: " + items);
    }
}

public class Stack_Queue {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Stack stack = new Stack();
        Queue queue = new Queue();

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Push (Stack)");
            System.out.println("2. Pop (Stack)");
            System.out.println("3. Enqueue (Queue)");
            System.out.println("4. Dequeue (Queue)");
            System.out.println("5. Swap Stack");
            System.out.println("6. Swap Queue");
            System.out.println("7. Tampilkan Semua");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            int pilih = sc.nextInt();
            sc.nextLine(); 

            switch (pilih) {
                case 1:
                    System.out.print("Masukkan data untuk Stack: ");
                    stack.push(sc.nextLine());
                    break;
                case 2:
                    stack.pop();
                    break;
                case 3:
                    System.out.print("Masukkan data untuk Queue: ");
                    queue.enqueue(sc.nextLine());
                    break;
                case 4:
                    queue.dequeue();
                    break;
                case 5:
                    System.out.print("Masukkan index pertama: ");
                    int s1 = sc.nextInt();
                    System.out.print("Masukkan index kedua: ");
                    int s2 = sc.nextInt();
                    stack.swap(s1, s2);
                    break;
                case 6:
                    System.out.print("Masukkan index pertama: ");
                    int q1 = sc.nextInt();
                    System.out.print("Masukkan index kedua: ");
                    int q2 = sc.nextInt();
                    queue.swap(q1, q2);
                    break;
                case 7:
                    stack.show();
                    queue.show();
                    break;
                case 0:
                    System.out.println("Keluar");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}