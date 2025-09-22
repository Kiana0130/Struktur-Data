import java.util.Scanner;

class Node {
    String data;
    Node next;

    Node(String data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedListQueue {
    private Node front, rear;

    public void enqueue(String item) {
        Node newNode = new Node(item);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        System.out.println("Enqueued: " + item);
    }

    public String dequeue() {
        if (front == null) {
            System.out.println("Queue kosong! Tidak bisa dequeue.");
            return null;
        }
        String removed = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        System.out.println("Dequeued: " + removed);
        return removed;
    }

    public void swap(int index1, int index2) {
        if (index1 == index2) {
            System.out.println("Index sama, tidak ada yang ditukar.");
            return;
        }

        Node node1 = front, node2 = front;
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
        if (front == null) {
            System.out.println("Queue kosong.");
            return;
        }
        System.out.print("Queue: ");
        Node current = front;
        while (current != null) {
            System.out.print(" " + current.data);
            current = current.next;
        }
        System.out.println();
    }

    public void perulangan() {
        for (int i = 1; i <= 10; i++){
            
        };

    }
}

public class QueueLinkedList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LinkedListQueue queue = new LinkedListQueue();

        while (true) {
            System.out.println("\n=== MENU QUEUE ===");
            System.out.println("1. Enqueue");
            System.out.println("2. Dequeue");
            System.out.println("3. Swap");
            System.out.println("4. Tampilkan Queue");
            System.out.println("5. Perulangan");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            int pilih = sc.nextInt();
            sc.nextLine();

            switch (pilih) {
                case 1:
                    System.out.print("Masukkan data: ");
                    queue.enqueue(sc.nextLine());
                    break;
                case 2:
                    queue.dequeue();
                    break;
                case 3:
                    System.out.print("Masukkan index pertama: ");
                    int i1 = sc.nextInt();
                    System.out.print("Masukkan index kedua: ");
                    int i2 = sc.nextInt();
                    queue.swap(i1, i2);
                    break;
                case 4:
                    queue.show();
                    break;
                case 5:
                    queue.perulangan();
                    break;
                case 0:
                    System.out.println("Keluar dari Queue.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}