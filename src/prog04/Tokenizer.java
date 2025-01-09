package prog04;

public class Tokenizer {
  static Object[] tokenize (String input) {
    Tokenizer.input = input;
    index = 0;

    Object[] out = new Object[input.length()];
    int n = 0;
      
    while (!atEndOfInput ()) {
      if (isNumber())
        out[n++] = getNumber();
      else
        out[n++] = input.charAt(index++);
    }
      
    Object[] out2 = new Object[n];
    System.arraycopy(out, 0, out2, 0, n);
    return out2;
  }
    
  private static String input;
  private static int index;
    
  private static boolean atEndOfInput () {
    while (index < input.length() &&
           Character.isWhitespace(input.charAt(index)))
      index++;
    return index == input.length();
  }

  private static boolean isNumber () {
    return Character.isDigit(input.charAt(index));
  }
    
  private static double getNumber () {
    int index2 = index;
    while (index2 < input.length() &&
           (Character.isDigit(input.charAt(index2)) ||
            input.charAt(index2) == '.'))
      index2++;
    double d = 0;
    try {
      d = Double.parseDouble(input.substring(index, index2));
    } catch (Exception e) {
      System.out.println(e);
    }
    index = index2;
    return d;
  }
}

