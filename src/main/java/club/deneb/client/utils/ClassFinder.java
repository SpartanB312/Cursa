package club.deneb.client.utils;

import org.reflections.Reflections;

import java.util.Set;

@SuppressWarnings("rawtypes")
public class ClassFinder {
    public static Set findClasses(String pack, Class subType) {
        Reflections reflections = new Reflections(pack);
        return reflections.getSubTypesOf(subType);
    }
}
