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

    public void swap(int index1, int index2) {
        if (index1 == index2) {
            System.out.println("Index sama, tidak ada yang ditukar.");
            return;
        }

        Node node1 = top, node2 = top;
        int i = 0;

        while (node1 != null && i < index1) {
            node1 = node1.next;
            i++;
        }

        i = 0;
        while (node2 != null && i < index2) {
            node2 = node2.next;
            i++;
        }

        if (node1 == null || node2 == null) {
            System.out.println("Index tidak valid!");
            return;
        }

        String temp = node1.data;
        node1.data = node2.data;
        node2.data = temp;

        System.out.println("Berhasil swap index " + index1 + " dan " + index2);
    }

    public void show() {
        if (top == null) {
            System.out.println("Stack kosong.");
            return;
        }
        System.out.print("Stack: ");
        Node current = top;
        while (current != null) {
            System.out.print(" " + current.data);
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
            System.out.println("3. Swap");
            System.out.println("4. Tampilkan Stack");
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
                    System.out.print("Masukkan index pertama: ");
                    int i1 = sc.nextInt();
                    System.out.print("Masukkan index kedua: ");
                    int i2 = sc.nextInt();
                    stack.swap(i1, i2);
                    break;
                case 4:
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
