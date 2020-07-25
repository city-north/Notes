package algorithms;

public class Test {
    public static void main(String[] args) {
        int n = 6;
        int count = 0;
        for(int i = 1; i < n; i = i * 2) {
            System.out.println(count++ +"and n =" +i);
        }
    }
}
