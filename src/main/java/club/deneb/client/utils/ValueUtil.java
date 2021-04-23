package club.deneb.client.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValueUtil {
    public static List<String> listOf(String ...strings){
        return Arrays.stream(strings).collect(Collectors.toList());
    }
}
