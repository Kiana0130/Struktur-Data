import java.util.Scanner;

class Node {
    String data;
    Node next;
    Node(String data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedListStack {
    private Node top;

    public void push(String item) {
        Node newNode = new Node(item);
        newNode.next = top;
        top = newNode;
        System.out.println("Pushed: " + item);
    }

    public String pop() {
        if (top == null) {
            System.out.println("Stack kosong! Tidak bisa pop.");
            return null;
        }
        String removed = top.data;
        top = top.next;
        System.out.println("Popped: " + removed);
        return removed;
    }

    public void show() {
        System.out.print("Stack (atas -> bawah): ");
        Node current = top;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}

public class StackLinkedList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LinkedListStack stack = new LinkedListStack();

        while (true) {
            System.out.println("\n=== MENU STACK ===");
            System.out.println("1. Push");
            System.out.println("2. Pop");
            System.out.println("3. Tampilkan Stack");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            int pilih = sc.nextInt();
            sc.nextLine();

            switch (pilih) {
                case 1:
                    System.out.print("Masukkan data: ");
                    stack.push(sc.nextLine());
                    break;
                case 2:
                    stack.pop();
                    break;
                case 3:
                    stack.show();
                    break;
                case 0:
                    System.out.println("Keluar dari Stack.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}
