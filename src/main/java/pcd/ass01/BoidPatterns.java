package pcd.ass01;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoidPatterns {
    private final List<Color> colors = List.of(Color.RED, Color.GREEN, Color.YELLOW);
    private final List<String> shapes = List.of("p", "t");
    private final List<Pattern> patterns;
    private int currentIndex = 0;

    public BoidPatterns() {
        patterns = generatePatterns();
    }

    private List<Pattern> generatePatterns() {
        List<Pattern> patternList = new ArrayList<>();
        for (String shape : shapes) {
            for (Color color : colors) {
                patternList.add(new Pattern(color, shape));
            }
        }
        return patternList;
    }

    public int getPatternsCount() {
        return patterns.size();
    }

    public Pattern getNextPattern() {
        if (currentIndex >= patterns.size()) {
            throw new IllegalStateException("There are no more available patterns!");
        }
        return patterns.get(currentIndex++);
    }

    public static class Pattern {
        private final Color color;
        private final String shape;

        public Pattern(Color color, String shape) {
            this.color = color;
            this.shape = shape;
        }

        public Color getColor() {
            return color;
        }

        public String getShape() {
            return shape;
        }
    }
}
