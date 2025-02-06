package WeChat;

import java.util.*;

public class CastList {
    public static <T> LinkedList<T> castList(Object obj, Class<T> clazz) {
        LinkedList<T> result = new LinkedList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
    public static <U> TreeSet<U> castTree(Object obj, Class<U> clazz) {
        TreeSet<U> result = new TreeSet<>();
        if (obj instanceof Set<?>) {
            for (Object o : (Set<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}
