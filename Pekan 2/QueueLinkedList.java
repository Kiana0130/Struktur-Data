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

    public void show() {
        System.out.print("Queue (depan -> belakang): ");
        Node current = front;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
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
            System.out.println("3. Tampilkan Queue");
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
                    queue.show();
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
