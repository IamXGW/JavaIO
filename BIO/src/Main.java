import java.io.*;
import java.util.*;

/**
 * 输入一个 n * m 的图
 * 举例：
 * 4 4
 * 1122
 * 1222
 * 3111
 * 3333
 */
public class Main {
    // 记得抛异常
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] params = br.readLine().trim().split(" ");
        int n = Integer.parseInt(params[0]);
        int m = Integer.parseInt(params[1]);
        char[][] grid = new char[n][m];
        for(int i = 0; i < n; i++)
            grid[i] = br.readLine().trim().toCharArray();
    }
}
