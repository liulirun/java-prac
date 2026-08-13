import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QuickRun2 {
  public static void main(String[] args) {
    int[] nums = { 1, 2, 3, -4, 5, 7, 17, 8 };
    int target = 13;
    int[] result = twoSum(nums, target);
    System.out.println(Arrays.toString(result));
    // String s = "abaapokacvgghhw";
    // int length = lengthOfLongestSubstring(s);
    // System.out.println(length);
  }
  public static int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> holder = new HashMap<Integer, Integer>();
    for (int index = 0; index < nums.length; index++) {
      int expected = target - nums[index];
      if (holder.containsKey(expected)) {
        return new int[] { holder.get(expected), index };
      }
      holder.put(nums[index], index);
    }
    return new int[] { 0, 0 };
  }
  //
  public static int lengthOfLongestSubstring(String inputStr) {
    if (inputStr.isBlank() || inputStr.isEmpty())
      return 0;
    int left = 0;
    int maxLength = 0;
    int startPoint = 0;
    Map<Character, Integer> charIndex = new HashMap<Character, Integer>();
    // 右指针（快指针）不断前进，吞入新的字符，扩展窗口
    for (int right = 0; right < inputStr.length(); right++) {
      char currentChar = inputStr.charAt(right);
      // 检查当前字符是否在【当前窗口内】重复了
      if (charIndex.containsKey(currentChar)) {
        /*
         * 核心动作：左指针跳跃 1. charIndex.get(currentChar) +
         * 1：让左指针瞬间跳到该重复字符上一次出现位置的下一个位置，把它挤出窗口。 2. Math.max(left,
         * ...)：防止左指针“往回跳”到旧窗口之外的过期字符（比如 "abba" 的第二个 'a'）。
         */
        left = Math.max(left, charIndex.get(currentChar) + 1);
      }
      // 更新或记录当前字符的最右位置，以便下次重复时参考
      charIndex.put(currentChar, right);
      if (maxLength < right - left + 1) {
        // 记录结果的开始点。可以打印出最长String，而不是length
        startPoint = left;
        // 每一次右指针移动后，计算当前窗口的长度（right - left + 1），并更新历史最大值
        maxLength = Math.max(maxLength, right - left + 1);
      }
    }
    System.out.println(inputStr.substring(startPoint, startPoint + maxLength));
    return maxLength;
  }
}
