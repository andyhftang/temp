import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String var1 = "this is notthing ${ARN:ID:KEY} after the string";
        Pattern pattern = Pattern.compile("\\$\\{([^:]+):([^:]+):([^}]+)\\}");
        Matcher matcher = pattern.matcher(var1);

        if (matcher.find()) {
            String arn = matcher.group(1);
            String id = matcher.group(2);
            String key = matcher.group(3);

            System.out.println("ARN: " + arn);
            System.out.println("ID: " + id);
            System.out.println("Key: " + key);
        } else {
            System.out.println("No match found");
        }
    }
}
