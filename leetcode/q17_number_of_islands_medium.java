import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Q17) Number of Islands
 *
 * AI-BEST:
 * Flood-fill from each unvisited land cell.
 * Time: O(rows * cols), Space: O(rows * cols) worst case
 *
 * AI-EASY:
 * BFS with separate visited matrix.
 * Time: O(rows * cols), Space: O(rows * cols)
 */
public class q17_number_of_islands_medium {
    static int numIslandsBest(char[][] grid) {
        if (grid.length == 0 || grid[0].length == 0) return 0;

        // Step 1: Clone grid so we can mark visited in place.
        // Why: in-place marking avoids extra visited matrix.
        char[][] board = cloneGrid(grid);
        int rows = board.length;
        int cols = board[0].length;
        int islands = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] != '1') continue;

                // Step 2: Start flood-fill when new land component is found.
                islands++;
                ArrayDeque<int[]> stack = new ArrayDeque<>();
                stack.push(new int[]{r, c});
                board[r][c] = '0';

                while (!stack.isEmpty()) {
                    int[] cell = stack.pop();
                    int cr = cell[0];
                    int cc = cell[1];
                    for (int[] n : neighbors(cr, cc)) {
                        int nr = n[0], nc = n[1];
                        if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
                        if (board[nr][nc] != '1') continue;
                        board[nr][nc] = '0';
                        stack.push(new int[]{nr, nc});
                    }
                }
            }
        }

        // Step 3: Return number of components discovered.
        return islands;
    }

    static int numIslandsEasy(char[][] grid) {
        if (grid.length == 0 || grid[0].length == 0) return 0;

        int rows = grid.length;
        int cols = grid[0].length;

        // Step 1: Track visited separately.
        // Why: simpler mental model than mutating input data.
        boolean[][] visited = new boolean[rows][cols];
        int islands = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != '1' || visited[r][c]) continue;
                islands++;

                // Step 2: BFS from unseen land to mark full island.
                Queue<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[]{r, c});
                visited[r][c] = true;

                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    int cr = cell[0];
                    int cc = cell[1];
                    for (int[] n : neighbors(cr, cc)) {
                        int nr = n[0], nc = n[1];
                        if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
                        if (grid[nr][nc] != '1' || visited[nr][nc]) continue;
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }

        // Step 3: Return island count.
        return islands;
    }

    static int[][] neighbors(int r, int c) {
        return new int[][]{
                {r - 1, c},
                {r + 1, c},
                {r, c - 1},
                {r, c + 1}
        };
    }

    static char[][] cloneGrid(char[][] grid) {
        char[][] copy = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        return copy;
    }

    static String gridToString(char[][] grid) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < grid.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(Arrays.toString(grid[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Q17: Number of Islands");
        char[][][] cases = {
                {
                        {'1', '1', '1', '1', '0'},
                        {'1', '1', '0', '1', '0'},
                        {'1', '1', '0', '0', '0'},
                        {'0', '0', '0', '0', '0'}
                },
                {
                        {'1', '1', '0', '0', '0'},
                        {'1', '1', '0', '0', '0'},
                        {'0', '0', '1', '0', '0'},
                        {'0', '0', '0', '1', '1'}
                }
        };

        for (char[][] grid : cases) {
            System.out.println("Input: grid=" + gridToString(grid));
            System.out.println("  BEST: " + numIslandsBest(grid));
            System.out.println("  EASY: " + numIslandsEasy(grid));
        }
    }
}
